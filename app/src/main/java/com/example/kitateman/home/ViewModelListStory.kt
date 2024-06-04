package com.example.kitateman.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.kitateman.data.response.ListStoryItem
import com.example.kitateman.database.DataPreferences
import com.example.kitateman.database.Repository

class ViewModelListStory(
    private val dataPreferences: DataPreferences, private val application: Application
) : ViewModel() {
    private val repository = Repository(application, dataPreferences)

    val getListStory: LiveData<PagingData<ListStoryItem>> =
        repository.listStory().cachedIn(viewModelScope)
}