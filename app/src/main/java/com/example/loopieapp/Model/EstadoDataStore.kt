package com.example.loopieapp.Model

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "preferences-usuario")

class EstadoDataStore (private val context : Context) {
    private val ESTADO_ACTIVADO = booleanPreferencesKey("modo-activado") //determina el estado por defecto

    suspend fun guardarEstado (valor : Boolean) { //guarda el valor incluso despues de haber cerrado la app
        context.dataStore.edit { preferencias ->
            preferencias[ESTADO_ACTIVADO] = valor
        }
    }

    fun obtenerEstado() : Flow<Boolean?> { //lee el valor guardado
        return context.dataStore.data.map { preferencias ->
            preferencias[ESTADO_ACTIVADO]
        }
    }
}