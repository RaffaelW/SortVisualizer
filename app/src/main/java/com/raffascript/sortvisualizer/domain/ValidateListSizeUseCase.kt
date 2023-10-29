package com.raffascript.sortvisualizer.domain

class ValidateListSizeUseCase {
    operator fun invoke(listSize: Int): Boolean {
        return listSize in 3..5000
    }
}