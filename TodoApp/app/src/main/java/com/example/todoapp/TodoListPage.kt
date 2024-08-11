@file:Suppress("UNUSED_ANONYMOUS_PARAMETER")

package com.example.todoapp

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodoListPage(viewModel: TodoViewModel) {
    val todoList by viewModel.todoList.observeAsState(emptyList())
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp), // Add padding to the left side
                value = inputText,
                onValueChange = { inputText = it }
            )
            Button(
                onClick = {
                    if (inputText.isEmpty()) {
                        Toast.makeText(context, "Text field is empty", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.addTodo(inputText)
                        inputText = ""
                    }
                },
                modifier = Modifier.padding(start = 16.dp, end = 5.dp)
            ) {
                Text(text = "Add")
            }
        }
        if (todoList.isNotEmpty()) {
            LazyColumn {
                itemsIndexed(todoList) { index, item ->
                    TodoItem(
                        item = item,
                        onDelete = { viewModel.deleteTodo(item.id) },
                        onUpdate = { newTitle -> viewModel.updateTodo(item.id, newTitle) }
                    )
                }
            }
        } else {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                textAlign = TextAlign.Center,
                text = "No items yet",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun TodoItem(item: Todo, onDelete: () -> Unit, onUpdate: (String) -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf(item.title) }
    val customFontFamily = FontFamily(Font(R.font.helvetica))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color = Color.Blue)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Distributes space between children
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = SimpleDateFormat("HH:mm:aa, dd/MM", Locale.ENGLISH).format(item.createdAt),
                style = TextStyle(
                    color = Color.LightGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(5.dp))

            if (isEditing) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        unfocusedLeadingIconColor = Color.White
                    ),
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Edit Title") },
                    modifier = Modifier.fillMaxWidth() // You might want to add a modifier
                )

                Spacer(modifier = Modifier.height(8.dp))
                IconButton(onClick = {
                    onUpdate(newTitle)
                    isEditing = false
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_check_24), // Assuming check icon for update
                        contentDescription = "Save",
                        tint = Color.White
                    )
                }
            } else {
                Text( modifier = Modifier.padding(top = 5.dp),
                    text = item.title,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = customFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Row for edit and delete buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { isEditing = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_edit_24),
                            contentDescription = "Edit",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onDelete) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_delete_24),
                            contentDescription = "Delete",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
