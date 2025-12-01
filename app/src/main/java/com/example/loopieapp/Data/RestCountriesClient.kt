package com.example.loopieapp.Data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestCountriesClient {
    // La URL base específica para la API de REST Countries
    private const val BASE_URL = "https://restcountries.com/v3.1/"

    // Reutilizamos lógica
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Reutilizamos lógica
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Creamos la instancia 'lazy' de Retrofit para esta API específica
    val instance: RestCountriesApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(RestCountriesApiService::class.java)
    }
}