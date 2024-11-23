package com.romoreno.compraplus.ui.main

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DatastorePreferences @Inject constructor(@ApplicationContext val context: Context) {

    companion object {
        val DARK_MODE = "key_dark_mode_"
    }


    suspend fun setNightModePreference(nightMode: Boolean, userId: String) {
        context.dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(DARK_MODE+userId)] = nightMode
        }
    }

    suspend fun getNightModePreference(userId: String): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[booleanPreferencesKey(DARK_MODE+userId)] ?: true
    }

}