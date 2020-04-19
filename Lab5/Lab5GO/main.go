package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

func init() {
	rand.Seed(time.Now().Unix())
}

func CreateArrays(rowSize, colSize int) [][]int {
	array := make([][]int, rowSize)
	for i := range array {
		array[i] = make([]int, colSize)
		for j := range array[i] {
			array[i][j] = rand.Intn(2)
		}
	}
	return array
}

func changeElement(arr []int, arrSize int, group *sync.WaitGroup) {
	var changeIdx = rand.Intn(arrSize)
	var dir = rand.Intn(2)

	if dir == 0 {
		arr[changeIdx]--
	} else {
		arr[changeIdx]++
	}
	group.Done()
}

func checkArrayRule(array [][]int, rowSize int) bool {
	sum := make([]int, rowSize)
	for i := range array {
		var counter = 0
		for _, val := range array[i] {
			counter += val
		}
		sum[i] = counter
	}

	res := false
	fmt.Println("Sum: ", sum)
	for i := range sum {
		if sum[0] != sum[i] {
			res = false
		}
	}
	return res
}

func RunSimulation(array [][]int, rowSize, colSize int, group *sync.WaitGroup) {
	stopFlag := false
	for !stopFlag {
		group.Add(rowSize)
		for i := 0; i < rowSize; i++ {
			go changeElement(array[i], colSize, group)
		}
		group.Wait()
		if checkArrayRule(array, rowSize) {
			stopFlag = true
			fmt.Println("Arrays matched the rule")
			fmt.Println(array)
		}
		fmt.Println(array)
		fmt.Println()
		time.Sleep(100 * time.Millisecond)
	}
}

func main() {
	const (
		N       = 3
		ArrSize = 5
	)

	arr := CreateArrays(N, ArrSize)
	group := new(sync.WaitGroup)
	RunSimulation(arr, N, ArrSize, group)
}
