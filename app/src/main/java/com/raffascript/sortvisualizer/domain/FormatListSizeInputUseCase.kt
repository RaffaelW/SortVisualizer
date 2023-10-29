package com.raffascript.sortvisualizer.domain

import com.raffascript.sortvisualizer.core.util.ErrorMessage
import com.raffascript.sortvisualizer.core.util.Resource

class FormatListSizeInputUseCase {

    operator fun invoke(input: String): Resource<Int> {
        val result = input.toIntOrNull()
        result ?: return Resource.Failure(ErrorMessage.InvalidListSizeInput)
        return Resource.Success(result)
    }
}