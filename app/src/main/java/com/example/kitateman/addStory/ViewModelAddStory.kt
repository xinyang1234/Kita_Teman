package com.example.kitateman.addStory

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.kitateman.database.DataPreferences
import com.example.kitateman.database.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ViewModelAddStory(
    private val dataPreferences: DataPreferences, private val application: Application
) : ViewModel() {
    private val repository = Repository(application, dataPreferences)

    fun aDDStory(image: MultipartBody.Part, desc: RequestBody) =
        repository.aDDStory(image, desc)
}