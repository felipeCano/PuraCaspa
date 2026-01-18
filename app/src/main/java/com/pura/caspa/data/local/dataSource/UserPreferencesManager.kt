package com.pura.caspa.data.local.dataSource

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveName(name: String) {
        sharedPreferences.edit().putString("user_name", name).apply()
    }

    fun getName(): String {
        return sharedPreferences.getString("user_name", "") ?: ""
    }
}
