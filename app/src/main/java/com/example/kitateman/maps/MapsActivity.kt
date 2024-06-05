package com.example.kitateman.maps

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.kitateman.R
import com.example.kitateman.data.response.ListStoryItem
import com.example.kitateman.database.DataPreferences
import com.example.kitateman.database.ResultOne
import com.example.kitateman.databinding.ActivityMapsBinding
import com.example.kitateman.home.ListStory
import com.example.kitateman.viewModelfactory.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var viewModelMaps: ViewModelMaps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        viewModelMaps = accommodating(this as AppCompatActivity)

        setUpAction()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun accommodating(activity: AppCompatActivity): ViewModelMaps {
        val mapPreferences = DataPreferences(activity.application)
        val factory = ViewModelFactory.getInstance(activity.application, mapPreferences)
        return ViewModelProvider(activity, factory)[ViewModelMaps::class.java]
    }

    private fun setUpAction() {
        binding.buttonBackToListInMaps.setOnClickListener {
            Intent(this, ListStory::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        val dicodingSpace = LatLng(-6.8957643, 107.6338462)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 7f))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 7f))
        setMapStyle()
        getStoryMaps(googleMap)
    }

    private fun getStoryMaps(googleMap: GoogleMap) {
        viewModelMaps.getStories().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultOne.Successdata -> {
                        showMarker(result.data.listStory, googleMap, this)
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
        }
    }

    private fun showMarker(listStory: List<ListStoryItem>, googleMap: GoogleMap, context: Context) {
        listStory.forEach { story ->
            val latLng = LatLng(story.lat, story.lon)
            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(story.name)
            )
            boundsBuilder.include(latLng)
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }

            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }

            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }

            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    companion object {
        const val TAG = "MapActivity"
    }
}