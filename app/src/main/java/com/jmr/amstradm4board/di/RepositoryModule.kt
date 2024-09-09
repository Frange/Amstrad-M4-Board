package com.jmr.amstradm4board.di

import com.jmr.amstradm4board.data.service.AmstradApiService
import com.jmr.amstradm4board.data.repository.AmstradRepository
import com.jmr.amstradm4board.data.repository.AmstradRepositoryImpl
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
    fun provideAmstradRepository(
//        gson: Gson,
        service: AmstradApiService
    ): AmstradRepository = AmstradRepositoryImpl(service)

}