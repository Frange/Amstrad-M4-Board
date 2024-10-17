package com.jmr.amstradm4board.di

import android.util.Log
import com.jmr.amstradm4board.data.repository.AmstradSharedPreference
import com.jmr.amstradm4board.data.service.AmstradApiService
import com.jmr.amstradm4board.ui.Utils.logs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        sharedPreference: AmstradSharedPreference,
        okHttpClient: OkHttpClient
    ): Retrofit {
        val baseUrl = "http://${sharedPreference.getLastIpAddress()}/"

        logs("BaseUrl: $baseUrl")

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideXferApi(retrofit: Retrofit): AmstradApiService {
        return retrofit.create(AmstradApiService::class.java)
    }

}
