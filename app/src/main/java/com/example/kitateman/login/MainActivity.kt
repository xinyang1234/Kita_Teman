package com.example.kitateman.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.kitateman.R
import com.example.kitateman.data.response.LoginResponse
import com.example.kitateman.data.response.LoginResult
import com.example.kitateman.database.DataPreferences
import com.example.kitateman.database.ResultOne
import com.example.kitateman.databinding.ActivityMainBinding
import com.example.kitateman.home.ListStory
import com.example.kitateman.register.RegisterActivity
import com.example.kitateman.viewModelfactory.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModelMain: ViewModelMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelMain = accommodating(this as AppCompatActivity)
        setupAction()
        setupAnimantion()
    }

    private fun accommodating(activity: AppCompatActivity): ViewModelMain {
        val loginPreferences = DataPreferences(activity.application)
        val factory = ViewModelFactory.getInstance(activity.application, loginPreferences)
        return ViewModelProvider(activity, factory)[ViewModelMain::class.java]
    }

    private fun setupAnimantion() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val emailEdit =
            ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(200)
        val passwordEdit =
            ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(200)
        val buttonLogin =
            ObjectAnimator.ofFloat(binding.myButtonMain, View.ALPHA, 1f).setDuration(200)
        val title = ObjectAnimator.ofFloat(binding.titleKitaTeman, View.ALPHA, 1f).setDuration(200)
        val desc =
            ObjectAnimator.ofFloat(binding.titleKitaAplikasi, View.ALPHA, 1f).setDuration(200)

        val together = AnimatorSet().apply {
            playTogether(emailEdit, passwordEdit, buttonLogin)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }
    }


    private fun setupAction() {
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    binding.titleRegisterPasswordWarning.setText(R.string.Password_cannot_characters)
                } else {
                    binding.titleRegisterPasswordWarning.text = ""
                    setMyButtonEnable()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setMyButtonEnable()

            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.myButtonMain.setOnClickListener {

            Toast.makeText(this@MainActivity, getString(R.string.wait), Toast.LENGTH_SHORT).show()
            val password = binding.passwordEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            if (password.isNotEmpty() && email.isNotEmpty()) {
                loginVerificationData(email, password)
            }
        }

        binding.titleToRegister.setOnClickListener {
            Intent(this, RegisterActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun setMyButtonEnable() {
        val resulttwo = binding.passwordEditText.text.toString()
        val resultOne = binding.emailEditText.text.toString()
        binding.myButtonMain.isEnabled =
            resulttwo != null && resulttwo.isNotEmpty() && resultOne != null && resultOne.isNotEmpty()
    }

    private fun loginVerificationData(email: String, password: String) {
        viewModelMain.loginUser(email, password)
            .observe(this@MainActivity) { result ->
                if (result != null) {
                    when (result) {
                        is ResultOne.Successdata -> {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.welcome),
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToListStory(result.data)
                        }

                        is ResultOne.Errordata -> {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.Something_wrong),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {}
                    }
                }
            }
    }

    private fun navigateToListStory(data: LoginResponse) {
        saveDataLogin(data)
        Intent(this, ListStory::class.java).apply {
            startActivity(this)
            finish()
        }
    }

    private fun saveDataLogin(data: LoginResponse) {
        val dataPreference = DataPreferences(this)
        val loginResult = data.loginResult
        val loginModel = LoginResult(
            name = loginResult?.name, userId = loginResult?.userId, token = loginResult?.token
        )
        dataPreference.setLogin(loginModel)
    }
}