package com.pura.caspa.data.repository

import com.pura.caspa.data.local.dataSource.UserPreferencesManager
import com.pura.caspa.data.model.PartyData
import com.pura.caspa.data.model.Player
import com.pura.caspa.data.model.Words
import com.pura.caspa.data.remote.dataSource.InstallationIdProvider
import com.pura.caspa.data.repository.dataSource.PuraCaspaRemoteDataSource
import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.repository.PuraCaspaRepository
import kotlinx.coroutines.flow.Flow

class PuraCaspaRepositoryImpl(
    private val puraCaspaRemoteDataSource: PuraCaspaRemoteDataSource,
    private val userPreferencesManager: UserPreferencesManager,
    private val installationIdProvider: InstallationIdProvider
) : PuraCaspaRepository {

    override suspend fun getWordstoPlay(): Resource<Words> {
        return try {
            val data = puraCaspaRemoteDataSource.fetchWords()
            Resource.Success(data)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error desconocido")
        }
    }

    //SaveUserNameUseCase
    override fun saveUserName(name: String) = userPreferencesManager.saveName(name)
    override fun getUserName(): String = userPreferencesManager.getName()

    //CreatePartyUsesCases
    override suspend fun createParty(customId: String): Resource<Unit> {
        val userName = userPreferencesManager.getName() // El host es el usuario actual
        val installationId = getInstallationId()
        val newRoom = PartyData(
            host_id = installationId,
            integrantes = listOf(Player(id = installationId, name = userName)),
            stateParty = "waiting"
        )
        return puraCaspaRemoteDataSource.createParty(customId, newRoom)
    }

    //JoinParty
    override suspend fun joinParty(roomId: String): Resource<Unit> {
        val userName = userPreferencesManager.getName() // Recuperamos el nombre guardado
        return puraCaspaRemoteDataSource.joinParty(roomId, userName)
    }

    //Listen PartyData
    override fun getPartyData(roomId: String): Flow<Resource<PartyData>> {
        //Repository delegates the real-time listening to the RemoteDataSource
        return puraCaspaRemoteDataSource.getPartyData(roomId)
    }

    //Star Game
    override suspend fun updatePartyStart(
        roomId: String,
        word: String,
        impostor: String,
        status: String,
        newUsedWordsList: List<String>
    ): Resource<Unit> {
        return puraCaspaRemoteDataSource.updatePartyStart(
            roomId,
            word,
            impostor,
            status,
            newUsedWordsList
        )
    }

    override suspend fun getInstallationId(): String {
        return installationIdProvider.getInstallationId()
    }
}