// cli.go
package main

import (
	"alpaca/bitset"
	"alpaca/repl"
	"alpaca/util"
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"
)

const MAXLOTTERYNUM = 90

func main() {

	if len(os.Args) < 2 {
		fmt.Printf("Missing arguments\n")
		os.Exit(1)
	}
	filename := os.Args[1]
	index, total, err := load(filename)

	if err != nil {
		fmt.Fprintf(os.Stderr, "failed to load %s: %v\n", filename, err)
		os.Exit(1)
	}
	callback := func(query string) bool {
		fmt.Printf("%s\n", query)
		duration := util.Track("report numbers ")
		nums := inputtoslice(query)
		fmt.Printf("%s winners for numbers %v\n", report(index, total, nums...), nums)
		duration()
		return true
	}
	fmt.Println("READY: enter your numbers below or press Ctrl+C to exit")
	repl.Repl(`--> `, 20, callback)
}

func report(index map[int][]int, total int, numbers ...int) string {
	/*
		bs := bitset.NewBitset(total)
		for k, n := range numbers {
			v, ok := index[n]
			if !ok {
				continue
			}
			if k > 0 {
				bs = op(&bs, total, v)
			} else {
				bs.SetAll(v, true)
			}
		}
		//fmt.Printf("SLICE: %v\n", bs.Slice())
		return len(bs.Slice())
	*/

	// 7 lines added after feedback
	sc := reporthelper(index, total, numbers...)
	template := `
	numbers matching | winners
	5                | %5d
	4                | %5d
	3                | %5d
	2                | %5d

	`
	return fmt.Sprintf(template, sc[3], sc[2], sc[1], sc[0])
}

// added after feedback
func reporthelper(index map[int][]int, total int, numbers ...int) [4]int {
	var scores [4]int
	bs := bitset.NewBitset(total)
	for k, n := range numbers {
		v, ok := index[n]
		if !ok {
			continue
		}
		if k > 0 {
			bs = op(&bs, total, v)
		} else {
			bs.SetAll(v, true)
		}
		//fmt.Printf("K %d BS: %v\n", k, bs.Slice())
		if k > 0 {
			scores[k-1] = len(bs.Slice())
		}
	}
	return scores
}

func op(bs *bitset.Bitset, total int, nums []int) bitset.Bitset {
	tb := bitset.NewBitset(total)
	tb.SetAll(nums, true)
	tb = bs.Intersection(&tb)
	return tb
}

func load(filename string) (map[int][]int, int, error) {

	f, err := os.Open(filename)

	if err != nil {
		return nil, 0, fmt.Errorf("error opening file: %v", err)
	}
	defer f.Close()
	var e error = nil
	var s string
	index := map[int][]int{}
	cnt := 0
	r := bufio.NewReader(f)

	for cnt = 0; e == nil; cnt++ {
		s, e = r.ReadString(uint8(10))
		if e != nil {
			break
		}
		ss := strings.Split(strings.Trim(s, " \n\r"), ` `)
		if err = addtoindex(index, ss, cnt); err != nil {
			fmt.Fprintf(os.Stderr, "error loading record %s: %v\n", s, err)
		}

	}
	fmt.Printf("LOADED %d records\n", cnt)
	return index, cnt, nil
}

func addtoindex(index map[int][]int, keys []string, id int) error {
	for _, v := range keys {
		if v == "" {
			continue
		}
		k, err := strconv.Atoi(v)
		if err != nil {
			return err
		}
		// 3 lines added after feedback
		if k < 1 || k > 90 {
			return fmt.Errorf("value %d out of range", k)
		}

		if v, ok := index[k]; ok {
			index[k] = append(v, id)
		} else {
			index[k] = []int{id}
		}
	}
	return nil
}

func inputtoslice(input string) []int {
	regex := regexp.MustCompile(`(\p{N}+)(\W|$)`)
	res := regex.FindAllStringSubmatch(input, -1)
	output := make([]int, 0, 5)
	for i := range res {
		if i >= 5 {
			break
		}
		//fmt.Printf("%d --> %s, %s\n", i, res[i][1], res[i][2])
		if s, err := strconv.Atoi(res[i][1]); err == nil {
			output = append(output, s)
		}
	}
	return output
}
