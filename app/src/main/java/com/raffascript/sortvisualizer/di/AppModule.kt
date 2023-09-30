package com.raffascript.sortvisualizer.di

import android.content.Context
import com.raffascript.sortvisualizer.data.AlgorithmRegister

interface AppModule {
    val algorithmRegister: AlgorithmRegister
}

class AppModuleImpl(private val context: Context) : AppModule {
    override val algorithmRegister: AlgorithmRegister by lazy {
        AlgorithmRegister()
    }
}