package com.pura.caspa.data.repository.dataSourceImpl

import com.google.firebase.firestore.FirebaseFirestore
import com.pura.caspa.data.model.APIResponse
import com.pura.caspa.data.repository.dataSource.PuraCaspaRemoteDataSource
import kotlinx.coroutines.tasks.await

class PuraCaspaRemoteDataSourceImpl(private val db: FirebaseFirestore) : PuraCaspaRemoteDataSource {
    override suspend fun fetchWords(): List<APIResponse> {
        val snapshot = db.collection("words").get().await()
        return snapshot.toObjects(APIResponse::class.java)
    }

}