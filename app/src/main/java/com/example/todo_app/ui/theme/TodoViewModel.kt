package com.example.todo_app.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo_app.todoData
class TodoViewModel : ViewModel() {

    private var _todolist = MutableLiveData<List<todoData>>()
    val todolist: LiveData<List<todoData>> = _todolist

    init {
        gettAll_Todo()
    }

    fun gettAll_Todo() {
        _todolist.value = Todo_object.getAllToDo()
    }

    fun addTodo(title: String) {
        Todo_object.addToDo(title)
        gettAll_Todo()
    }

    fun deleteTodo(id: Int) {
        Todo_object.deleteTodo(id)
        gettAll_Todo()
    }
}