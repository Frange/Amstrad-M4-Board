package com.jmr.amstradm4board.di

import android.app.Application
import com.jmr.amstradm4board.data.service.AmstradApiService
import com.jmr.amstradm4board.data.repository.AmstradRepository
import com.jmr.amstradm4board.data.repository.AmstradRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAmstradRepository(
        context: Application,
        service: AmstradApiService,
        client: OkHttpClient
    ): AmstradRepository = AmstradRepositoryImpl(context, service, client)

}