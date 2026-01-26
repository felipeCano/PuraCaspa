package com.pura.caspa.data.repository.dataSourceImpl

import com.google.firebase.firestore.FirebaseFirestore
import com.pura.caspa.data.model.PartyData
import com.pura.caspa.data.model.Words
import com.pura.caspa.data.repository.dataSource.PuraCaspaRemoteDataSource
import com.pura.caspa.data.util.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PuraCaspaRemoteDataSourceImpl(private val db: FirebaseFirestore) : PuraCaspaRemoteDataSource {

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
    ): Resource<String> {
        return try {
            val roomRef = db.collection("salas").document(customId)

            // We check if the room already exists
            val snapshot = roomRef.get().await()
            if (snapshot.exists()) {
                return Resource.Error("La sala '$customId' ya existe. Intenta con otro nombre.")
            }

            // If it does not exist, we save the room with that manual ID
            val roomWithId = partyData.copy(id = customId)
            roomRef.set(roomWithId).await()

            Resource.Success(customId)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al conectar con Firebase")
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

            db.runTransaction { transaction ->
                val snapshot = transaction.get(roomRef)

                if (!snapshot.exists()) {
                    throw Exception("La sala no existe")
                }

                val integrantes = snapshot.get("integrantes") as? MutableList<String> ?: mutableListOf()

                // Verificamos si ya está en la sala para no duplicarlo
                if (!integrantes.contains(userName)) {
                    integrantes.add(userName)
                    transaction.update(roomRef, "integrantes", integrantes)
                }
            }.await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al unirse a la sala")
        }
    }

    //Listen PartyData
    override fun getPartyData(roomId: String): Flow<Resource<PartyData>> = callbackFlow {
        val roomRef = db.collection("salas").document(roomId)

        // Listen in realTime our document
        val subscription = roomRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Resource.Error(error.message ?: "Error al escuchar sala"))
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
            Resource.Error(e.message ?: "Error al actualizar la partida")
        }
    }

}