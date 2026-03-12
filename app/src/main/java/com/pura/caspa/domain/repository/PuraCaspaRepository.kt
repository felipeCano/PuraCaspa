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
    suspend fun createParty(customId: String): Resource<Unit>

    //JoinParty
    suspend fun joinParty(roomId: String): Resource<Unit>

    //Listen PartyData
    fun getPartyData(roomId: String): Flow<Resource<PartyData>>

    //StarGame
    suspend fun updatePartyStart(
        roomId: String, word: String, impostor: String, status: String,
        newUsedWordsList: List<String>
    ): Resource<Unit>

    //InstallationId from Firebase
    suspend fun getInstallationId(): String

    //UpdatePartyState
    suspend fun updateToVotingStatus(roomId: String): Resource<Unit>

    //Voting
    suspend fun voteForPlayer(roomId: String, playerVotedId: String, voterId: String): Resource<Unit>

    //Reveal Imposter
    suspend fun revealImpostor(roomId: String, reveal: Boolean): Resource<Unit>

    //ResetVoting
    suspend fun resetPlayersVotes(roomId: String): Resource<Unit>
}