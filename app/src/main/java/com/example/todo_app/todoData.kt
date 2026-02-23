package com.example.todo_app

import androidx.compose.runtime.Composable
import java.time.Instant

data class todoData(
    var id: Int,
    var title: String,
    var createAt: Instant
)

fun getFaketodo(): List<todoData>
{
   return listOf<todoData>(
        todoData(id = 1,
            title = "first project",
            createAt = Instant.now()
        ),
       todoData(id=2,
           title = "the second project",
           createAt = Instant.now()
       ),
       todoData(id=3,
           title="the third project",
           createAt= Instant.now()),


    )
}
