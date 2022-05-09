package com.thk.data

import com.thk.domain.Todo
import com.thk.domain.TodoRepository
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(private val todoDataSource: TodoDataSource) : TodoRepository {
    override suspend fun getTodoItems(): List<Todo> {
        return todoDataSource.getTodoItems().map { mapperToTodo(it) }
    }

    override suspend fun addNewTodo(newTodo: Todo) {
        val todoRow = mapperToTodoRow(newTodo)
        todoDataSource.addNewTodo(todoRow)
    }

    override suspend fun setCompleted(todo: Todo) {
        val todoRow = mapperToTodoRow(todo)
        todoDataSource.setCompleted(todoRow)
    }
}