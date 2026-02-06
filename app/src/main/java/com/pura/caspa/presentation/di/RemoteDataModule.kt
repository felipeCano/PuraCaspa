package com.pura.caspa.presentation.di

import com.google.firebase.firestore.FirebaseFirestore
import com.pura.caspa.data.remote.dataSource.InstallationIdProvider
import com.pura.caspa.data.repository.dataSource.PuraCaspaRemoteDataSource
import com.pura.caspa.data.repository.dataSourceImpl.PuraCaspaRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteDataModule {

    @Singleton
    @Provides
    fun providePuraCaspaRemoteDataSource(
        firebaseFirestore: FirebaseFirestore,
        installationIdProvider: InstallationIdProvider
    ): PuraCaspaRemoteDataSource {
        return PuraCaspaRemoteDataSourceImpl(firebaseFirestore, installationIdProvider)
    }
}