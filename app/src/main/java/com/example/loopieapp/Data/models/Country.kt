package com.example.loopieapp.Data.models

data class Country(
    val name: CountryName,
    val flags: CountryFlags
)

data class CountryName(
    val common: String,
    val official: String
)

data class CountryFlags(
    val png: String, //imagen png
    val svg: String, //imagen svg
    val alt: String? //texto alternativo
)