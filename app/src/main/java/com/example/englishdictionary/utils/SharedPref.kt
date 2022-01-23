package com.example.englishdictionary.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPref {
    lateinit var sharedPreferences: SharedPreferences

    fun getInstanceDis(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            "" +
                    "", MODE_PRIVATE
        )
    }

    var user: String?
        get() = sharedPreferences.getString("user", null)
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putString("user", value)
            }
        }
    var word: String?
        get() = sharedPreferences.getString("word", null)
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putString("word", value)
            }
        }
    var origin: String?
        get() = sharedPreferences.getString("origin", null)
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putString("origin", value)
            }
        }
    var audio: String?
        get() = sharedPreferences.getString("audio", null)
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putString("audio", value)
            }
        }
}