package com.example.englishdictionary.database.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class History(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var word: String? = null,
    var origin: String? = null,
    var saved: Boolean? = null,
    var audio:String? = null

)
