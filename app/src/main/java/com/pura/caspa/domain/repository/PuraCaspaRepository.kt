package com.pura.caspa.domain.repository

import com.pura.caspa.data.model.APIResponse
import com.pura.caspa.data.util.Resource

interface PuraCaspaRepository {

    suspend fun getWordstoPlay(): Resource<APIResponse>
}