package com.thk.domain

import javax.inject.Inject

class GetTodoListUseCase @Inject constructor(private val repository: TodoRepository) {
    fun invoke() = repository.getTodoItems()
}

class AddNewTodoUseCase @Inject constructor(private val repository: TodoRepository) {
    suspend fun invoke(content: String) {
        val todo = Todo(content = content)
        repository.addNewTodo(newTodo = todo)
    }
}

class SetCompletedUseCase @Inject constructor(private val repository: TodoRepository) {
    suspend fun invoke(todo: Todo) {
        repository.setCompleted(todo = todo)
    }
}

class DeleteTodoUseCase @Inject constructor(private val repository: TodoRepository) {
    suspend fun invoke(todo: Todo) {
        repository.deleteTodo(todo = todo)
    }
}