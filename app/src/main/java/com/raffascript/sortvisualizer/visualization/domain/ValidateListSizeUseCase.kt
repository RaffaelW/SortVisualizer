package com.raffascript.sortvisualizer.visualization.domain

class ValidateListSizeUseCase {
    operator fun invoke(listSize: Int): Boolean {
        return listSize in 3..5000
    }
}