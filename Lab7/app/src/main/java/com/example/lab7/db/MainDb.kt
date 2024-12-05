package com.example.lab7.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lab7.utils.ListItem
@Database(
    entities = [ListItem::class],
    version = 1
)
abstract class MainDb: RoomDatabase() {
    abstract val dao:Dao;
}
