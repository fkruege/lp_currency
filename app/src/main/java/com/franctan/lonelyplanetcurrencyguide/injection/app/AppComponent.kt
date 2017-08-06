package com.franctan.lonelyplanetcurrencyguide.injection.app

import android.app.Application
import com.franctan.lonelyplanetcurrencyguide.LPApp
import com.franctan.lonelyplanetcurrencyguide.main_activity.injection.MainActivityModule
import com.franctan.lonelyplanetcurrencyguide.networking.NetworkingModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class
        , AppModule::class
        , NetworkingModule::class
        , MainActivityModule::class
))
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: Application): Builder
        fun networkingModule(networkingModule: NetworkingModule): Builder
        fun build(): AppComponent
    }

    fun app(): Application
    fun inject(app: LPApp)
}