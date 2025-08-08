package com.kdbrian.sage.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map


val Context.appDataStore by preferencesDataStore(name = "app_data")


class AppDataStore(
    private val context: Context
) {

    private val _isFirstTime = booleanPreferencesKey(AppDatastoreKeys.firstTime)
    suspend fun updateFirstTime(firstTime: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[_isFirstTime] = firstTime
        }
    }

    val firstTime = context.appDataStore.data.map { preferences ->
        preferences[_isFirstTime] ?: true
    }


    object AppDatastoreKeys {
        const val firstTime = "isFirstTime"
    }

}