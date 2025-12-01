package com.example.loopieapp.Data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    //URL BASE DE la api
    private const val BASE_URL = "http://192.168.1.83:8080/"

    //CONFIGURACIÓN DEL CLIENTE HTTP
    //Creamos un interceptor que registrará en el Logcat todas las peticiones y respuestas.
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Construimos el cliente OkHttp, añadiendo el interceptor.
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // --- INSTANCIA DE RETROFIT (SINGLETON) ---

    //Usamos 'lazy' para que la instancia de Retrofit se cree solo una vez, cuando se necesite por primera vez.
    val instance: ApiService by lazy {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

        //Crea y devuelve la implementación de tu interfaz ApiService
        retrofit.create(ApiService::class.java)
    }
}