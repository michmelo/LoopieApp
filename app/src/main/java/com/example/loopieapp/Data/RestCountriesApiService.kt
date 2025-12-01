package com.example.loopieapp.Data

import com.example.loopieapp.Data.models.Country
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RestCountriesApiService {

    @GET("all?fields=name,flags")
    suspend fun getAllCountries(): Response<List<Country>>

    @GET("name/{countryName}")
    suspend fun getCountryDetails(
        @Path("countryName") countryName: String
    ): Response<List<Country>>
}