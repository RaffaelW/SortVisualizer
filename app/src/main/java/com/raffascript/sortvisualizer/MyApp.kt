package com.raffascript.sortvisualizer

import android.app.Application
import com.raffascript.sortvisualizer.di.AppModule
import com.raffascript.sortvisualizer.di.AppModuleImpl
import com.raffascript.sortvisualizer.di.UseCaseModule
import com.raffascript.sortvisualizer.di.UseCaseModuleImpl

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
        useCaseModule = UseCaseModuleImpl(appModule)
    }

    companion object {
        lateinit var appModule: AppModule
        lateinit var useCaseModule: UseCaseModule
    }
}