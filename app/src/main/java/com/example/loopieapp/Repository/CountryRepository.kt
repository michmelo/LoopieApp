package com.example.loopieapp.Repository

import com.example.loopieapp.Data.RestCountriesApiService
import com.example.loopieapp.Data.RestCountriesClient
import com.example.loopieapp.Data.models.Country
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.concurrent.Volatile

class CountryRepository(
    private val apiService: RestCountriesApiService = RestCountriesClient.instance
) {
    //Cache para guardar la lista de paises en memoria, la anotación
    //Volatile asegura que se pueda usar entre diferentes hilos
    @Volatile
    private var countriesCache: List<Country>? = null

    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    // ✅ PASO 3: Exponemos un StateFlow inmutable para que la UI y los ViewModels lo observen.
    val countries: StateFlow<List<Country>> = _countries.asStateFlow()

    suspend fun preloadCountries() {
        if (_countries.value.isEmpty()) {
            try {
                val response = apiService.getAllCountries()
                if (response.isSuccessful) {
                    // Ordenamos la lista y la emitimos al StateFlow.
                    _countries.value = response.body()?.sortedBy { it.name.common } ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getAllCountries(): List<Country> {
        //Si caché ya tiene datos la devuelve instantáneamente
        val cached = countriesCache

        //Si caché está vacía, llama a la API
        return try {
            val response = apiService.getAllCountries()
            if (response.isSuccessful) {
                val countries = response.body() ?: emptyList()
                // Guarda la lista ordenada en la caché para futuras llamadas.
                countries.sortedBy { it.name.common }.also { sortedList ->
                    countriesCache = sortedList
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getCountryDetails(countryName: String): Country? {
        return try {
            val response = apiService.getCountryDetails(countryName)
            if (response.isSuccessful) {
                // La API devuelve una lista, tomamos el primer resultado si no está vacía.
                response.body()?.firstOrNull()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}