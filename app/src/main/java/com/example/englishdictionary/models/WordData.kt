package com.example.englishdictionary.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverters
import com.example.englishdictionary.utils.Converters
import java.io.Serializable

@Entity
@TypeConverters(Converters::class)
data class WordData(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val meanings: List<Meaning>? = null,
    val origin: String? = null,
    val phonetic: String? = null,
    val phonetics: List<Phonetic>? = null,
    val word: String? = null
):Serializable