package com.thk.domain

import javax.inject.Inject

class GetTodoListUseCase @Inject constructor(private val repository: TodoRepository) {
    suspend fun invoke() = repository.getTodoItems()
}