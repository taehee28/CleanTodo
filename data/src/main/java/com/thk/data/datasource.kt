package com.thk.data

import com.thk.data.database.TodoDao

interface TodoDataSource {
    suspend fun getTodoItems(): List<TodoRow>
}

class TodoDataSourceImpl(private val dao: TodoDao) : TodoDataSource {
    override suspend fun getTodoItems(): List<TodoRow> {
        return dao.getTodoItems()
    }
}