package com.example.englishdictionary.database

import android.content.Context
import androidx.room.Room
import com.example.englishdictionary.utils.Converters
import retrofit2.converter.gson.GsonConverterFactory

object DatabaseBuilder {

    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (INSTANCE == null) {
            synchronized(AppDatabase::class) {
                INSTANCE = buildRoomDB(context)
            }
        }
        return INSTANCE!!
    }

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "data"
        )
            .build()
}