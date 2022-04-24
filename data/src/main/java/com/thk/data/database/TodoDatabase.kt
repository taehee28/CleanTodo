package com.thk.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thk.data.DB_VERSION
import com.thk.data.TodoRow

@Database(entities = [TodoRow::class], version = DB_VERSION)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}