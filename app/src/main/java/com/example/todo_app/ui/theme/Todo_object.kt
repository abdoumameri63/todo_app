package com.example.todo_app.ui.theme

import com.example.todo_app.todoData
import java.time.Instant
import java.util.Date

object Todo_object {

    private val todoList = mutableListOf<todoData>()

    fun getAllToDo(): List<todoData> {
        return todoList.toList()
    }

    fun addToDo(title: String) {
        todoList.add(
            todoData(
                System.currentTimeMillis().toInt(),
                title,
                java.time.Instant.now()
            )
        )
    }

    fun deleteTodo(id: Int) {
        todoList.removeIf { it.id == id }
    }
}