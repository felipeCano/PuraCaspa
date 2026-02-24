package com.pura.caspa.presentation.di

import android.content.Context
import com.pura.caspa.data.remote.ads.GoogleAdProviderImpl
import com.pura.caspa.domain.repository.AdProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAdProvider(@ApplicationContext context: Context): AdProvider {
        return GoogleAdProviderImpl(context)
    }


}