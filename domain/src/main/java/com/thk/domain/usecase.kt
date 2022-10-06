package com.thk.domain

class GetTodoListUseCase(private val repository: TodoRepository) {
    fun invoke() = repository.getTodoItems()
}

class AddNewTodoUseCase(private val repository: TodoRepository) {
    suspend fun invoke(content: String) {
        val todo = Todo(content = content)
        repository.addNewTodo(newTodo = todo)
    }
}

class SetCompletedUseCase(private val repository: TodoRepository) {
    suspend fun invoke(todo: Todo) {
        repository.setCompleted(todo = todo)
    }
}

class DeleteTodoUseCase(private val repository: TodoRepository) {
    suspend fun invoke(todo: Todo) {
        repository.deleteTodo(todo = todo)
    }
}

class UpdateTodoUseCase(private val repository: TodoRepository) {
    suspend fun invoke(todo: Todo) {
        repository.updateTodo(todo)
    }
}