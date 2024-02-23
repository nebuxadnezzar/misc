
## Story
__Alpaca Inc(?)__ presented me with this take home assignment described [here](pdf/Software_Engineering_Test.pdf).

## Data
Sample file [here](data/lottery-300.txt)
```
4 79 13 80 56
71 84 48 85 38
41 65 39 82 36
36 42 79 21 58
...
```
__or__ to generate 10 million records lottery numbers file yourself
```ruby
ruby -e '50000000.times.map{rand(1..90)}.each_with_index{|a,i| printf("\n") if i > 0 && i % 5 == 0; printf("%s ", a)}'

or to generate without trailing space

ruby -e '50000000.times.map{rand(1..90)}.each_with_index{|a,i| if i > 0 then i % 5 == 0 ? printf("\n") : print(" ") end; printf("%d",a)}'
```
### Building binary
```
make all
```
### Running code
```
make run arg1=~/path/to/10m-v2.txt
```

### Run results

```
go run cmd/cli.go ~/test-data/10m-v2.txt
error loading record 85 21 67 93 2
: value 93 out of range
error loading record 10 85 69 -7 50
: value -7 out of range
LOADED 10000000 records
READY: enter your numbers below or press Ctrl+C to exit
--> 77 88 21 33 1

	numbers matching | winners
	5                |     0
	4                |    12
	3                |   917
	2                | 25008

	 winners for numbers [77 88 21 33 1]
report numbers , execution time 63.910126ms

```

### Alpaca feedback
*I will leave it mostly uncommented ...*

>Unfortunately you were not selected to advance to the next stage of screening.
>
>For the homework I would like to share the following feedback:
>
>Pros:
>- Divided into multiple packages
>- Faster than linear search
>- Fun CLI with history
>
>Cons:
>
>- It doesn't implement the assignment (e.g. it should return the number of winners with 5, 4, 3 and 2  matches) <span style="color:blue">*Why?*</span>
>- No unit tests <span style="color:blue">*None were required in the assignment.*</span>
>- No validation of the lotto numbers (e.g they should be in the range of [1 ... 90], each line should contain 5 entries)
>- O(N) solution with a constant less than one <span style="color:blue">*Is sub-linear solution that searches 10 million records in ~60ms bad?*</span>
>- The prompt handling requires external utilities (stty) instead of relying on libraries
>- CTRL-C handling should be done using signals instead of terminal manipulation
>- Anding the bitsets could be done inplace preventing unnecessary memory allocation in cmd.op
>

I can think of  some kind of solution using sort whereby you first sort every line, then sort whole file thus having the same items together and then use binary search to locate one of the same items in the sequence and then you would need to count them. But at this point I can't think of anything else, so if someone can please share.
I think the code performed pretty good finding and counting winners in 10 million set under 70ms (I tried several runs).
