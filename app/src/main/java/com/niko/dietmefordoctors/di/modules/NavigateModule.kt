package com.niko.dietmefordoctors.di.modules

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Module
class NavigateModule {

    private val cicerone: Cicerone<Router> = Cicerone.create()


    @Singleton
    @Provides
    fun provideRouter(): Router = cicerone.router

    @Singleton
    @Provides
    fun provideNavHolder(): NavigatorHolder = cicerone.navigatorHolder

}