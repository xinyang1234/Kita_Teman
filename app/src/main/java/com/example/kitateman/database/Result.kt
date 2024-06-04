package com.example.kitateman.database

sealed class ResultOne<out R> private constructor() {
    object Loading : ResultOne<Nothing>()
    data class Errordata(val data: String) : ResultOne<Nothing>()
    data class Successdata<out T>(val data: T) : ResultOne<T>()
}