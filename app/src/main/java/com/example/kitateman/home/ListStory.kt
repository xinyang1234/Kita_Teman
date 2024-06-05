package com.example.kitateman.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kitateman.R
import com.example.kitateman.addStory.AddStoryActivity
import com.example.kitateman.data.adapter.Loading
import com.example.kitateman.data.adapter.StoryAdapter
import com.example.kitateman.database.DataPreferences
import com.example.kitateman.databinding.ActivityListStoryBinding
import com.example.kitateman.login.MainActivity
import com.example.kitateman.maps.MapsActivity
import com.example.kitateman.viewModelfactory.ViewModelFactory

class ListStory : AppCompatActivity() {
    private lateinit var binding: ActivityListStoryBinding
    private lateinit var dataloginPreference: DataPreferences
    private lateinit var listStoryViewModel: ViewModelListStory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataloginPreference = DataPreferences(this)
        listStoryViewModel = accommodating(this as AppCompatActivity)
        binding.rcListStory.layoutManager = LinearLayoutManager(this)
        getData()
        setupAction()
    }

    private fun accommodating(activity: AppCompatActivity): ViewModelListStory {
        val listStoryPreferences = DataPreferences(activity.application)
        val factory = ViewModelFactory.getInstance(activity.application, listStoryPreferences)
        return ViewModelProvider(activity, factory)[ViewModelListStory::class.java]
    }

    private fun setupAction() {
        binding.buttonToAddStory.setOnClickListener {
            Intent(this, AddStoryActivity::class.java).apply {
                startActivity(this)
            }
        }
        binding.buttonLogout.setOnClickListener {
            dataloginPreference.deleteUser()
            Toast.makeText(this, getString(R.string.successful_exit), Toast.LENGTH_SHORT).show()
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
        binding.buttonToMaps.setOnClickListener {
            Intent(this, MapsActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun getData() {
        val adapter = StoryAdapter()
        listStoryViewModel.getListStory.observe(this) {
            adapter.submitData(lifecycle, it)
        }
        binding.rcListStory.adapter = adapter.withLoadStateFooter(
            footer = Loading {
                adapter.retry()
            }
        )
    }
}