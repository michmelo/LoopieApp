package com.example.loopieapp.Data

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.loopieapp.Model.Usuario
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para crear una instancia única de DataStore en toda la app.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class UserDataStore(private val context: Context) {

    // 1. GSON para convertir el objeto Usuario a String (JSON) y viceversa.
    private val gson = Gson()

    companion object {
        // 2. La "llave" bajo la cual guardaremos el string del usuario en DataStore.
        private val USER_KEY = stringPreferencesKey("active_user_json")
    }

    /**
     * Guarda el objeto Usuario completo en DataStore.
     * Convierte el objeto a un string JSON antes de guardarlo.
     * Es una función `suspend` porque la escritura es asíncrona.
     */
    suspend fun guardarUsuarioActivo(usuario: Usuario) {
        context.dataStore.edit { preferences ->
            val usuarioJson = gson.toJson(usuario) // Convierte el objeto a JSON
            preferences[USER_KEY] = usuarioJson
        }
    }

    /**
     * Devuelve un Flow que emite el objeto Usuario cada vez que cambia.
     * Si no hay ningún usuario guardado, emite `null`.
     * El `Flow` es ideal porque notifica a sus observadores (como el ViewModel) de cualquier cambio.
     */
    fun getUsuarioActivo(): Flow<Usuario?> {
        return context.dataStore.data.map { preferences ->
            val usuarioJson = preferences[USER_KEY]
            if (usuarioJson != null) {
                gson.fromJson(usuarioJson, Usuario::class.java) // Convierte el JSON de vuelta a objeto
            } else {
                null
            }
        }
    }

    /**
     * Limpia los datos del usuario guardado, útil para el "cerrar sesión".
     */
    suspend fun limpiarUsuarioActivo() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_KEY)
        }
    }
}
