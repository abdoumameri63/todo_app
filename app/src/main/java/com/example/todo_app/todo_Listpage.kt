package com.example.todo_app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.ui.theme.TodoViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Todo_App(viewModel: TodoViewModel) {

    val todolist by viewModel.todolist.observeAsState(emptyList())
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 30.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {
                    if (inputText.isNotEmpty()) {
                        viewModel.addTodo(inputText)
                        inputText = ""
                    }
                }
            ) {
                Text(text = "ADD")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            items(todolist) { item ->
                TodoItem(
                    item = item,
                    onDelete = { viewModel.deleteTodo(item.id) }
                )
            }

        }
    }
}

@Composable
fun TodoItem(item: todoData, onDelete: () -> Unit) {

    val date = Date.from(item.createAt)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(50.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {

                Text(
                    text = SimpleDateFormat(
                        "hh:mm a, dd MMM",
                        Locale.getDefault()
                    ).format(date),
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Text(
                    text = item.title,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

            }

            IconButton(onClick = onDelete) {

                Icon(
                    painter = painterResource(id = R.drawable.outline_delete_24),
                    contentDescription = "DELETE",
                    tint = Color.Black
                )

            }
        }
    }
}