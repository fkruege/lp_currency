package com.franctan.lonelyplanetcurrencyguide.injection.app

import android.app.Application
import android.content.Context
import com.franctan.lonelyplanetcurrencyguide.LPApp
import com.franctan.lonelyplanetcurrencyguide.injection.view_model.ViewModelModule
import dagger.Module
import dagger.Provides


@Module (includes = arrayOf(ViewModelModule::class))
class AppModule {

    @Provides fun provideContext(application: Application): Context {
        return application.applicationContext
    }

}