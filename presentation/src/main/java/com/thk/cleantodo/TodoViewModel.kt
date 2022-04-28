package com.thk.cleantodo

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thk.cleantodo.util.logd
import com.thk.domain.AddNewTodoUseCase
import com.thk.domain.GetTodoListUseCase
import com.thk.domain.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTodoListUseCase: GetTodoListUseCase,
    private val addNewTodoUseCase: AddNewTodoUseCase
) : ViewModel() {

    var todoItems = mutableStateListOf<Todo>()
        private set

    init {
        viewModelScope.launch {
            todoItems.addAll(getTodoListUseCase.invoke().toMutableStateList())
        }
    }

    fun addNewTodo(content: String) {
        val newTodo = Todo(content = content)

        todoItems.add(newTodo)

        viewModelScope.launch {
            addNewTodoUseCase.invoke(newTodo = newTodo)
        }
    }
}