package com.pura.caspa.data.repository.dataSourceImpl

import com.google.firebase.firestore.FirebaseFirestore
import com.pura.caspa.data.model.APIResponse
import com.pura.caspa.data.repository.dataSource.PuraCaspaRemoteDataSource
import kotlinx.coroutines.tasks.await

class PuraCaspaRemoteDataSourceImpl(private val db: FirebaseFirestore) : PuraCaspaRemoteDataSource {
    override suspend fun fetchWords(): APIResponse {
        //Target the collection "Words" and the document "Words"
        val snapshot = db.collection("Words")
            .document("Words")
            .get()
            .await()
        //Convert the document to a class APIResponse
        return snapshot.toObject(APIResponse::class.java) ?: APIResponse()
    }

}