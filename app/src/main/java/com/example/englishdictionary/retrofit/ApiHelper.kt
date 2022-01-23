package com.example.englishdictionary.retrofit

import com.example.englishdictionary.models.WordData

interface ApiHelper {
    suspend fun getWord(word:String):List<WordData>

    suspend fun getOnwWord(word: String):WordData
}