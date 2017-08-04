package com.franctan.lonelyplanetcurrencyguide.main_activity.injection

import com.franctan.lonelyplanetcurrencyguide.main_activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = arrayOf(MainActivityFragmentModule::class))
    abstract fun contributesMainActivity(): MainActivity

}