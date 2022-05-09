package com.thk.domain

interface TodoRepository {
    suspend fun getTodoItems(): List<Todo>
    suspend fun addNewTodo(newTodo: Todo)
    suspend fun setCompleted(todo: Todo)
}