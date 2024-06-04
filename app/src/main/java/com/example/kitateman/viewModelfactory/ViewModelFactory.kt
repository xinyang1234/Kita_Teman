package com.example.kitateman.viewModelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kitateman.addStory.ViewModelAddStory
import com.example.kitateman.database.DataPreferences
import com.example.kitateman.home.ViewModelListStory
import com.example.kitateman.login.ViewModelMain
import com.example.kitateman.register.ViewModelRegister

class ViewModelFactory(
    private val application: Application,
    private val dataPreferences: DataPreferences
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ViewModelRegister::class.java) ->
                ViewModelRegister(
                    dataPreferences,
                    application
                ) as T

            modelClass.isAssignableFrom(ViewModelMain::class.java) ->
                ViewModelMain(
                    dataPreferences,
                    application
                ) as T

            modelClass.isAssignableFrom(ViewModelListStory::class.java) ->
                ViewModelListStory(
                    dataPreferences,
                    application
                ) as T

            modelClass.isAssignableFrom(ViewModelAddStory::class.java) ->
                ViewModelAddStory(
                    dataPreferences,
                    application
                ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            application: Application,
            dataPreferences: DataPreferences
        ): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(application, dataPreferences).also { INSTANCE = it }
            }
        }
    }
}
