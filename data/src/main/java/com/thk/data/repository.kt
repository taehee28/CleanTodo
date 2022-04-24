package com.thk.data

import com.thk.domain.Todo
import com.thk.domain.TodoRepository

class TodoRepositoryImpl(private val todoDataSource: TodoDataSource) : TodoRepository {
    override suspend fun getTodoList(): List<Todo> {
        return todoDataSource.getTodoList().map { mapperToTodo(it) }
    }
}