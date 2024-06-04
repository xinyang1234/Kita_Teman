package com.example.kitateman.splasScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.kitateman.data.response.LoginResult
import com.example.kitateman.database.DataPreferences
import com.example.kitateman.databinding.ActivitySplashScreenBinding
import com.example.kitateman.home.ListStory

class SplashScreen : AppCompatActivity() {
    private lateinit var activitySplashBinding: ActivitySplashScreenBinding
    private lateinit var dataloginPreference: DataPreferences
    private lateinit var loginModel: LoginResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(activitySplashBinding.root)
        dataloginPreference = DataPreferences(this)
        loginModel = dataloginPreference.getUser()
        Handler(Looper.getMainLooper()).postDelayed({
            if (loginModel.name != null && loginModel.token != null && loginModel.userId != null) {
                Intent(this, ListStory::class.java).apply {
                    startActivity(this)
                    finish()
                }
            } else {
                Intent(this, WelcomeActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
        }, _DURATION)
    }

    companion object {
        const val _DURATION = 2000L
    }
}