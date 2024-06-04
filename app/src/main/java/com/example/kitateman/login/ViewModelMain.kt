package com.example.kitateman.login

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.kitateman.database.DataPreferences
import com.example.kitateman.database.Repository

class ViewModelMain(
    private val dataPreferences: DataPreferences, private val application: Application
) : ViewModel() {
    private val repositoryLogin = Repository(application, dataPreferences)

    fun loginUser(email: String, password: String) = repositoryLogin.login(email, password)
}