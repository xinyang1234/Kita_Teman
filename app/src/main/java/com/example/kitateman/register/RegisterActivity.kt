package com.example.kitateman.register

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
import com.example.kitateman.database.DataPreferences
import com.example.kitateman.database.ResultOne
import com.example.kitateman.databinding.ActivityRegisterBinding
import com.example.kitateman.login.MainActivity
import com.example.kitateman.viewModelfactory.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModelRegister: ViewModelRegister
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelRegister = accommodating(this as AppCompatActivity)
        setupAction()
        setupAnimation()

    }

    private fun accommodating(activity: AppCompatActivity): ViewModelRegister {
        val regisPreferences = DataPreferences(activity.application)
        val factory = ViewModelFactory.getInstance(activity.application, regisPreferences)
        return ViewModelProvider(activity, factory)[ViewModelRegister::class.java]
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val nameanimation =
            ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(400)
        val emailanimation =
            ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(400)
        val passwordanimation =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(400)
        val buttonanimation =
            ObjectAnimator.ofFloat(binding.myButtonRegister, View.ALPHA, 1f).setDuration(400)
        val title = ObjectAnimator.ofFloat(binding.titleKitaTeman, View.ALPHA, 1f).setDuration(400)
        val desc =
            ObjectAnimator.ofFloat(binding.titleKitaAplikasi, View.ALPHA, 1f).setDuration(400)

        val together = AnimatorSet().apply {
            playTogether(nameanimation, emailanimation, passwordanimation, buttonanimation)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }
    }

    private fun setupAction() {
        binding.passwordEditTextRegis.addTextChangedListener(object : TextWatcher {
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

        binding.emailEditTextRegis.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.buttonBackToLogin.setOnClickListener {
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.myButtonRegister.setOnClickListener {
            Toast.makeText(this, getString(R.string.wait), Toast.LENGTH_SHORT).show()
            val password = binding.passwordEditTextRegis.text.toString()
            val email = binding.emailEditTextRegis.text.toString()
            val name = binding.nameEditTextRegis.text.toString()
            if (password.isNotEmpty() && name.isNotEmpty() && email.isNotEmpty()) {
                registerDataSend(name, email, password)
            }
        }

    }

    private fun setMyButtonEnable() {
        val password = binding.passwordEditTextRegis.text.toString()
        val email = binding.emailEditTextRegis.text.toString()
        val name = binding.nameEditTextRegis.text.toString()
        binding.myButtonRegister.isEnabled =
            password != null && password.isNotEmpty() && name != null && name.isNotEmpty() && email != null && email.isNotEmpty()
    }

    private fun registerDataSend(name: String, email: String, password: String) {
        viewModelRegister.registerUser(name, email, password)
            .observe(this@RegisterActivity) { result ->
                if (result != null) {
                    when (result) {
                        is ResultOne.Successdata -> {
                            Toast.makeText(
                                this@RegisterActivity,
                                getString(R.string.success),
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToLogin()
                        }

                        is ResultOne.Errordata -> {
                            Toast.makeText(
                                this@RegisterActivity,
                                getString(R.string.Something_wrong),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {}
                    }
                }

            }
    }

    private fun navigateToLogin() {
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }

}