package com.niko.dietmefordoctors

import android.app.Application
import com.niko.dietmefordoctors.di.AppComponent
import com.niko.dietmefordoctors.di.DaggerAppComponent
import com.niko.dietmefordoctors.di.modules.AppModule
import com.niko.dietmefordoctors.di.modules.NavigateModule


class App : Application() {

    companion object {
        lateinit var INSTANCE: AppComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .navigateModule(NavigateModule())
            .build()

    }

}