package com.thk.data

import com.thk.domain.Todo

fun mapperToTodo(todoRow: TodoRow) = Todo(
    num = todoRow.num,
    content = todoRow.content,
    isCompleted = todoRow.isCompleted
)

fun mapperToTodoRow(todo: Todo) = TodoRow(
    num = todo.num,
    content = todo.content,
    isCompleted = todo.isCompleted
)