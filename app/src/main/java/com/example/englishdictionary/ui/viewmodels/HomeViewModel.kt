package com.example.englishdictionary.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishdictionary.database.AppDatabase
import com.example.englishdictionary.database.DatabaseHelper
import com.example.englishdictionary.database.DatabaseHelperImpl
import com.example.englishdictionary.database.models.History
import com.example.englishdictionary.models.WordData
import com.example.englishdictionary.retrofit.ApiHelper
import com.example.englishdictionary.utils.Resource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class HomeViewModel(private val apiHelper: ApiHelper, private val dbHelper: DatabaseHelper) :
    ViewModel() {

    private var words = MutableLiveData<Resource<List<WordData>>>()
    private var latesWords = MutableLiveData<List<History>>()
    private var savedWords = MutableLiveData<List<History>>()
    private var selectWord = MutableLiveData<Resource<WordData>>()


    fun getOneWord(word: String): MutableLiveData<Resource<WordData>> {
        viewModelScope.launch {
            coroutineScope {
                supervisorScope {
                    selectWord.postValue(Resource.loading(null))
                    try {
                        val wordFromApi = apiHelper.getOnwWord(word)
                        selectWord.postValue(Resource.success(wordFromApi))
                    } catch (e: Exception) {
                        selectWord.postValue(Resource.error(e.message.toString(), null))
                    }
                }
            }
        }
        return selectWord
    }

    fun getWords(word: String): MutableLiveData<Resource<List<WordData>>> {
        viewModelScope.launch {
            coroutineScope {
                supervisorScope {
                    words.postValue(Resource.loading(null))
                    try {
                        val wordFromApi = apiHelper.getWord(word)
                        words.postValue(Resource.success(wordFromApi))
                    } catch (e: Exception) {
                        words.postValue(Resource.error(e.message.toString(), null))
                    }
                }
            }
        }
        return words
    }

    fun getLatest(): MutableLiveData<List<History>> {
        viewModelScope.launch {
            dbHelper.getAllHistory().distinctUntilChanged().collect {
                if (it.isEmpty()) {
                    latesWords.value = emptyList()
                } else {
                    latesWords.value = it
                }
            }
        }
        return latesWords
    }

    fun getSaved(): MutableLiveData<List<History>> {
        viewModelScope.launch {
            dbHelper.getAllSaved().distinctUntilChanged().collect {
                savedWords.value = it
            }
        }
        return savedWords
    }

    fun insertHistory(history: History) {
        viewModelScope.launch {
            dbHelper.insertAll(history)
        }
    }

    fun findHistory(word: String) {
        viewModelScope.launch {
            dbHelper.findHistory(word)!!
        }
    }

    fun updateHistroy(history: History) {
        viewModelScope.launch {
            dbHelper.updateHistory(history)
        }
    }
}