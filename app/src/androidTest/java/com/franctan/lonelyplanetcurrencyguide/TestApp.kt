package com.franctan.lonelyplanetcurrencyguide

import com.franctan.lonelyplanetcurrencyguide.injection.app.DaggerTestAppComponent
import com.franctan.lonelyplanetcurrencyguide.injection.app.TestAppComponent

class TestApp : LPApp() {

    lateinit var daggerComponent: TestAppComponent

    override fun onCreate() {
        super.onCreate()
    }

    override fun injectDagger() {
        daggerComponent = DaggerTestAppComponent.builder().application(this).build()
        daggerComponent.inject(this)
    }

    fun daggerComponent(): TestAppComponent {
        return daggerComponent
    }


}