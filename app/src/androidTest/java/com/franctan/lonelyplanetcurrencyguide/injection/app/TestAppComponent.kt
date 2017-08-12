package com.franctan.lonelyplanetcurrencyguide.injection.app

import android.app.Application
import com.franctan.lonelyplanetcurrencyguide.TestApp
import com.franctan.lonelyplanetcurrencyguide.main_activity.MainActivityTest
import com.franctan.lonelyplanetcurrencyguide.main_activity.injection.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        MainActivityModule::class,
        TestViewModelModule::class
))
interface TestAppComponent : AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: Application): Builder
        fun build(): TestAppComponent
    }

    fun inject(app: TestApp)
    fun inject(testActivity: MainActivityTest)

}