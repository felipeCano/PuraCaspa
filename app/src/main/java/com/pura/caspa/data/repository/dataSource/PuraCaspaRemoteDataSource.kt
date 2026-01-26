package com.pura.caspa.data.repository.dataSource

import com.pura.caspa.data.model.PartyData
import com.pura.caspa.data.model.Words
import com.pura.caspa.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface PuraCaspaRemoteDataSource {
    suspend fun fetchWords(): Words

    //PartyData
    suspend fun createParty(customId: String, partyData: PartyData): Resource<String>

    //JoinParty
    suspend fun joinParty(roomId: String, userName: String): Resource<Unit>

    //Listen PartyData
    fun getPartyData(roomId: String): Flow<Resource<PartyData>>

    //Star game
    suspend fun updatePartyStart(
        roomId: String,
        word: String,
        impostor: String,
        status: String,
        newUsedWordsList: List<String>
    ): Resource<Unit>
}