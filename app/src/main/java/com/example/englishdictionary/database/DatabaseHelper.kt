package com.example.englishdictionary.database

import com.example.englishdictionary.database.models.History
import kotlinx.coroutines.flow.Flow

interface DatabaseHelper {

    suspend fun getAllHistory(): Flow<List<History>>

    suspend fun getAllSaved(): Flow<List<History>>

    suspend fun insertAll(history: History)

    suspend fun findHistory(word:String)

    suspend fun updateHistory(history: History)

}