package com.example.kitateman.database

import android.content.Context
import com.example.kitateman.data.response.LoginResult

class DataPreferences(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setLogin(value: LoginResult) {
        val editor = preferences.edit()
        editor.putString(NAME, value.name)
        editor.putString(USER_ID, value.userId)
        editor.putString(TOKEN, value.token)
        editor.apply()
    }

    fun getUser(): LoginResult {
        val name = preferences.getString(NAME, null)
        val userId = preferences.getString(USER_ID, null)
        val token = preferences.getString(TOKEN, null)
        return LoginResult(userId, name, token)
    }

    fun deleteUser() {
        val editor = preferences.edit().clear()
        editor.apply()
    }

    fun getString(key: String): String? {
        return preferences.getString(key, null)
    }

    companion object {
        const val PREFS_NAME = "login_pref"
        const val NAME = "name"
        const val USER_ID = "userId"
        const val TOKEN = "token"
    }
}
