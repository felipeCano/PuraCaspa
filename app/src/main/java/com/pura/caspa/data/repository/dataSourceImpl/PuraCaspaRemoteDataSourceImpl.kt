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

    override fun fetchWords(): Flow<Words> = callbackFlow {
        val docRef = db.collection("Words").document("Words")
        val subscription = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val data = snapshot.toObject(Words::class.java)
                data?.let { trySend(it) }
            }
        }
        awaitClose { subscription.remove() }
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

}