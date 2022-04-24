package com.thk.cleantodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thk.cleantodo.ui.TodoScreen
import com.thk.cleantodo.ui.theme.CleanTodoTheme

class MainActivity : ComponentActivity() {

    private val todoViewModel by viewModels<TodoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoApp(todoViewModel = todoViewModel)
        }
    }
}

@Composable
fun TodoApp(todoViewModel: TodoViewModel) {
    CleanTodoTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            TodoScreen(todoItems = todoViewModel.todoItems)
        }
    }
}