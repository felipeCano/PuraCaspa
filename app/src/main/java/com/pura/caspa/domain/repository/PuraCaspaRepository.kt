package com.pura.caspa.domain.repository

import com.pura.caspa.data.model.Words
import com.pura.caspa.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface PuraCaspaRepository {

    //GetWordsToPlayUseCase
    fun getWordstoPlay(): Flow<Resource<Words>>
    //SaveUserNameUseCase
    fun saveUserName(name: String)
    fun getUserName(): String
    //CreateParty
    suspend fun createParty(customId: String): Resource<String>
}