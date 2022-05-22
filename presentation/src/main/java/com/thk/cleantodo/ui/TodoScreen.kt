@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class, ExperimentalMaterialApi::class
)

package com.thk.cleantodo.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thk.cleantodo.ui.theme.CleanTodoTheme
import com.thk.domain.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun TodoScreen(
    todoItemsFlow: StateFlow<List<Todo>>,
    onAddNewTodo: (String) -> Unit,
    onCheckCompleted: (Todo) -> Unit,
    onDeleteTodo: (Todo) -> Unit
) {
    val todoItems by todoItemsFlow.collectAsState()
    val (input, setInput) = remember { mutableStateOf("")}

    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "To-do List", style = MaterialTheme.typography.titleLarge)
                }
            )
        },
        bottomBar = {
            TodoInput(
                input = input,
                setInput = setInput,
                onAddButtonClick = {
                    if (input.trim().isNotEmpty()) {
                        setInput("")
                        focusManager.clearFocus(true)

                        onAddNewTodo(input)
                    }
                },
                onFocusChanged = {
                    if (it.isFocused) {
                        scope.launch {
                            todoItems.lastIndex.let { index -> if (index > 0) scrollState.animateScrollToItem(index)}
                        }
                    }
                }
            )
        },
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = interactionSource
            ) {
                focusManager.clearFocus(true)
            }
    ) {
        TodoList(
            Modifier.padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding()
            ),
            todoItems = todoItems,
            onCheckCompleted = onCheckCompleted,
            onDeleteTodo = onDeleteTodo,
            scrollState = scrollState
        )
    }
}

@Composable
@Preview
fun TodoScreenPreview()  {
    CleanTodoTheme {
        TodoScreen(MutableStateFlow(emptyList()), {}, {}, {})
    }
}

@Composable
fun TodoInput(
    input: String,
    setInput: (String) -> Unit,
    onAddButtonClick: () -> Unit,
    onFocusChanged: (FocusState) -> Unit
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
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onAddButtonClick() }),
            modifier = Modifier
                .weight(1f)
                .onFocusChanged(onFocusChanged)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Button(
            onClick = onAddButtonClick
        ) {
            Icon(Icons.Default.Add, contentDescription = "add")
        }
    }
}

@Composable
fun TodoList(
    modifier: Modifier = Modifier,
    todoItems: List<Todo>,
    scrollState: LazyListState,
    onCheckCompleted: (Todo) -> Unit,
    onDeleteTodo: (Todo) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxHeight(),
        contentPadding = PaddingValues(horizontal = 8.dp),
        state = scrollState
    ) {
        items(todoItems, { it.num }) {
            SwipeableRow(
                todo = it,
                onCheckCompleted = onCheckCompleted,
                onDeleteTodo = onDeleteTodo,
                modifier = Modifier.animateItemPlacement(animationSpec = tween(durationMillis = 300))
            )
        }
    }
}


@Composable
fun SwipeableRow(
    todo: Todo,
    onCheckCompleted: (Todo) -> Unit,
    onDeleteTodo: (Todo) -> Unit,
    modifier: Modifier = Modifier
) {

    val dismissState = rememberDismissState()
    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
        onDeleteTodo(todo)
    }

    SwipeToDismiss(
        modifier = modifier,
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss

            val backgroundAlpha by animateFloatAsState(
                dismissState.progress.fraction.let {
                    if (it <= 0.99f) it else 0f
                }
            )

            val iconAlpha by animateFloatAsState(
                if (dismissState.progress.fraction <= 0.99f) 1f else 0f
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFA8072).copy(alpha = backgroundAlpha))
                    .padding(end = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "delete",
                    modifier = Modifier.alpha(iconAlpha)
                )
            }
        },
        dismissContent = {
            TodoRow(todo = todo, onCheckCompleted = onCheckCompleted)
        }
    )
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
        shape = RoundedCornerShape(16.dp)
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