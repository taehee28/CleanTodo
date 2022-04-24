package com.thk.cleantodo

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thk.domain.GetTodoListUseCase
import com.thk.domain.Todo
import kotlinx.coroutines.launch

class TodoViewModel(
    private val getTodoListUseCase: GetTodoListUseCase
) : ViewModel() {

    var todoItems = mutableStateListOf<Todo>()
        private set

    init {
        getTodoList()
    }

    fun getTodoList() {
        viewModelScope.launch {
            todoItems = getTodoListUseCase.invoke().toMutableStateList()
        }
    }
}