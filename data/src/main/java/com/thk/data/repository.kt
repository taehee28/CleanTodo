package com.thk.data

import com.thk.domain.Todo
import com.thk.domain.TodoRepository

class TodoRepositoryImpl(private val todoDataSource: TodoDataSource) : TodoRepository {
    override suspend fun getTodoItems(): List<Todo> {
        return todoDataSource.getTodoItems().map { mapperToTodo(it) }
    }
}