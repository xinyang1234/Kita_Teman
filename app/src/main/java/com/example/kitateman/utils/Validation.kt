package com.example.kitateman.utils

import android.util.Patterns

object Validation {
    fun isInvalidEmail(email: CharSequence): Boolean = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
}