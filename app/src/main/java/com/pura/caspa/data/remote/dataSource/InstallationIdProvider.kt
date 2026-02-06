package com.pura.caspa.data.remote.dataSource

import android.content.Context
import com.google.firebase.installations.FirebaseInstallations
import java.util.UUID
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class InstallationIdProvider(private val context: Context) {
    suspend fun getInstallationId(): String {
        return try {
            FirebaseInstallations.getInstance().id.await()
        } catch (e: Exception) {
            UUID.randomUUID().toString()
        }
    }
}
