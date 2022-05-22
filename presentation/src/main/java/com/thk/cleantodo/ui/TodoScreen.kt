@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class, ExperimentalMaterialApi::class
)

package com.thk.cleantodo.ui

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
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
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.focus.*
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
    onDeleteTodo: (Todo) -> Unit,
    onEditStart: (Todo) -> Unit,
    onEditDone: (Todo) -> Unit,
    currentEditTodo: Todo?
) {
    val todoItems by todoItemsFlow.collectAsState()
    val (input, setInput) = remember { mutableStateOf("")}
    var isEditMode by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

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
                        onAddNewTodo(input)

                        setInput("")
                        focusManager.clearFocus(true)
                    }
                },
                onEditButtonClick = {
                    if (input.trim().isNotEmpty()) {
                        currentEditTodo?.let { onEditDone(it.copy(content = input)) }

                        setInput("")
                        isEditMode = false
                        focusManager.clearFocus(true)
                    }
                },
                onFocusChanged = {
                    if (it.isFocused) {
                        scope.launch {
                            todoItems.lastIndex.let { index -> if (index > 0) scrollState.animateScrollToItem(index)}
                        }
                    }
                },
                focusRequester = focusRequester,
                isEditMode = isEditMode
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
            scrollState = scrollState,
            onEditStart = { todo ->
                onEditStart(todo)
                focusRequester.requestFocus()
                setInput(todo.content)
                isEditMode = true
            }
        )
    }
}

@Composable
@Preview
fun TodoScreenPreview()  {
    CleanTodoTheme {
        TodoScreen(MutableStateFlow(emptyList()), {}, {}, {}, {}, {}, Todo())
    }
}

@Composable
fun TodoInput(
    input: String,
    setInput: (String) -> Unit,
    onAddButtonClick: () -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    focusRequester: FocusRequester,
    isEditMode: Boolean,
    onEditButtonClick: () -> Unit,
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
                .focusRequester(focusRequester)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Button(
            onClick = {
                if (isEditMode) onEditButtonClick() else onAddButtonClick()
            }
        ) {
            if (isEditMode) {
                Icon(Icons.Default.Edit, contentDescription = "edit")
            } else {
                Icon(Icons.Default.Add, contentDescription = "add")
            }
        }
    }
}

@Composable
fun TodoList(
    modifier: Modifier = Modifier,
    todoItems: List<Todo>,
    scrollState: LazyListState,
    onCheckCompleted: (Todo) -> Unit,
    onDeleteTodo: (Todo) -> Unit,
    onEditStart: (Todo) -> Unit
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
                onEditStart = onEditStart,
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
    onEditStart: (Todo) -> Unit,
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
            TodoRow(
                todo = todo,
                onCheckCompleted = onCheckCompleted,
                onEditStart = onEditStart
            )
        }
    )
}

@Composable
fun TodoRow(
    todo: Todo,
    onCheckCompleted: (Todo) -> Unit,
    onEditStart: (Todo) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    onEditStart(todo)
                }
            ),
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