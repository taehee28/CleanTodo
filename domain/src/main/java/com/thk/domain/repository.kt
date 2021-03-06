package com.thk.domain

import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodoItems(): Flow<List<Todo>>
    suspend fun addNewTodo(newTodo: Todo)
    suspend fun setCompleted(todo: Todo)
    suspend fun deleteTodo(todo:Todo)
    suspend fun updateTodo(todo: Todo)
}