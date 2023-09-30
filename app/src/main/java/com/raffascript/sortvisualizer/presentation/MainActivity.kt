package com.raffascript.sortvisualizer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.raffascript.sortvisualizer.presentation.theme.AlgorithmsVisualizerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlgorithmsVisualizerTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }
    }
    /*
     * heapsort update progress
     * To do:
     * measure time (maybe only with delay 0)
     * Typo
     * full screen
     * show algorithm best/worst/average time complexity, description ...
     * sounds
     * launcher icon
     * Implement Algorithms:
     * - MergeSort
     * - QuickSort
     * - RadixSort
     * - HeapSort
     * - CountingSort
     * (- TimSort)
     */
}