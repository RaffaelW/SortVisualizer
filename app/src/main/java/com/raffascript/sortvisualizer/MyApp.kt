package com.raffascript.sortvisualizer

import android.app.Application
import com.raffascript.sortvisualizer.di.AppModule
import com.raffascript.sortvisualizer.di.AppModuleImpl

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }

    companion object {
        lateinit var appModule: AppModule
    }
}