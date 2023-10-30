# Todo List

## To do

+ bugs: no known bugs
+ (show description of algorithm)
+ button in bottom bar to disable the sound
+ Implement Algorithms:
  + Quick Sort
  + Radix Sort
  + Counting Sort
  + Tim Sort
  + Bogo Sort
  + Shell Sort
  + Comb Sort
  + Tree Sort
  + Strand Sort
  + Cycle Sort
  + Pigeonhole Sort
  + Bucket Sort
+ create nice README.md file

## Doing

+ optimize SurfaceView for multiple CPUs (already wasted 4h here...)
+ sounds

## Done

+ change orientation to portrait when leaving the visualizer screen in
  portrait mode
+ change color theming
+ launcher icon
+ show algorithm best/worst/average time complexity, ...
+ save latest delay and list size value
+ button to restore default values for delay, listSize
+ full screen
+ migrate Algorithms to new code structure
+ Improve code structure and implement use cases
+ choose better fitting delay values
+ Split dependency injection module into different files
+ Implemented Algorithms:
  + InsertionSort
  + SelectionSort
  + BubbleSort
  + HeapSort
  + MergeSort
  + Shaker Sort
+ resolved bugs:
  + java.lang.IllegalStateException: Surface has already been released
    (ChartSurfaceView, l. 138)
  + SurfaceView is empty when activity is paused &rarr; exit app, screen
    turned off, screen rotated