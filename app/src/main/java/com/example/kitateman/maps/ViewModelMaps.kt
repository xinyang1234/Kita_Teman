package com.example.kitateman.maps

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.kitateman.database.DataPreferences
import com.example.kitateman.database.Repository

class ViewModelMaps(
    private val dataPreferences: DataPreferences, private val application: Application
) : ViewModel() {
    private val repository = Repository(application, dataPreferences)

    fun getStories() = repository.listStoryLoc()
}