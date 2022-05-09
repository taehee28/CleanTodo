package com.thk.domain

data class Todo(
    val num: Int = 0,
    val content: String = "",
    val isCompleted: Boolean = false
)