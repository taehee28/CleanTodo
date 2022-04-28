@file:OptIn(ExperimentalMaterial3Api::class)

package com.thk.cleantodo.ui

import android.util.Log
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thk.cleantodo.ui.theme.CleanTodoTheme
import com.thk.domain.Todo

@Composable
fun TodoScreen(
    todoItems: List<Todo>,
    onAddNewTodo: (String) -> Unit,
) {
    val (input, setInput) = remember { mutableStateOf("")}

    Log.d("TAG", ">>>>>>>>>> TodoScreen: todoItems size = ${todoItems.size}")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "To-do List", style = MaterialTheme.typography.titleLarge)
                }
            )
        },
        bottomBar = {
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
                        if (input.isEmpty()) return@Button

                        onAddNewTodo(input)
                        setInput("")
                    }
                ) {
                    Icon(Icons.Filled.Send, contentDescription = "send")
                }
            }
        }
    ) {
        TodoList(
            Modifier.padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding()
            ),
            todoItems = todoItems
        )
    }
}

@Composable
@Preview
fun TodoScreenPreview()  {
    CleanTodoTheme {
        TodoScreen(emptyList(), {})
    }
}

@Composable
fun TodoList(
    modifier: Modifier = Modifier,
    todoItems: List<Todo>,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(todoItems) {
            TodoRow(isCompleted = it.isCompleted, content = it.content)
        }
    }
}

@Composable
fun TodoRow(
    isCompleted: Boolean,
    content: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = {

                }
            )

            Text(
                text = content,
                textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )
        }

    }
}