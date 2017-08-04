package com.franctan.lonelyplanetcurrencyguide.main_activity.injection


import com.franctan.lonelyplanetcurrencyguide.main_activity.ChooseCurrencyDialogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityFragmentModule {
    @ContributesAndroidInjector
    abstract fun contributesChooseDialogFragment(): ChooseCurrencyDialogFragment

}