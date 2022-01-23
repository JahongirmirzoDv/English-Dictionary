package com.example.englishdictionary.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"

    fun getRetofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val apiService:ApiService = getRetofit().create(ApiService::class.java)
}