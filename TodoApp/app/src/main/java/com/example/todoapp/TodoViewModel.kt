package com.example.todoapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date

open class TodoViewModel : ViewModel() {

    private val todoDao = MainApplication.todoDatabase.getTodoDao()
    open val todoList: LiveData<List<Todo>> = todoDao.getAllTodo()

    // Add a new Todo
    @RequiresApi(Build.VERSION_CODES.O)
    open fun addTodo(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.addTodo(
                Todo(
                    title = title,
                    createdAt = Date.from(Instant.now()) // Current time
                )
            )
        }
    }

    // Delete a Todo by its ID
    @RequiresApi(Build.VERSION_CODES.O)
    open fun deleteTodo(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.deleteTodo(id)
        }
    }

    // Update a Todo's title
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateTodo(id: Int, newTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val todo = todoDao.getTodoById(id)
            if (todo != null) {
                todo.title = newTitle
                todoDao.updateTodo(todo)
            }
        }
    }
}

