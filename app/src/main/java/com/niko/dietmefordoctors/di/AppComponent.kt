package com.niko.dietmefordoctors.di

import com.niko.dietmefordoctors.di.modules.AppModule
import com.niko.dietmefordoctors.di.modules.NavigateModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NavigateModule::class])
interface AppComponent {


}