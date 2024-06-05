package com.example.kitateman

import com.example.kitateman.data.response.ListStoryItem

object Dummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "createdAt + $i",
                "name $i",
                "desctiption $i",
                i.toDouble(),
                "id $i",
                i.toDouble(),
            )
            items.add(story)
        }
        return items
    }
}