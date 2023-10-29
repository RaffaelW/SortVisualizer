package com.raffascript.sortvisualizer.core.util

sealed class Resource<T>(val data: T?, val error: ErrorMessage? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Failure<T>(error: ErrorMessage?, data: T? = null) : Resource<T>(data, error)
}