package com.example.todo_app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todo_app.MainApplication
import com.example.todo_app.TodoData
import com.example.todo_app.db.TodoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import com.example.todo_app.ui.theme.Todo_appTheme
class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: TodoDao =
        (application as MainApplication).database.todoDao()

    val todos: LiveData<List<TodoData>> = dao.getAllTodos()

    fun addTodo(title: String) {
        val trimmed = title.trim()
        if (trimmed.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertTodo(
                TodoData(title = trimmed, createdAt = Instant.now())
            )
        }
    }

    fun toggleComplete(todo: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateTodo(todo.copy(isCompleted = !todo.isCompleted))
        }
    }

    fun deleteTodo(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteTodoById(id)
        }
    }

    fun clearCompleted() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteCompletedTodos()
        }
    }
}