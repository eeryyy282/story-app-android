package com.example.storyappjuzairi.view.main.add_story

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.storyappjuzairi.databinding.FragmentAddStoryBinding
import com.example.storyappjuzairi.view.main.camera.CameraActivity
import com.google.android.material.snackbar.Snackbar

class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddStoryViewModel by viewModels()

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setSelectImageUri(uri)
            showImage()
        } else {
            Log.d("Photo picker", "Tidak ada foto yang dipilih")
        }
    }

    private val launchCameraActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.getStringExtra("image_uri")?.let { Uri.parse(it) }
            uri?.let {
                viewModel.setSelectImageUri(it)
                showImage()
            }
        } else {
            Log.d("Camera", "Gagal mengambil gambar")
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("Photo picker", "Izin diberikan")
                showSnackBar("Izin diberikan")
            } else {
                Log.d("Photo picker", "Izin ditolak")
                showSnackBar("Izin ditolak")
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAnimation()

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCameraX.setOnClickListener { startCameraX() }

        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private fun startCameraX() {
        val intentCamera = Intent(requireActivity(), CameraActivity::class.java)
        launchCameraActivity.launch(intentCamera)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun isPhotoPickerAvailable(): Boolean {
        return activity?.packageManager?.let { pm ->
            val pickIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.resolveActivity(pm) != null
        } ?: false
    }


    private fun startGallery() {
        if (isPhotoPickerAvailable()) {
            launchGallery.launch("image/*")
        } else {
            Log.d("Photo picker", "Photo picker tidak tersedia di perangkat ini")
            showSnackBar("Photo picker tidak tersedia di perangkat ini")
        }
    }

    private fun showImage() {
        val uri = viewModel.selectImageUri.value
        uri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImage.setImageURI(it)
        }
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.ivBanner, View.TRANSLATION_Y, -25f, 25f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}