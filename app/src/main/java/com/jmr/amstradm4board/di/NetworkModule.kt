package com.jmr.amstradm4board.di

import com.jmr.amstradm4board.XferApi
import com.jmr.amstradm4board.ui.XferRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.39/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideXferApi(retrofit: Retrofit): XferApi {
        return retrofit.create(XferApi::class.java)
    }

    @Provides
    @Singleton
    fun provideXferRepository(xferApi: XferApi): XferRepository {
        return XferRepository(xferApi)
    }
}