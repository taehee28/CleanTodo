package com.thk.data.database

import androidx.room.Dao
import androidx.room.Query
import com.thk.data.TABLE_NAME
import com.thk.data.TodoRow

@Dao
interface TodoDao {
    @Query("select * from $TABLE_NAME")
    suspend fun getTodoItems(): List<TodoRow>
}