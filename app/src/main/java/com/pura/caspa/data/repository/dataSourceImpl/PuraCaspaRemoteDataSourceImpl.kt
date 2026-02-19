package com.pura.caspa.data.repository.dataSourceImpl

import androidx.compose.ui.res.stringResource
import com.google.firebase.firestore.FirebaseFirestore
import com.pura.caspa.R
import com.pura.caspa.data.model.PartyData
import com.pura.caspa.data.model.Player
import com.pura.caspa.data.model.Words
import com.pura.caspa.data.remote.dataSource.InstallationIdProvider
import com.pura.caspa.data.repository.dataSource.PuraCaspaRemoteDataSource
import com.pura.caspa.data.util.PartyError
import com.pura.caspa.data.util.Resource
import com.pura.caspa.data.util.UiText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PuraCaspaRemoteDataSourceImpl(
    private val db: FirebaseFirestore,
    private val installationIdProvider: InstallationIdProvider
) : PuraCaspaRemoteDataSource {

    override suspend fun fetchWords(): Words {
        return try {
            val snapshot = db.collection("Words").document("Words").get().await()
            snapshot.toObject(Words::class.java) ?: Words()
        } catch (e: Exception) {
            Words()
        }
    }

    // Create Party Document
    override suspend fun createParty(
        customId: String,
        partyData: PartyData
    ): Resource<Unit> {
        return try {
            val roomRef = db.collection("salas").document(customId)

            // We check if the room already exists
            val snapshot = roomRef.get().await()
            if (snapshot.exists()) {
                return Resource.Error(PartyError.ALREADY_EXISTS)
            }

            // If it does not exist, we save the room with that manual ID
            val roomWithId = partyData.copy(id = customId)
            roomRef.set(roomWithId).await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            val errorEnum = if (e.localizedMessage == null) PartyError.FIREBASE_ERROR else null
            val dynamicMsg = e.localizedMessage?.let { UiText.DynamicString(it) }
            Resource.Error(errorEnum, dynamicMsg)
        }
    }

    //JoinParty
    override suspend fun joinParty(
        roomId: String,
        userName: String
    ): Resource<Unit> {
        return try {
            val db = FirebaseFirestore.getInstance()
            val roomRef = db.collection("salas").document(roomId)
            val installationId = installationIdProvider.getInstallationId()

            db.runTransaction { transaction ->
                val snapshot = transaction.get(roomRef)

                if (!snapshot.exists()) {
                    throw Exception("La sala no existe")
                }

                val partyData = snapshot.toObject(PartyData::class.java)
                val integrantes = partyData?.integrantes?.toMutableList() ?: mutableListOf()

                val existingPlayer = integrantes.find { it.id == installationId }
                if (existingPlayer == null) {
                    // Si el dispositivo NO está en la lista, lo agregamos
                    val newPlayer = Player(id = installationId, name = userName)
                    integrantes.add(newPlayer)
                    transaction.update(roomRef, "integrantes", integrantes)
                } else {
                    // Si el dispositivo YA ESTÁ, pero cambió su nombre, lo actualizamos (opcional)
                    if (existingPlayer.name != userName) {
                        val index = integrantes.indexOf(existingPlayer)
                        integrantes[index] = existingPlayer.copy(name = userName)
                        transaction.update(roomRef, "integrantes", integrantes)
                    }
                }
            }.await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            val errorEnum = if (e.localizedMessage == null) PartyError.FIREBASE_ERROR else null
            val dynamicMsg = e.localizedMessage?.let { UiText.DynamicString(it) }
            Resource.Error(errorEnum, dynamicMsg)
        }
    }

    //Listen PartyData
    override fun getPartyData(roomId: String): Flow<Resource<PartyData>> = callbackFlow {
        val roomRef = db.collection("salas").document(roomId)

        // Listen in realTime our document
        val subscription = roomRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                val errorEnum =
                    if (error.localizedMessage == null) PartyError.ERROR_TO_LISTEN_PARTY else null
                val dynamicMsg = error.localizedMessage?.let { UiText.DynamicString(it) }
                trySend(Resource.Error(errorEnum, dynamicMsg))
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val room = snapshot.toObject(PartyData::class.java)
                if (room != null) {
                    trySend(Resource.Success(room)) //Sent partyData updated
                }
            }
        }

        //Important: We close the listener when the Flow is no longer used
        awaitClose { subscription.remove() }
    }

    //Star Game
    override suspend fun updatePartyStart(
        roomId: String,
        word: String,
        impostor: String,
        status: String,
        newUsedWordsList: List<String>
    ): Resource<Unit> {
        return try {
            //We update the specific fields in the room document
            db.collection("salas").document(roomId)
                .update(
                    "palabra_actual", word,
                    "amoung_us", impostor,
                    "stateParty", status,
                    "usedWords", newUsedWordsList
                ).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            val errorEnum = if (e.localizedMessage == null) PartyError.FIREBASE_ERROR else null
            val dynamicMsg = e.localizedMessage?.let { UiText.DynamicString(it) }
            Resource.Error(errorEnum, dynamicMsg)
        }
    }

    //Updated PartyState
    override suspend fun updateToVotingStatus(roomId: String): Resource<Unit> {
        return try {
            db.collection("salas").document(roomId)
                .update("stateParty", "voting") // Actualizamos el estado a votando
                .await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            //Resource.Error(e.message ?: "Error al cambiar a votación")
            val errorEnum = if (e.localizedMessage == null) PartyError.FIREBASE_ERROR else null
            val dynamicMsg = e.localizedMessage?.let { UiText.DynamicString(it) }
            Resource.Error(errorEnum, dynamicMsg)
        }
    }

    //Voting
    override suspend fun voteForPlayer(roomId: String, playerVotedId: String): Resource<Unit> {
        return try {
            val roomRef = db.collection("salas").document(roomId)

            db.runTransaction { transaction ->
                val snapshot = transaction.get(roomRef)
                val partyData = snapshot.toObject(PartyData::class.java)
                val integrantes = partyData?.integrantes?.toMutableList() ?: mutableListOf()
                val currentRoundVotes = partyData?.votos_en_esta_ronda ?: 0
                val playerToUpdate = integrantes.find { it.id == playerVotedId }

                if (playerToUpdate != null) {
                    val index = integrantes.indexOf(playerToUpdate)
                    integrantes[index] = playerToUpdate.copy(votes = playerToUpdate.votes + 1)
                    transaction.update(roomRef, "integrantes", integrantes)
                    transaction.update(roomRef, "votos_en_esta_ronda", currentRoundVotes + 1)
                }
            }.await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            val errorEnum = if (e.localizedMessage == null) PartyError.FIREBASE_ERROR else null
            val dynamicMsg = e.localizedMessage?.let { UiText.DynamicString(it) }
            Resource.Error(errorEnum, dynamicMsg)
        }
    }

    //Reveal Imposter
    override suspend fun revealImpostor(roomId: String, reveal: Boolean): Resource<Unit> {
        return try {
            db.collection("salas").document(roomId)
                .update("showImpostor", reveal)
                .await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            val errorEnum = if (e.localizedMessage == null) PartyError.FIREBASE_ERROR else null
            val dynamicMsg = e.localizedMessage?.let { UiText.DynamicString(it) }
            Resource.Error(errorEnum, dynamicMsg)
            //Resource.Error(e.message ?: "Error al revelar impostor")
        }
    }

    //ResetVotin
    override suspend fun resetPlayersVotes(roomId: String): Resource<Unit> {
        return try {
            db.collection("salas").document(roomId)
                .update("votos_en_esta_ronda", 0).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            val errorEnum = if (e.localizedMessage == null) PartyError.FIREBASE_ERROR else null
            val dynamicMsg = e.localizedMessage?.let { UiText.DynamicString(it) }
            Resource.Error(errorEnum, dynamicMsg)
            //Resource.Error(e.localizedMessage ?: "Error al reiniciar contador de ronda")
        }
    }
}