package com.franctan.lonelyplanetcurrencyguide.injection.app

import android.arch.lifecycle.ViewModelProvider
import com.franctan.lonelyplanetcurrencyguide.main_activity.MainViewModel
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.*
import javax.inject.Singleton


@Module
class TestViewModelModule {

    @Provides
    @Singleton
    fun provideViewModelFactory(): ViewModelProvider.Factory {
        val mock = mock(ViewModelProvider.Factory::class.java)
        return mock
    }


    @Provides
    @Singleton
    fun provideMainViewModel(): MainViewModel {
        val mock = mock(MainViewModel::class.java)
        return mock
    }
}