// repl.go
package repl

import (
	"bytes"
	"fmt"
	"math"
	"os"
	"os/exec"
	"os/signal"
	"strings"
	"syscall"
)

const BUFF_LEN int = 1024

func main() {
	fmt.Println("Hello World!")
	callback := func(s string) bool {
		if s == `qq` {
			fmt.Println("Quitting...")
			return false
		}
		os.Stdout.Write([]byte(strings.ToUpper(s)))
		return true
	}
	Repl("--> ", 5, callback)
}

func Repl(prompt string, histSize int, callback func(string) bool) {

	setup()
	defer cleanup()
	interruptCleanup()
	data := make([]byte, 3)
	buff := make([]byte, BUFF_LEN)
	f := os.Stdin
	offset := len(prompt) + 1
	addToHist, keyUp, keyDown, showHistory := createHistoryFunc(histSize)
	index := 0
	cnt := 0

	printPrompt := func() {
		fmt.Printf("%s", prompt)
		setCursoToPos(len(prompt) + 1)
		fmt.Print("\033[0K") // clear line
	}
	resetBuf := func() {
		clearBuf(buff, cnt)
		cnt = 0
		index = 0
	}
	processCallback := func(s string) bool {
		//fmt.Printf("[%s]\n", s)
		printPrompt()
		retval := callback(s)
		fmt.Println()
		printPrompt()
		return retval
	}
	processKeyUpDown := func(s string) {
		cnt = len(s)
		index = cnt
		copy(buff, s)
	}
	printPrompt()

	keep_looping := true

	for keep_looping {
		_, err := f.Read(data)

		if err != nil {
			fmt.Printf("ERROR: %#v\n", err)
			continue
		}
		ch := data[0]
		if ch == 3 { /* Ctrl+C */
			return
		} else if 32 < ch && ch <= 126 {
			if index < cnt {
				copyBytes(buff, index+1, index)
			}
			buff[index] = ch
			index++
			cnt++
		} else if ch == 10 || ch == 13 {
			setCursoToPos(offset)
			s := string(buff[:cnt])
			addToHist(s)
			keep_looping = processCallback(s)
			resetBuf()
			continue
		} else if ch == 27 {
			if data[1] == 91 {
				if data[2] == 68 { /* left */
					index = decrement(index)
				} else if data[2] == 67 { /* right */
					index = increment(index)
				} else if data[2] == 65 { /* key up */
					processKeyUpDown(keyUp())
				} else if data[2] == 66 { /* key down */
					processKeyUpDown(keyDown())
				} else if data[2] == 72 { /* key home - show history */
					fmt.Printf("\n%s", showHistory())
				}
			}
		} else if ch == 127 {
			index = decrement(index)
			cnt = decrement(cnt)
			copyBytes(buff, index, index+1)
		} else if ch == 32 {
			copyBytes(buff, index+1, index)
			index = increment(index)
			cnt = increment(cnt)
		}
		printPrompt()

		// echo characters to the screen
		fmt.Print(string(buff[:cnt]))
		setCursoToPos(offset)

		if index > 0 { /* move cursor to the index */
			fmt.Printf("\033[%dC", index)
		}
	}
}

func createHistoryFunc(histSize int) (func(string), func() string, func() string, func() string) {

	buf := make([]string, histSize)
	sz := 1
	idx := 0
	upIdx := 0
	downIdx := 0

	funcAdd := func(s string) {
		if sz < len(buf) {
			sz++
		}
		buf[idx] = s
		upIdx = idx
		idx = (idx + 1) % sz
	}

	funcUp := func() string {
		d := sz
		if d < 1 {
			return ""
		}
		s := buf[upIdx]
		upIdx = ((upIdx-1)%d + d) % d
		downIdx = (upIdx + 1) % d
		return s
	}

	funcDown := func() string {
		d := sz
		if d < 1 {
			return ""
		}
		downIdx = (downIdx + 1) % d
		upIdx = ((downIdx-1)%d + d) % d
		s := buf[downIdx]
		return s
	}

	funcShowHistory := func() string {
		var b bytes.Buffer
		for i := 0; i < sz; i++ {
			b.WriteString(fmt.Sprintf("\t%02d %s\n", i+1, buf[i]))
		}
		return b.String()
	}
	return funcAdd, funcUp, funcDown, funcShowHistory
}
func copyBytes(bytes []byte, to int, from int) {
	n := len(bytes)
	if from > n {
		from = n
	}

	if n < 1 || to < 0 || to > n || from > n || from < 0 {
		return
	}

	dir := 1
	dif := from - to
	l := len(bytes)
	k := l

	if dif < 0 {
		dir = -1
		k = from
		to = l - 1
	}

	for i := to; (dif > 0 && i+dif < k) || (dif < 0 && i+dif >= k); i += dir {
		bytes[i] = bytes[i+dif]
	}
	if dif > 0 {
		for i, k := len(bytes)-1, 0; k < dif; k++ {
			bytes[i-k] = 0x20
		}
	} else {
		dif *= -1
		for i, k := from, 0; k < dif; k++ {
			bytes[i+k] = 0x20
		}
	}
}

func decrement(i int) int {
	return int(math.Max(0, float64(i-1)))
}

func increment(i int) int {
	return int(math.Min(float64(BUFF_LEN), float64(i+1)))
}

func moveCursorLeft(pos int) {
	fmt.Printf("\033[%dD", pos)
}

func setCursoToPos(pos int) {
	fmt.Printf("\033[%dG", pos)
}

func clearBuf(a []byte, sz int) int {
	n := len(a)
	if sz < n {
		n = sz
	}
	for i := 0; i < n; i++ {
		a[i] = 0
	}
	return n
}

func setup() {
	exec.Command("stty", "-F", "/dev/tty", "-echo").Run()
	exec.Command("stty", "-F", "/dev/tty", "cbreak", "min", "1").Run()
}

func cleanup() {
	exec.Command("stty", "-F", "/dev/tty", "echo").Run()
	exec.Command("stty", "-F", "/dev/tty", "-cbreak", "min", "1").Run()
}

func interruptCleanup() {

	c := make(chan os.Signal, 2)
	signal.Notify(c, os.Interrupt, syscall.SIGTERM)
	go func() {
		<-c
		fmt.Println("\nReceived an interrupt, exiting REPL...\n")
		exec.Command("stty", "-F", "/dev/tty", "echo").Run()
		exec.Command("stty", "-F", "/dev/tty", "-cbreak", "min", "1").Run()
		os.Exit(0)
	}()
}
