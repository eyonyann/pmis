package com.example.lab7.utils

import androidx.compose.runtime.saveable.listSaver
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "main")
data class ListItem(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val title: String,
    val imageName: String,
    val htmlName: String,
    val isfav: Boolean,
    val category:String
)

val ItemSaver = listSaver<ListItem, Any>(
    save = { listOf(it.title, it.imageName, it.htmlName, it.isfav, it.category) },
    restore = {
        ListItem(
            id = 0,
            title = it[0] as String,
            imageName = it[1] as String,
            htmlName = it[2] as String,
            isfav = it[3] as Boolean,
            category = it[4] as String
        )
    }
)
