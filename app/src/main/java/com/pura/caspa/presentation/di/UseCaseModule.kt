package com.pura.caspa.presentation.di

import com.pura.caspa.domain.repository.PuraCaspaRepository
import com.pura.caspa.domain.usecase.GetWordsToPlayUseCase
import com.pura.caspa.domain.usecase.SaveUserNameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun providePuraCaspaGetWordUsesCases(
    puraCaspaRepository: PuraCaspaRepository
    ): GetWordsToPlayUseCase{
        return GetWordsToPlayUseCase(puraCaspaRepository)
    }

    @Singleton
    @Provides
    fun providePuraCaspaSaveNameUseCase(
        puraCaspaRepository: PuraCaspaRepository
    ): SaveUserNameUseCase {
        return SaveUserNameUseCase(puraCaspaRepository)
    }
}