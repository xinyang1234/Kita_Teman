package com.example.kitateman.database

import ApiConfig
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.kitateman.data.response.AddNewStoryResponse
import com.example.kitateman.data.response.ListStoryItem
import com.example.kitateman.data.response.LoginResponse
import com.example.kitateman.data.response.RegisterResponse
import com.example.kitateman.data.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository(application: Application, private val dataPreferences: DataPreferences) {
    private val apiService: ApiService = ApiConfig.getApiService()

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultOne<RegisterResponse>> = liveData {
        emit(ResultOne.Loading)
        try {
            val response = apiService.register(
                name, email, password
            )
            if (response.error) {
                emit(ResultOne.Errordata(response.message))
            } else {
                emit(ResultOne.Successdata(response))
            }
        } catch (e: Exception) {
            emit(ResultOne.Errordata(e.message.toString()))
        }

    }

    fun login(email: String, password: String):
            LiveData<ResultOne<LoginResponse>> = liveData {
        try {
            val response = apiService.login(
                email, password
            )
            if (response.error) {
                emit(ResultOne.Errordata(response.message))
            } else {
                emit(ResultOne.Successdata(response))
            }
        } catch (e: Exception) {
            emit(ResultOne.Errordata(e.message.toString()))
        }
    }

    fun listStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPage(dataPreferences, apiService)
            }
        ).liveData
    }

    fun aDDStory(
        imageFile: MultipartBody.Part,
        desc: RequestBody
    ): LiveData<ResultOne<AddNewStoryResponse>> = liveData {
        try {
            val response = apiService.aDDStory(
                token = "Bearer ${dataPreferences.getUser().token}",
                file = imageFile,
                description = desc
            )
            if (response.error) {
                emit(ResultOne.Errordata(response.message))
            } else {
                emit(ResultOne.Successdata(response))
            }
        } catch (e: Exception) {
            emit(ResultOne.Errordata(e.message.toString()))
        }
    }
}