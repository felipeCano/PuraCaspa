package com.pura.caspa.presentation.di

import com.pura.caspa.data.repository.PuraCaspaRepositoryImpl
import com.pura.caspa.data.repository.dataSource.PuraCaspaRemoteDataSource
import com.pura.caspa.domain.repository.PuraCaspaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun providePuraCaspaRepository(puraCaspaRemoteDataSource: PuraCaspaRemoteDataSource
    ): PuraCaspaRepository{
        return PuraCaspaRepositoryImpl(puraCaspaRemoteDataSource)
    }
}