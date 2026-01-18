package com.pura.caspa.data.repository

import com.pura.caspa.data.local.dataSource.UserPreferencesManager
import com.pura.caspa.data.model.Words
import com.pura.caspa.data.repository.dataSource.PuraCaspaRemoteDataSource
import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.repository.PuraCaspaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class PuraCaspaRepositoryImpl(
    private val puraCaspaRemoteDataSource: PuraCaspaRemoteDataSource,
    private val userPreferencesManager: UserPreferencesManager
) : PuraCaspaRepository {

    override fun getWordstoPlay(): Flow<Resource<Words>> {
        return puraCaspaRemoteDataSource.fetchWords()
            .map { data ->
                Resource.Success(data) as Resource<Words>
            }
            .onStart { emit(Resource.Loading()) }
            .catch { e -> emit(Resource.Error(e.message ?: "Error desconocido")) }
    }

    override fun saveUserName(name: String) = userPreferencesManager.saveName(name)
    override fun getUserName(): String = userPreferencesManager.getName()
}