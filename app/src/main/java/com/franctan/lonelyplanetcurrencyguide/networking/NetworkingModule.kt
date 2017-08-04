package com.franctan.lonelyplanetcurrencyguide.networking

import com.franctan.lonelyplanetcurrencyguide.LPApp
import com.franctan.lonelyplanetcurrencyguide.networking.adapters.CurrencyConversionRatesModelAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@Singleton
class NetworkingModule constructor(val baseUrl: String) {

    @Provides
    @Singleton
    fun provideOkHttpCache(application: LPApp): Cache {
        val cacheSize = 10L * 1024L * 1024L
        val cache = Cache(application.cacheDir, cacheSize)
        return cache
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(logger)
                .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi
                .Builder()
                .add(CurrencyConversionRatesModelAdapter())
                .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideCurrencyApi(retrofit: Retrofit): CurrencyApi {
        return retrofit.create(CurrencyApi::class.java)
    }

}
