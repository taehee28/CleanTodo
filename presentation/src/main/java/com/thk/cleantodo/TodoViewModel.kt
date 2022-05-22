package com.thk.cleantodo

import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thk.cleantodo.util.logd
import com.thk.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTodoListUseCase: GetTodoListUseCase,
    private val addNewTodoUseCase: AddNewTodoUseCase,
    private val setCompletedUseCase: SetCompletedUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase
) : ViewModel() {

    val todoItems: StateFlow<List<Todo>> = getTodoListUseCase.invoke().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private var currentEditPosition by mutableStateOf(-1)

    val currentEditTodo: Todo?
        get() = todoItems.value.getOrNull(currentEditPosition)

//    var todoItems = MutableStateFlow<List<Todo>>(emptyList())
//        private set

    init {
//        viewModelScope.launch {
//            getTodoListUseCase.invoke().collect {
//                todoItems.emit(it)
//            }
//        }
    }


    fun addNewTodo(content: String) = viewModelScope.launch {
        addNewTodoUseCase.invoke(content)
    }

    fun onCheckCompleted(todo: Todo) = viewModelScope.launch {
        setCompletedUseCase.invoke(todo = todo)
    }

    fun deleteTodo(todo: Todo) = viewModelScope.launch {
        deleteTodoUseCase.invoke(todo)
    }

    fun onEditStart(todo: Todo) {
        currentEditPosition = todoItems.value.indexOf(todo)
    }

    fun onEditDone(todo: Todo) = viewModelScope.launch {
        updateTodoUseCase.invoke(todo)
    }


}