# TickTackToe - which letter won
## Build and Run instruction
1) javac -d . Main.java
2) java t.Main
3) enter X, O or - and press enter
4) repeat 3 times and press Ctrl+Z on Windows or Ctrl+D on Linux

## This code provides solution for the following problem.

Let say we provided with following input (dash represents draw):
```
X O O
O X -
O O X

```
Our program needs to be able to tell which letter won: X or O - by returning X or O.

I was given - I think 90 minutes - to solve it, but being slow thinker I needed more time and meanwhile started with
regex. Mistake! It has much simpler solution as you can see. Also I was not able to get Stdout debug lines printed at this filtered.ai site. The code pad had very small letters so I coded in my Notepad++ and tried to paste my code, but that was blocked. Anyway, here is solution, maybe one of you will find this helpful.
