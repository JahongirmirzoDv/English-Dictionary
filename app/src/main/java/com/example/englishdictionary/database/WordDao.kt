package com.example.englishdictionary.database

import androidx.room.*
import com.example.englishdictionary.database.models.History
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistory(history: History)

    @Query("select * from history")
    fun getAllHistory(): Flow<List<History>>

    @Query("select * from history where saved=1")
    fun getAllSaved(): Flow<List<History>>

    @Update
    suspend fun updatHistory(history: History)

    @Query("delete from history where word=:word")
    suspend fun findHistory(word:String)
}