package com.pura.caspa.data.repository.dataSource

import com.pura.caspa.data.model.Words
import kotlinx.coroutines.flow.Flow

interface PuraCaspaRemoteDataSource {
    fun fetchWords(): Flow<Words>
}