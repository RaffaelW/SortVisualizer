package com.raffascript.sortvisualizer.domain

class CheckListSizeInputUseCase {
    operator fun invoke(input: String): Boolean {
        val number = input.toIntOrNull() ?: return false
        return number in 3..5000
    }
}