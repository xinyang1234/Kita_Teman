package com.example.kitateman.addStory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.kitateman.R
import com.example.kitateman.database.DataPreferences
import com.example.kitateman.database.ResultOne
import com.example.kitateman.databinding.ActivityAddStoryBinding
import com.example.kitateman.home.ListStory
import com.example.kitateman.utils.compressImage
import com.example.kitateman.utils.getImageUri
import com.example.kitateman.viewModelfactory.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null
    private val camera_permission = 100
    private lateinit var viewModelAddStory: ViewModelAddStory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAction()
        viewModelAddStory = accommodating(this as AppCompatActivity)
    }

    private fun accommodating(activity: AppCompatActivity): ViewModelAddStory {
        val addStoryPreferences = DataPreferences(activity.application)
        val factory = ViewModelFactory.getInstance(activity.application, addStoryPreferences)
        return ViewModelProvider(activity, factory)[ViewModelAddStory::class.java]
    }

    private fun setupAction() {
        binding.buttonAdd.setOnClickListener {
            Toast.makeText(this@AddStoryActivity, getString(R.string.wait), Toast.LENGTH_SHORT)
                .show()
            if (binding.edAddDescription.text.isNullOrEmpty()) {
                Toast.makeText(this, getString(R.string.description_required), Toast.LENGTH_SHORT)
                    .show()
            } else {
                val description = binding.edAddDescription.text.toString()
                if (!TextUtils.isEmpty(description) && currentImageUri != null) {
                    PostFile(description)
                }
            }
        }

        binding.buttonBackToList.setOnClickListener {
            Intent(this, ListStory::class.java).apply {
                startActivity(this)
                finish()
            }
        }
        binding.buttonCamera.setOnClickListener { checkCameraPermissionAndStartCamera() }
        binding.buttonGaleri.setOnClickListener { startGallery() }
    }

    private fun PostFile(description: String) {
        currentImageUri?.let { uri ->
            val file = uriToFile(uri)
            val descriptionBody = RequestBody.create("text/plain".toMediaTypeOrNull(), description)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val multipartImage =
                MultipartBody.Part.createFormData("photo", file.name, requestImageFile)

            viewModelAddStory.aDDStory(multipartImage, descriptionBody).observe(this) { result ->
                when (result) {
                    is ResultOne.Successdata -> {
                        Toast.makeText(this, getString(R.string.success), Toast.LENGTH_SHORT).show()
                        finish()
                        navigateToHome()
                    }

                    is ResultOne.Errordata -> {
                        Toast.makeText(
                            this,
                            getString(R.string.Something_wrong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ResultOne.Loading -> {
                        Toast.makeText(this, getString(R.string.loading), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } ?: Toast.makeText(this, getString(R.string.Something_wrong), Toast.LENGTH_SHORT).show()
    }

    private fun navigateToHome() {
        binding.imageStory.setImageResource(R.drawable.baseline_image_24)
        binding.edAddDescription.text?.clear()
        Intent(this, ListStory::class.java).apply {
            startActivity(this)
            finish()
        }
    }

    private fun uriToFile(uri: Uri): File {
        val contentResolver = contentResolver
        val tempFile = File.createTempFile("temp_image", ".jpg", cacheDir)
        contentResolver.openInputStream(uri)?.use { inputStream ->
            val bitmap = BitmapFactory.decodeStream(inputStream)
            compressImage(bitmap, tempFile)
        }
        return tempFile
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo", "No media selected")
        }
    }

    private fun checkCameraPermissionAndStartCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                camera_permission
            )
        } else {
            startCamera()
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imageStory.setImageURI(it)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == camera_permission) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startCamera()
            } else {
                Toast.makeText(this, getString(R.string.camera_permission), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
