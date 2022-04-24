package com.thk.cleantodo

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thk.domain.GetTodoListUseCase
import com.thk.domain.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
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