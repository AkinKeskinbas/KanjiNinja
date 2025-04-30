package com.keak.kanjininja.preference

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.keak.kanjininja.preference.DataStoreKey.NAME
import com.keak.kanjininja.preference.DataStoreKey.PREVIOUS_SEARCH_LIST
import com.keak.kanjininja.preference.DataStoreKey.TOKEN

class DataStoreManager(context: Context) {

    private val Context.dataStore by preferencesDataStore("app_preferences")
    private val dataStore = context.dataStore

    // -- Token & Name Setters/Getters --
    suspend fun setToken(value: String) {
        putString(TOKEN, value)
    }
    val getToken: Flow<String?> = getString(TOKEN)

    suspend fun setName(value: String) {
        putString(NAME, value)
    }
    val getName: Flow<String?> = getString(NAME)

    // -- Previous Search List Operations --
    suspend fun addToPreviousSearchList(value: String, maxItems: Int = 10) {
        saveListToPreferences(PREVIOUS_SEARCH_LIST, value, maxItems)
    }

    suspend fun removeFromPreviousSearchList(value: String) {
        removeFromList(PREVIOUS_SEARCH_LIST, value)
    }

    suspend fun clearAllPreviousSearchList() {
        clearList(PREVIOUS_SEARCH_LIST)
    }

    val getPreviousSearchList: Flow<List<String>> = getList(PREVIOUS_SEARCH_LIST)

    // -- Private Helpers --
    private suspend fun putString(key: String, value: String) = dataStore.edit { preferences ->
        preferences[stringPreferencesKey(key)] = value
    }

    private fun getString(key: String) = dataStore.data.map { preferences ->
        preferences[stringPreferencesKey(key)]
    }

    private suspend fun saveListToPreferences(key: String, value: String, maxItems: Int) {
        dataStore.edit { preferences ->
            val currentList = stringListFromPreferences(key, preferences)
            val updatedList = currentList.toMutableList().apply {
                remove(value) // varsa çıkar
                add(0, value) // başa ekle
                if (size > maxItems) removeAt(size - 1) // maxItems'ı aşarsa sil
            }
            saveListToPreferences(key, updatedList, preferences)
        }
    }

    private suspend fun removeFromList(key: String, value: String) {
        dataStore.edit { preferences ->
            val currentList = stringListFromPreferences(key, preferences)
            val updatedList = currentList.filterNot { it == value }
            saveListToPreferences(key, updatedList, preferences)
        }
    }

    private suspend fun clearList(key: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = ""
        }
    }

    private fun getList(key: String): Flow<List<String>> = dataStore.data.map { preferences ->
        stringListFromPreferences(key, preferences)
    }

    private fun stringListFromPreferences(key: String, preferences: Preferences): List<String> {
        val currentString = preferences[stringPreferencesKey(key)] ?: ""
        return currentString.split(",").filter { it.isNotEmpty() }
    }

    private suspend fun saveListToPreferences(key: String, list: List<String>, preferences: MutablePreferences) {
        preferences[stringPreferencesKey(key)] = list.joinToString(",")
    }

    // -- Boolean Setters/Getters --
    private suspend fun putBoolean(key: String, value: Boolean) = dataStore.edit { preferences ->
        preferences[booleanPreferencesKey(key)] = value
    }

    private fun getBoolean(key: String) = dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(key)]
    }
}
