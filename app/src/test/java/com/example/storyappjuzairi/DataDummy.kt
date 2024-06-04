package com.example.storyappjuzairi

import com.example.storyappjuzairi.data.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                id = i.toString(),
                name = "author $i",
                description = "story $i",
                photoUrl = "photo_url_$i",
                createdAt = "created_at_$i",
                lat = i.toDouble(),
                lon = i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}
