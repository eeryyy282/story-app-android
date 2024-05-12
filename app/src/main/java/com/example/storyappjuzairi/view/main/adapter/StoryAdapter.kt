package com.example.storyappjuzairi.view.main.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyappjuzairi.data.response.ListStoryItem
import com.example.storyappjuzairi.databinding.ItemStoryBinding
import com.example.storyappjuzairi.utils.DateFormatter
import com.example.storyappjuzairi.view.main.detail_story.DetailStoryActivity

class StoryAdapter :
    ListAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(
        DIFF_CALLBACK
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val story = getItem(position)
        holder.bind(story)

        holder.itemView.setOnClickListener {
            val storyId = story.id
            val intentId = Intent(holder.itemView.context, DetailStoryActivity::class.java).apply {
                putExtra(DetailStoryActivity.EXTRA_ID, storyId)
            }

            val optionCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.ivPhoto, "imageStoryDetail"),
                    Pair(holder.binding.tvName, "nameStoryDetail"),
                    Pair(holder.binding.tvDescription, "descriptionStoryDetail"),
                    Pair(holder.binding.tvCreatedAt, "dateStoryDetail")
                )

            holder.itemView.context.startActivity(intentId, optionCompat.toBundle())
        }
    }

    class MyViewHolder(val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(story: ListStoryItem) {
            binding.tvName.text = story.name
            binding.tvDescription.text = story.description
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.ivPhoto)
            val formattedDate = story.createdAt?.let { DateFormatter.formatDate(it) }
            binding.tvCreatedAt.text = formattedDate
        }
    }


    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ListStoryItem> =
            object : DiffUtil.ItemCallback<ListStoryItem>() {
                override fun areItemsTheSame(
                    oldItem: ListStoryItem,
                    newItem: ListStoryItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: ListStoryItem,
                    newItem: ListStoryItem
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }
}