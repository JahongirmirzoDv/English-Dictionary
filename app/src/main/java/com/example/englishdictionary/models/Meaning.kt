package com.example.englishdictionary.models

data class Meaning(
    val definitions: List<Definition>? =null,
    val partOfSpeech: String? = null
)