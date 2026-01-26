package com.pura.caspa.domain.repository

import com.pura.caspa.data.model.PartyData
import com.pura.caspa.data.model.Words
import com.pura.caspa.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface PuraCaspaRepository {

    //GetWordsToPlayUseCase
    suspend fun getWordstoPlay(): Resource<Words>
    //SaveUserNameUseCase
    fun saveUserName(name: String)
    fun getUserName(): String
    //CreateParty
    suspend fun createParty(customId: String): Resource<String>
    //JoinParty
    suspend fun joinParty(roomId: String): Resource<Unit>
    //Listen PartyData
    fun getPartyData(roomId: String): Flow<Resource<PartyData>>
}