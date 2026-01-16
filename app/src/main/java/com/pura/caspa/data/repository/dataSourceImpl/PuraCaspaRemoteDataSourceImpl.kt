package com.pura.caspa.data.repository.dataSourceImpl

import com.google.firebase.firestore.FirebaseFirestore
import com.pura.caspa.data.model.Words
import com.pura.caspa.data.repository.dataSource.PuraCaspaRemoteDataSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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

}