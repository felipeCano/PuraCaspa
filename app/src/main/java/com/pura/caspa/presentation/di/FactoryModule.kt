package com.pura.caspa.presentation.di

import android.app.Application
import com.pura.caspa.domain.usecase.GetWordsToPlayUseCase
import com.pura.caspa.presentation.viewmodel.PuraCaspaViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {

    @Singleton
    @Provides
    fun providePuraCaspaViewModelFactory(
        application: Application,
        getWordsToPlayUseCase: GetWordsToPlayUseCase
    ): PuraCaspaViewModelFactory {
        return PuraCaspaViewModelFactory(
            application,
            getWordsToPlayUseCase
        )
    }
}