package com.pura.caspa.data.repository

import com.pura.caspa.data.model.APIResponse
import com.pura.caspa.data.repository.dataSource.PuraCaspaRemoteDataSource
import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.repository.PuraCaspaRepository

class PuraCaspaRepositoryImpl(
    private val puraCaspaRemoteDataSource: PuraCaspaRemoteDataSource
):PuraCaspaRepository {

    override suspend fun getWordstoPlay(): Resource<List<APIResponse>> {
        return try {
            val data = puraCaspaRemoteDataSource.fetchWords()
            Resource.Success(data)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage)
        }
    }
}