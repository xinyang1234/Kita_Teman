package com.example.kitateman.data.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kitateman.data.response.ListStoryItem
import com.example.kitateman.databinding.ItemRowListStoryBinding
import com.example.kitateman.detailStory.DetailStoryActivity

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        return ListViewHolder(
            ItemRowListStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(story = data)
        }
    }

    class ListViewHolder(private val binding: ItemRowListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            with(binding) {
                Glide.with(itemView)
                    .load(story.photoUrl)
                    .into(ivItemPhoto)
                tvItemName.text = story.name
                val latitude = story.lat.toString()
                val longitude = story.lon.toString()
                binding.cardStory.setOnClickListener {
                    val intent = Intent(itemView.context, DetailStoryActivity::class.java).apply {
                        putExtra(DetailStoryActivity.USERNAME, story.name)
                        putExtra(DetailStoryActivity.DESCRIPTION, story.description)
                        putExtra(DetailStoryActivity.PHOTO, story.photoUrl)
                        putExtra(DetailStoryActivity.LATITUDE, latitude)
                        putExtra(DetailStoryActivity.LONGITUDE, longitude)
                    }
                    val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        androidx.core.util.Pair(ivItemPhoto as View, "images"),
                        androidx.core.util.Pair(tvItemName as View, "name")
                    )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }

                if (story.lat != 0.0 && story.lon != 0.0) {
                    storyLocation.visibility = View.GONE
                } else {
                    storyLocation.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
