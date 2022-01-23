package com.example.englishdictionary.database

import androidx.room.Database
import androidx.room.ProvidedTypeConverter
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.englishdictionary.database.models.History
import com.example.englishdictionary.models.WordData
import com.example.englishdictionary.utils.Converters

@Database(entities = [WordData::class, History::class], version = 3)
@ProvidedTypeConverter
abstract class AppDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

}