package com.example.kitateman.utils

import android.util.Patterns

object Validation {
    fun isInvalidPassword(password: CharSequence): Boolean = password.length < 8
    fun isInvalidEmail(email: CharSequence): Boolean = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
}