@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

package com.thk.cleantodo.ui

/**
 * TodoScreen.kt에서 사용하는 ui 컴포넌트들
 */

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.thk.domain.Todo
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * 스와이프 가능한 Row
 *
 * @param state
 * @param modifier
 * @param backgroundMenu content 뒤에 나타날 버튼들을 표시하는 컴포저블. targetValue 값을 받아서
 * swipeableState.animateTo를 호출하는 람다를 인자로 받는다.
 * @param content 스와이프 될 컴포저블. offset을 인자로 받으며, content 안의 컴포저블에게
 * offset을 전달해서 스와이프 하는대로 움직일 수 있도록 해야한다.
 */
@Composable
fun SwipeableRow(
    state: SwipeableState<Int>,
    modifier: Modifier = Modifier,
    backgroundMenu: @Composable () -> Unit = {},
    content: @Composable (offsetProvider: () -> Int) -> Unit,
) {
//    val swipeableState = rememberSwipeableState(initialValue = 0)
    var rowSize by remember { mutableStateOf(IntSize.Zero) }
    val anchor = rowSize.width.toFloat()
    val anchors = mapOf(0f to 0, -anchor to 1)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 4.dp)
            .swipeable(
                state = state,
                orientation = Orientation.Horizontal,
                anchors = anchors
            )
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .onSizeChanged { rowSize = it }
        ) {
            backgroundMenu()
        }

        content {
            state.offset.value.let { if (it.isNaN()) 0 else it.roundToInt() }
        }
    }

}

/**
 * [SwipeableRow]의 backgroundMenu 컴포저블에서 사용하는 메뉴 버튼 컴포저블.
 *
 * @param onClick
 * @param backgroundColor
 * @param icon
 */
@Composable
fun MenuButton(
    onClick: () -> Unit,
    backgroundColor: Color = Color.Gray,
    icon: @Composable () -> Unit
) {
    androidx.compose.material.IconButton(
        onClick = onClick,
        modifier = Modifier
            .width(60.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
    ) {
        icon()
    }
}

@Composable
fun TodoRow(
    todo: Todo,
    onCheckCompleted: (Todo) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = {
                    onCheckCompleted(todo.copy(isCompleted = it))
                }
            )

            Text(
                text = todo.content,
                maxLines = 1,
                textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

    }
}

/**
 * **사용하지 않음**
 *
 * [SwipeToDismiss]로 구현한 스와이프 가능한 Row.
 * 스와이프했을 때 삭제되는 기능이 있음.
 */
@Composable
fun SwipeToDismissRow(
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
            )
        }
    )
}