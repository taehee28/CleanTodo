package com.thk.domain

class GetTodoListUseCase(private val repository: TodoRepository) {
    suspend fun invoke() = repository.getTodoItems()
}