package com.example.kitateman.detailStory

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kitateman.databinding.ActivityDetailStoryBinding
import com.example.kitateman.home.ListStory

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val decs = intent.getStringExtra(DESCRIPTION)
        val photo = intent.getStringExtra(PHOTO)
        val username = intent.getStringExtra(USERNAME)
        binding.apply {
            Glide.with(this@DetailStoryActivity)
                .load(photo)
                .into(tvDetailPhoto)
            tvDetailName.text = username
            tvDetailDescription.text = decs
        }

        setupAction()
    }

    private fun setupAction() {
        binding.buttonBackToListInDetailStory.setOnClickListener {
            Intent(this, ListStory::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }

    companion object {
        const val USERNAME = "name"
        const val DESCRIPTION = "desc"
        const val PHOTO = "photo"
        const val LATITUDE = "lat"
        const val LONGITUDE = "long"
    }
}