package com.franctan.lonelyplanetcurrencyguide

import android.app.Activity
import android.app.Application
import android.util.Log
import com.franctan.lonelyplanetcurrencyguide.injection.app.DaggerAppComponent
import com.franctan.lonelyplanetcurrencyguide.networking.NetworkingModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

//@OpenClassOnDebug
open class LPApp : Application(), HasActivityInjector {
    val TAG: String = LPApp::class.toString()

    @Inject protected lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        injectDagger()

        Log.d(TAG, "onCreate : App Starting up")
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return dispatchingActivityInjector
    }

    open fun injectDagger() {
        DaggerAppComponent
                .builder()
                .application(this)
                .networkingModule(NetworkingModule(CURRENCY_CONVERSION_URL))
                .build()
                .inject(this)
    }
}
