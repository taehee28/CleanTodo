package com.thk.data

import com.thk.domain.Todo
import com.thk.domain.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(private val todoDataSource: TodoDataSource) : TodoRepository {
    override fun getTodoItems(): Flow<List<Todo>> {
        return todoDataSource.getTodoItems().map { list -> list.map { mapperToTodo(it) } }
    }

    override suspend fun addNewTodo(newTodo: Todo) {
        val todoRow = mapperToTodoRow(newTodo)
        todoDataSource.addNewTodo(todoRow)
    }

    override suspend fun setCompleted(todo: Todo) {
        val todoRow = mapperToTodoRow(todo)
        todoDataSource.setCompleted(todoRow)
    }

    override suspend fun deleteTodo(todo: Todo) {
        val todoRow = mapperToTodoRow(todo)
        todoDataSource.deleteTodo(todoRow)
    }

    override suspend fun updateTodo(todo: Todo) {
        val todoRow = mapperToTodoRow(todo)
        todoDataSource.updateTodo(todoRow)
    }
}