package com.pura.caspa.data.repository.dataSource

import com.pura.caspa.data.model.APIResponse

interface PuraCaspaRemoteDataSource {
    suspend fun fetchWords(): List<APIResponse>
}