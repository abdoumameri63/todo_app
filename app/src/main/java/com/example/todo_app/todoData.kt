package com.example.todo_app

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "todo_items")
data class TodoData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val createdAt: Instant,
    val isCompleted: Boolean = false
)
