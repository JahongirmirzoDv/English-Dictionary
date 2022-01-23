package com.example.englishdictionary.utils

import androidx.room.TypeConverter
import com.example.englishdictionary.models.Meaning
import com.example.englishdictionary.models.Phonetic
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
class Converters {
    var gson = Gson()

    @TypeConverter
    fun foodItemToString(foodItems: List<Meaning>): String {
        return gson.toJson(foodItems)
    }

    @TypeConverter
    fun stringToFoodItem(data: String): List<Meaning> {
        val listType = object : TypeToken<List<Meaning>>() {
        }.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun pItemToString(pItems: List<Phonetic>): String {
        return gson.toJson(pItems)
    }

    @TypeConverter
    fun stringToP(data: String): List<Phonetic> {
        val listType = object : TypeToken<List<Phonetic>>() {
        }.type
        return gson.fromJson(data, listType)
    }
}