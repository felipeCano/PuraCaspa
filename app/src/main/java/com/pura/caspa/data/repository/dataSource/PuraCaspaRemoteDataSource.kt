package com.pura.caspa.data.repository.dataSource

import com.pura.caspa.data.model.PartyData
import com.pura.caspa.data.model.Words
import com.pura.caspa.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface PuraCaspaRemoteDataSource {
    suspend fun fetchWords(): Words

    //PartyData
    suspend fun createParty(customId: String, partyData: PartyData): Resource<Unit>

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

    //Update PartyState
    suspend fun updateToVotingStatus(roomId: String): Resource<Unit>

    //Voting
    suspend fun voteForPlayer(roomId: String, playerVotedId: String, voterId: String): Resource<Unit>

    //Reveal Imposter
    suspend fun revealImpostor(roomId: String, reveal: Boolean): Resource<Unit>

    //ResetVoting
    suspend fun resetPlayersVotes(roomId: String): Resource<Unit>
}