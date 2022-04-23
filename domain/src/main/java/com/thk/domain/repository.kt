package com.thk.domain

interface TodoRepository {
    suspend fun getTodoList(): List<Todo>
}