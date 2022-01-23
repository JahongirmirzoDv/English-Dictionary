package com.example.englishdictionary.retrofit

import com.example.englishdictionary.models.WordData

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getWord(word: String): List<WordData> = apiService.getUsers(word)

    override suspend fun getOnwWord(word: String): WordData = apiService.getWord(word)
}