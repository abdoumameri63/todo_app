package com.example.todo_app

import android.graphics.pdf.models.ListItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat

@Composable
fun toDo_App(){
val todolist=getFaketodo()
    Text(text = todolist.toString())
    Column(modifier = Modifier.fillMaxSize().padding(10.dp))
    {
        LazyColumn (content = {
        itemsIndexed(todolist){
            index: Int, item: todoData->
        }

        })
    }



}
@Composable
fun TodoItem(item: todoData) {

    val date = java.util.Date.from(item.createAt)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(50.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            Text(
                text = SimpleDateFormat(
                    "hh:mm a, dd MMM",
                    java.util.Locale.getDefault()
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
    }


}