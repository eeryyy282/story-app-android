package com.example.storyappjuzairi.view.maps

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyappjuzairi.R
import com.example.storyappjuzairi.data.Result
import com.example.storyappjuzairi.databinding.ActivityMapsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factoryResult: MapsViewModelFactory =
            MapsViewModelFactory.getInstance(this)
        mapsViewModel =
            ViewModelProvider(this, factoryResult)[MapsViewModel::class.java]

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupViewModel()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun setupViewModel() {
        mapsViewModel.getStoryLocation()
        mapsViewModel.location.observe(this) { storyLocation ->
            when (storyLocation) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    storyLocation.data.let { result ->
                        for (location in result) {
                            location?.let {
                                val latLng = LatLng(it.lat!!, it.lon!!)
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(latLng)
                                        .title(it.name)
                                )
                            }
                        }
                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Gagal mendapatkan lokasi cerita",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }
}