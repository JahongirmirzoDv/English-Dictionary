package com.example.englishdictionary.retrofit

import com.example.englishdictionary.models.WordData
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("{word}")
    suspend fun getUsers(@Path("word") word:String): List<WordData>

    @GET("{word}")
    suspend fun getWord(@Path("word") word:String): WordData

}