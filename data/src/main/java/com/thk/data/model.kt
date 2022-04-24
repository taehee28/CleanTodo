package com.thk.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TABLE_NAME)
data class TodoRow(
    @PrimaryKey(autoGenerate = true)
    val num: Int,
    val content: String,
    val isCompleted: Boolean
)