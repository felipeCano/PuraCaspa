package com.pura.caspa.presentation.di

import com.pura.caspa.domain.repository.PuraCaspaRepository
import com.pura.caspa.domain.usecase.CreatePartyUseCase
import com.pura.caspa.domain.usecase.GetInstallationIdUseCase
import com.pura.caspa.domain.usecase.GetPartyDataUseCase
import com.pura.caspa.domain.usecase.GetUserNameUseCase
import com.pura.caspa.domain.usecase.GetWordsToPlayUseCase
import com.pura.caspa.domain.usecase.JoinPartyUseCase
import com.pura.caspa.domain.usecase.SaveUserNameUseCase
import com.pura.caspa.domain.usecase.StartGameUseCase
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

    @Singleton
    @Provides
    fun providePuraCaspaCreatePartyUseCase(
        puraCaspaRepository: PuraCaspaRepository
    ): CreatePartyUseCase {
        return CreatePartyUseCase(puraCaspaRepository)
    }

    @Singleton
    @Provides
    fun providePuraCaspaJoinPartyUseCase(
        puraCaspaRepository: PuraCaspaRepository
    ): JoinPartyUseCase {
        return JoinPartyUseCase(puraCaspaRepository)
    }

    @Singleton
    @Provides
    fun providePuraCaspaGetPartyDataUseCase(
        puraCaspaRepository: PuraCaspaRepository
    ): GetPartyDataUseCase {
        return GetPartyDataUseCase(puraCaspaRepository)
    }

    @Singleton
    @Provides
    fun provideGetUserNameUseCase(
        puraCaspaRepository: PuraCaspaRepository
    ): GetUserNameUseCase {
        return GetUserNameUseCase(puraCaspaRepository)
    }

    @Singleton
    @Provides
    fun provideStarGameUseCase(
        puraCaspaRepository: PuraCaspaRepository
    ): StartGameUseCase {
        return StartGameUseCase(puraCaspaRepository)
    }

    @Singleton
    @Provides
    fun provideGetInstallationIdUseCase(
        puraCaspaRepository: PuraCaspaRepository
    ): GetInstallationIdUseCase {
        return GetInstallationIdUseCase(puraCaspaRepository)
    }
}