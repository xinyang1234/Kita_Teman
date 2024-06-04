package com.example.kitateman.register

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.kitateman.database.DataPreferences
import com.example.kitateman.database.Repository

class ViewModelRegister(
    private val dataPreferences: DataPreferences, private val application: Application
) : ViewModel() {
    private val repositoryRegister = Repository(application, dataPreferences)

    fun registerUser(name: String, email: String, password: String) =
        repositoryRegister.register(name, email, password)


}