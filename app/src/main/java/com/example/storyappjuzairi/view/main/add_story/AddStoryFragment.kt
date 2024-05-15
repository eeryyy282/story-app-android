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
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.storyappjuzairi.R
import com.example.storyappjuzairi.databinding.FragmentAddStoryBinding
import com.example.storyappjuzairi.utils.AppExecutors
import com.example.storyappjuzairi.utils.uriToFile
import com.example.storyappjuzairi.view.main.camera.CameraActivity
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import reduceFileImage

class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!

    private val addStoryViewModel: AddStoryViewModel by viewModels {
        AddStoryViewModelFactory.getInstance(requireContext())
    }

    private val appExecutors: AppExecutors by lazy { AppExecutors() }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            addStoryViewModel.setSelectImageUri(uri)
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
                addStoryViewModel.setSelectImageUri(it)
                showImage()
            }
        } else {
            Log.d("Camera", "Gagal mengambil gambar")
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAnimation()
        setButtonEnable()
        checkChanged()
        setupObserver()
        buttonClickListener()

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCameraX.setOnClickListener { startCameraX() }

        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (addStoryViewModel.uploadSuccess.value == true) {
                        toHomeFragment()
                    } else {
                        findNavController().popBackStack()
                    }
                }

            })


    }

    private fun buttonClickListener() {
        binding.buttonAdd.setOnClickListener {
            val currentImageUri = addStoryViewModel.selectImageUri.value
            if (currentImageUri != null) {
                binding.buttonAdd.isEnabled = false
                showLoading(true)

                appExecutors.diskIO.execute {
                    try {
                        val imageFIle = uriToFile(currentImageUri, requireContext())
                        val reduceImageFile = imageFIle.reduceFileImage()
                        val description = binding.edAddDescription.text.toString()
                        val requestBody = description.toRequestBody("text/plain".toMediaType())
                        val requestImageFile =
                            reduceImageFile.asRequestBody("image/jpeg".toMediaType())
                        val multiPart = MultipartBody.Part.createFormData(
                            "photo", reduceImageFile.name, requestImageFile

                        )
                        appExecutors.mainThread.execute {
                            addStoryViewModel.uploadImage(multiPart, requestBody)

                        }

                    } catch (e: Exception) {
                        appExecutors.mainThread.execute {
                            showSnackBar("Terjadi kesalahan: ${e.message}")
                            binding.buttonAdd.isEnabled = true
                            showLoading(false)
                        }
                    }
                }


            } else {

                showSnackBar("Silakan pilih atau ambil gambar terlebih dahulu")

            }
        }
    }

    private fun setupObserver() {
        addStoryViewModel.selectImageUri.observe(viewLifecycleOwner) {
            showImage()
        }

        addStoryViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
            if (!it) {
                binding.buttonAdd.isEnabled = true
            }
        }

        addStoryViewModel.snackBar.observe(viewLifecycleOwner) {
            it?.let {
                showSnackBar(it)
            }
        }

        addStoryViewModel.uploadSuccess.observe(viewLifecycleOwner) {
            if (it) {
                toHomeFragment()
                resetFragment()
            }
        }
    }

    private fun toHomeFragment() {
        findNavController().navigate(
            R.id.action_navigation_add_story_to_navigation_home,
            null,
            NavOptions.Builder().setPopUpTo(R.id.navigation_add_story, true).build()
        )
    }

    private fun checkChanged() {
        binding.edAddDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun setButtonEnable() {
        val description = binding.edAddDescription.text
        binding.buttonAdd.isEnabled = (description.toString().isNotEmpty())
    }

    private fun resetFragment() {
        addStoryViewModel.setSelectImageUri(null)
        binding.edAddDescription.text.clear()
        binding.previewImage.setImageURI(null)
    }


    private fun allPermissionGranted() = ContextCompat.checkSelfPermission(
        requireContext(), REQUIRED_PERMISSION
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
        val uri = addStoryViewModel.selectImageUri.value
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
            requireView(), message, Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.overlay.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}