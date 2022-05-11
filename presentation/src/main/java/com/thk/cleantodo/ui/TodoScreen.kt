@file:OptIn(ExperimentalMaterial3Api::class)

package com.thk.cleantodo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thk.cleantodo.ui.theme.CleanTodoTheme
import com.thk.domain.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun TodoScreen(
    todoItemsFlow: StateFlow<List<Todo>>,
    onAddNewTodo: (String) -> Unit,
    onCheckCompleted: (Todo) -> Unit
) {
    val todoItems by todoItemsFlow.collectAsState()
    val (input, setInput) = remember { mutableStateOf("")}

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "To-do List", style = MaterialTheme.typography.titleLarge)
                }
            )
        },
        bottomBar = {
            TodoInput(input = input, setInput = setInput, onAddNewTodo = onAddNewTodo)
        }
    ) {
        TodoList(
            Modifier.padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding()
            ),
            todoItems = todoItems,
            onCheckCompleted = onCheckCompleted
        )
    }
}

@Composable
@Preview
fun TodoScreenPreview()  {
    CleanTodoTheme {
        TodoScreen(MutableStateFlow(emptyList()), {}, { todo -> })
    }
}

@Composable
fun TodoInput(
    input: String,
    setInput: (String) -> Unit,
    onAddNewTodo: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = setInput,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Button(
            onClick = {
                if (input.trim().isEmpty()) return@Button

                onAddNewTodo(input)
                setInput("")
            }
        ) {
            Icon(Icons.Filled.Send, contentDescription = "send")
        }
    }
}

@Composable
fun TodoList(
    modifier: Modifier = Modifier,
    todoItems: List<Todo>,
    onCheckCompleted: (Todo) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(todoItems) {
            TodoRow(todo = it, onCheckCompleted = onCheckCompleted)
        }
    }
}

@Composable
fun TodoRow(
    todo: Todo,
    onCheckCompleted: (Todo) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = {
                    onCheckCompleted(todo.copy(isCompleted = it))
                }
            )

            Text(
                text = todo.content,
                textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )
        }

    }
}