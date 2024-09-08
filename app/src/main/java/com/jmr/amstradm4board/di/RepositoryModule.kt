package com.jmr.amstradm4board.di

import android.app.Application
import com.google.gson.Gson
import com.jmr.amstradm4board.XferApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providePoiRepository(
        gson: Gson,
        service: XferApi
    ): AmstradRepository = AmstradRepositoryImpl(gson, service)

}