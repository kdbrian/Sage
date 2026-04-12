package com.kdbrian.sage.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kdbrian.sage.domain.model.UserProfile
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json


val Context.appDataStore by preferencesDataStore(name = "app_data")


class AppDataStore(
    private val context: Context,
    private val json: Json
) {

    //first time
    private val _isFirstTime = booleanPreferencesKey(Companion.firstTime)
    private val _userProfile = stringPreferencesKey(Companion.userProfile)
    suspend fun updateFirstTime(firstTime: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[_isFirstTime] = firstTime
        }
    }

    suspend fun updateUserProfile(userProfile: UserProfile) {
        context.appDataStore.edit { preferences ->
            preferences[_userProfile] = json.encodeToString(userProfile)
        }
    }

    val firstTime = context.appDataStore.data.map { preferences ->
        preferences[_isFirstTime] ?: true
    }

    val userProfile = context.appDataStore.data.map { preferences ->
        preferences[_userProfile]?.also {
            if (it.isNotEmpty())
                json.decodeFromString<UserProfile>(it)
        }
    }

    //fav topics
    private val _favouriteTopics = stringPreferencesKey(Companion.favouriteTopics)
    suspend fun updateFavouriteTopics(favouriteTopic: String) {
        context.appDataStore.edit { preferences ->
            //decode existing topics
            val decodeFromString =
                Json.decodeFromString<Set<String>>(preferences[_favouriteTopics] ?: "[]")

            //add all new
            val mutableSet = decodeFromString.toMutableSet()
            mutableSet.add(favouriteTopic)

            preferences[_favouriteTopics] = Json.encodeToString(mutableSet)
        }
    }

    val favouriteTopics = context.appDataStore.data.map { preferences ->
        val decodeFromString =
            Json.decodeFromString<Set<String>>(preferences[_favouriteTopics] ?: "[]")
        decodeFromString.toMutableSet()
    }

    companion object {
        private const val firstTime = "isFirstTime"
        private const val userProfile = "userProfile"
        private const val favouriteTopics = "favouriteTopics"
    }

}