package com.thk.data.database

import androidx.room.*
import com.thk.data.TABLE_NAME
import com.thk.data.TodoRow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface TodoDao {
    @Query("select * from $TABLE_NAME")
    fun getTodoItems(): Flow<List<TodoRow>>

    @Insert
    suspend fun addNewTodo(todoRow: TodoRow)

    @Update
    suspend fun setCompleted(todoRow: TodoRow)

    @Delete
    suspend fun deleteTodo(todoRow: TodoRow)
}