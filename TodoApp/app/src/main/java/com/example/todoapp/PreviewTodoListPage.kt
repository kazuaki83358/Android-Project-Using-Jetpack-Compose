package com.example.todoapp
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.Date


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewTodoListPage() {
    // Mock data
    val sampleTodos = listOf(
        Todo(
            id = 1,
            title = "Sample Todo 1",
            createdAt = Date() // Use current date
        ),
        Todo(
            id = 2,
            title = "Sample Todo 2",
            createdAt = Date() // Use current date
        )
    )

    // Use mutableStateOf for mock data
    val todoListState = remember { mutableStateOf(sampleTodos) }

    // Mock ViewModel behavior with mutableStateOf
    TodoListPage(
        viewModel = object : TodoViewModel() {
            val mockLiveData = MutableLiveData(sampleTodos)
            override val todoList: LiveData<List<Todo>> = mockLiveData

            override fun addTodo(title: String) {
                // Mock behavior: Do nothing
            }

            override fun deleteTodo(id: Int) {
                // Mock behavior: Do nothing
            }
        }
    )
}
