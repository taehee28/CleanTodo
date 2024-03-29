@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class, ExperimentalMaterialApi::class
)

package com.thk.cleantodo.ui

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.thk.cleantodo.R
import com.thk.cleantodo.ui.theme.CleanTodoTheme
import com.thk.cleantodo.util.logd
import com.thk.domain.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

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
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "To-do List", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
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
    val isScroll by remember { derivedStateOf { scrollState.isScrollInProgress } }

    // State로 remember 해서 해당 값을 읽는 Row들이 영향을 받도록
    var inSwipeItemIndex: Int by remember { mutableStateOf(-1) }

    LazyColumn(
        modifier = modifier.fillMaxHeight(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        state = scrollState
    ) {
        itemsIndexed(todoItems, { _, item -> item.num }) { index, item ->
            val swipeableState = rememberSwipeableState(initialValue = 0)
            val scope = rememberCoroutineScope()

            // Row가 왼쪽으로 움직이면 true, 나머지는 false
            val isSwipeProgress by remember { derivedStateOf { swipeableState.direction == -1.0f } }
            val isSwiped by remember { derivedStateOf { swipeableState.currentValue == 1 } }

            logd(">>>>>>>>>>>> $index")

            LaunchedEffect(isSwipeProgress) {
                if (isSwipeProgress) {
                    logd("isSwipeProgress $index")
                    inSwipeItemIndex = index
                }
            }


            // FIXME: inSwipeItemIndex가 State라서 값이 바뀌면 recomposition이 발생해버림. 
            //        값이 바뀌었을 때 recompositoin이 발생하지 않으면서
            //        모든 Row의 LaunchedEffect가 실행되도록 해야 함...
            // index 값이 변경되면 화면에 보이는 모든 Row에서 해당 Effect가 실행됨
            LaunchedEffect(inSwipeItemIndex) {
                if (isSwiped || isSwipeProgress) {
                    // 현재 swipe되었거나, swipe중인 상태라면 원래 위치로 animate
                    swipeableState.animateTo(0)
                }
            }

            LaunchedEffect(isScroll) {
                if (isScroll && isSwiped) {
                    swipeableState.animateTo(0)
                }
            }

            SwipeableRow(
                state = swipeableState,
                modifier = Modifier.animateItemPlacement(animationSpec = tween(durationMillis = 300)),
                backgroundMenu = {
                    MenuButton(
                        onClick = {
                            scope.launch { swipeableState.animateTo(0) }
                            onEditStart(item)
                        },
                        backgroundColor = Color(0xFFC4DEFF)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_edit),
                            "edit",
                            Modifier.scale(0.8f)
                        )
                    }

                    MenuButton(
                        onClick = { onDeleteTodo(item) },
                        backgroundColor = Color(0xFFFFB9B9)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_delete),
                            "delete",
                            Modifier.scale(0.8f)
                        )
                    }
                }
            ) { offsetProvider ->
                TodoRow(
                    todo = item,
                    onCheckCompleted = onCheckCompleted,
                    modifier = Modifier.offset { IntOffset(offsetProvider(), 0) }
                )
            }
        }
    }
}