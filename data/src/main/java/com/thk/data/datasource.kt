package com.thk.data

import com.thk.data.database.TodoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface TodoDataSource {
    fun getTodoItems(): Flow<List<TodoRow>>
    suspend fun addNewTodo(todoRow: TodoRow)
    suspend fun setCompleted(todoRow: TodoRow)
    suspend fun deleteTodo(todoRow: TodoRow)
}

class TodoDataSourceImpl @Inject constructor(private val dao: TodoDao) : TodoDataSource {
    override fun getTodoItems(): Flow<List<TodoRow>> {
        return dao.getTodoItems()
    }

    override suspend fun addNewTodo(todoRow: TodoRow) {
        dao.addNewTodo(todoRow)
    }

    override suspend fun setCompleted(todoRow: TodoRow) {
        dao.setCompleted(todoRow)
    }

    override suspend fun deleteTodo(todoRow: TodoRow) {
        dao.deleteTodo(todoRow)
    }
}