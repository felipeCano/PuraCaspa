package com.pura.caspa.data.repository.dataSource

import com.pura.caspa.data.model.PartyData
import com.pura.caspa.data.model.Words
import com.pura.caspa.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface PuraCaspaRemoteDataSource {
    fun fetchWords(): Flow<Words>
    //PartyData
    suspend fun createParty(customId: String, partyData: PartyData): Resource<String>
    //JoinParty
    suspend fun joinParty(roomId: String, userName: String): Resource<Unit>
}