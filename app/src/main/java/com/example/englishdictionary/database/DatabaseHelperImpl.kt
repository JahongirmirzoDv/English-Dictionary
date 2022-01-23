package com.example.englishdictionary.database

import com.example.englishdictionary.database.models.History
import kotlinx.coroutines.flow.Flow

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {
    override suspend fun getAllHistory(): Flow<List<History>> =
        appDatabase.wordDao().getAllHistory()

    override suspend fun getAllSaved(): Flow<List<History>> = appDatabase.wordDao().getAllSaved()

    override suspend fun insertAll(history: History) = appDatabase.wordDao().insertHistory(history)

    override suspend fun findHistory(word: String) = appDatabase.wordDao().findHistory(word)

    override suspend fun updateHistory(history: History) = appDatabase.wordDao().updatHistory(history)

}