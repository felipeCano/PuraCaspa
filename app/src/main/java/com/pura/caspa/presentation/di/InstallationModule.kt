package com.pura.caspa.presentation.di

import android.content.Context
import com.pura.caspa.data.remote.dataSource.InstallationIdProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class InstallationModule {

    @Singleton
    @Provides
    fun provideInstallationIdProvider(@ApplicationContext context: Context): InstallationIdProvider {
        return InstallationIdProvider(context)
    }
}