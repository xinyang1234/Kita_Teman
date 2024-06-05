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
import com.example.kitateman.data.response.StoryResponse
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

    fun listStoryLoc(): LiveData<ResultOne<StoryResponse>> = liveData {
        try {
            val response = apiService.getStoryLoc(
                token = "Bearer ${dataPreferences.getUser().token}",
                page = 1,
                size = 100,
                location = 1
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

    fun aDDStory(
        imageFile: MultipartBody.Part,
        desc: RequestBody,
        lat: Double,
        lon: Double
    ): LiveData<ResultOne<AddNewStoryResponse>> = liveData {
        try {
            val response = apiService.aDDStory(
                token = "Bearer ${dataPreferences.getUser().token}",
                file = imageFile,
                description = desc,
                lat = lat,
                lon = lon
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