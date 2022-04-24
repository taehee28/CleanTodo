package com.thk.data

import com.thk.data.database.TodoDao
import javax.inject.Inject

interface TodoDataSource {
    suspend fun getTodoItems(): List<TodoRow>
}

class TodoDataSourceImpl @Inject constructor(private val dao: TodoDao) : TodoDataSource {
    override suspend fun getTodoItems(): List<TodoRow> {
        return dao.getTodoItems()
    }
}