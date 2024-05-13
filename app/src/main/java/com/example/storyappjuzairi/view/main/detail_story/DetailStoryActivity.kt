package com.example.storyappjuzairi.view.main.detail_story

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyappjuzairi.R
import com.example.storyappjuzairi.data.Result
import com.example.storyappjuzairi.databinding.ActivityDetailStoryBinding
import com.example.storyappjuzairi.utils.DateFormatter
import com.google.android.material.snackbar.Snackbar

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var detailStoryViewModel: DetailStoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val factoryStoryDetail: DetailStoryViewModelFactory =
            DetailStoryViewModelFactory.getInstance(application)
        detailStoryViewModel =
            ViewModelProvider(this, factoryStoryDetail)[DetailStoryViewModel::class.java]

        val id = intent.getStringExtra(EXTRA_ID)
        if (id != null) {
            if (detailStoryViewModel.storyDetail.value == null) {
                detailStoryViewModel.findDetailStory(id)
            }
        }

        detailStoryViewModel.storyDetail.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val story = result.data
                        with(binding) {
                            tvDetailName.text = story?.name
                            tvDetailDescription.text = story?.description
                            val formattedDate =
                                story?.createdAt?.let { DateFormatter.formatDate(it) }
                            tvDetailCreated.text = formattedDate
                            Glide.with(binding.root)
                                .load(story?.photoUrl)
                                .into(binding.ivDetailPhoto)
                        }
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(
                            binding.root,
                            "Gagal memuat detail cerita",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}