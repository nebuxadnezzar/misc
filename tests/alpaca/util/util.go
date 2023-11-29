// util.go
package util

import (
	"fmt"
	"time"
)

type Number interface {
	int | int64 | float64
}

func Max[V Number](nums ...V) V {
	var mx V = V(nums[0])
	for _, val := range nums {
		if val > mx {
			mx = val
		}
	}
	return mx
}

func Min[V Number](nums ...V) V {
	var mn V = V(nums[0])
	for _, val := range nums {
		if val < mn {
			mn = val
		}
	}
	return mn
}

func Track(name string) func() {
	start := time.Now()
	return func() {
		fmt.Printf("%s, execution time %s\n", name, time.Since(start).String())
	}
}
