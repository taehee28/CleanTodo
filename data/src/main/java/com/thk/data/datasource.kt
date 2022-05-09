package com.thk.data

import com.thk.data.database.TodoDao
import javax.inject.Inject

interface TodoDataSource {
    suspend fun getTodoItems(): List<TodoRow>
    suspend fun addNewTodo(todoRow: TodoRow)
    suspend fun setCompleted(todoRow: TodoRow)
}

class TodoDataSourceImpl @Inject constructor(private val dao: TodoDao) : TodoDataSource {
    override suspend fun getTodoItems(): List<TodoRow> {
        return dao.getTodoItems()
    }

    override suspend fun addNewTodo(todoRow: TodoRow) {
        dao.addNewTodo(todoRow)
    }

    override suspend fun setCompleted(todoRow: TodoRow) {
        dao.setCompleted(todoRow)
    }
}