package bitset

import (
	"alpaca/util"
	"fmt"
	"math"
)

const MAXBITSETSZ = 20_000_000

type Bitset []uint8

func NewBitset(n int) Bitset {
	sz := 0
	if n > 0 {
		if n > MAXBITSETSZ {
			n = MAXBITSETSZ
		}
		sz = int(math.Ceil(float64(n+7) / 8))
	}
	return make(Bitset, sz)
}

func (bs Bitset) Len() int {
	return 8 * len(bs)
}

func (bs Bitset) Get(idx int) (bool, error) {
	if pos, offset, err := getPosAndOffset(idx, bs.Len()); err == nil {
		return (bs[pos] & (uint8(1) << offset)) != 0, nil
	} else {
		return false, err
	}
}

func (bs Bitset) Set(idx int, val bool) error {
	if pos, offset, err := getPosAndOffset(idx, bs.Len()); err == nil {
		if val {
			bs[pos] |= (uint8(1) << offset)
		} else {
			bs[pos] &= ^(uint8(1) << offset)
		}
	} else {
		return err
	}
	return nil
}

func (bs Bitset) SetAll(idx []int, val bool) error {
	for _, v := range idx {
		if err := bs.Set(v, val); err != nil {
			return err
		}
	}
	return nil
}

func getPosAndOffset(idx int, ln int) (int, uint, error) {
	if idx < 0 {
		idx = ln + idx
	}
	if !(idx >= 0 && idx < ln) {
		return -1, 0, fmt.Errorf("index %d out of range", idx)
	}
	pos := idx / 8
	offset := uint(idx % 8)
	return pos, offset, nil
}

func (bs Bitset) Intersection(bp *Bitset) Bitset {
	f := func(b1, b2 uint8) uint8 { return (b1 & b2) }
	return op(bs, *bp, f)
}

func (bs Bitset) Union(bp *Bitset) Bitset {
	f := func(b1, b2 uint8) uint8 { return (b1 | b2) }
	return op(bs, *bp, f)
}

func (bs Bitset) Difference(bp *Bitset) Bitset {
	f := func(b1, b2 uint8) uint8 { return (b1 & b2) }
	a := op(bs, *bp, f).Slice()
	c := bs.Clone()
	for _, v := range a {
		c.Set(v, false)
	}
	return c
}

func op(bs1, bs2 Bitset, opfunc func(uint8, uint8) uint8) Bitset {
	l1 := bs1.Len()
	l2 := bs2.Len()
	if l1 == 0 || l2 == 0 {
		return NewBitset(0)
	}
	sz := util.Min(l1, l2) / 8
	bi := NewBitset(util.Max(l1, l2))
	for i, k := 0, sz; i < k; i++ {
		b := opfunc(bs1[i], bs2[i])
		if b > 0 {
			bi[i] |= b
		}
	}
	return bi
}

func (bs Bitset) Invert() {
	for k, v := range bs {
		bs[k] = ^v
	}
}

func (bs Bitset) ClearAll() {
	for k, v := range bs {
		bs[k] = v ^ v
	}
}

func (bs Bitset) Clone() Bitset {
	bn := make(Bitset, len(bs))
	copy(bn, bs)
	return bn
}

func (bs Bitset) Slice() []int {
	l := len(bs) * 8 / 3
	if l < 1 {
		l = 1
	}
	a := make([]int, 0, l)
	mask := uint8(1)
	for k, v := range bs {
		if v < 1 {
			continue
		}
		for i := uint(0); i < 8; i++ {
			b := mask & (v >> i)
			if b > 0 {
				a = append(a, k*8+int(i))
			}
		}
	}
	return a
}

func (bs Bitset) dump() {
	for k, v := range bs {
		fmt.Printf("%02d %08b\n", k, v)
	}
}
