package com.example.kitateman.home

import androidx.lifecycle.ViewModel
import com.example.kitateman.database.Repository

class ViewModelStory(private val storyRepository: Repository) : ViewModel() {
    fun getStory() = storyRepository.listStory()
}