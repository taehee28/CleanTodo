package com.thk.data

import com.thk.data.database.TodoDao

interface TodoDataSource {
    suspend fun getTodoList(): List<TodoRow>
}

class TodoDataSourceImpl(private val dao: TodoDao) : TodoDataSource {
    override suspend fun getTodoList(): List<TodoRow> {
        return dao.getTodoList()
    }
}