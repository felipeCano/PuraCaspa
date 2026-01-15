package com.pura.caspa.domain.repository

import com.pura.caspa.data.model.Words
import com.pura.caspa.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface PuraCaspaRepository {

    fun getWordstoPlay(): Flow<Resource<Words>>
}