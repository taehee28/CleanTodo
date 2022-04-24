package com.thk.domain

interface TodoRepository {
    suspend fun getTodoItems(): List<Todo>
}