package com.example.todo_app

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

// ── Design tokens ──────────────────────────────────────────────────────────────
private val BgDeep      = Color(0xFF0D1117)
private val BgSurface   = Color(0xFF161B22)
private val BgCard      = Color(0xFF1C2128)
private val BgCardAlt   = Color(0xFF21262D)
private val AccentGreen = Color(0xFF3FB950)
private val AccentBlue  = Color(0xFF58A6FF)
private val TextHigh    = Color(0xFFE6EDF3)
private val TextMid     = Color(0xFF8B949E)
private val TextLow     = Color(0xFF484F58)
private val DangerRed   = Color(0xFFF85149)
private val BorderColor = Color(0xFF30363D)

@Composable
fun Todo_App(viewModel: TodoViewModel) {
    val todos by viewModel.todos.observeAsState(emptyList())
    var inputText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val pendingCount = todos.count { !it.isCompleted }
    val completedCount = todos.count { it.isCompleted }
    val hasCompleted = completedCount > 0

    fun submitTodo() {
        viewModel.addTodo(inputText)
        inputText = ""
        focusManager.clearFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            // ── Header ─────────────────────────────────────────────────────────
            Spacer(Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Tasks",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextHigh,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = if (pendingCount == 0 && todos.isEmpty()) "Nothing here yet"
                        else if (pendingCount == 0) "All done! 🎉"
                        else "$pendingCount open  ·  $completedCount done",
                        fontSize = 13.sp,
                        color = TextMid
                    )
                }

                // Clear completed button
                AnimatedVisibility(visible = hasCompleted) {
                    TextButton(
                        onClick = { viewModel.clearCompleted() },
                        colors = ButtonDefaults.textButtonColors(contentColor = DangerRed)
                    ) {
                        Text("Clear done", fontSize = 13.sp)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Progress bar ───────────────────────────────────────────────────
            if (todos.isNotEmpty()) {
                val progress = if (todos.isEmpty()) 0f
                else completedCount.toFloat() / todos.size.toFloat()

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Progress",
                            fontSize = 11.sp,
                            color = TextLow,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            fontSize = 11.sp,
                            color = if (progress == 1f) AccentGreen else TextMid,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = AccentGreen,
                        trackColor = BorderColor
                    )
                }

                Spacer(Modifier.height(24.dp))
            }

            // ── Input field ────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(BgSurface)
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            "What needs to be done?",
                            color = TextLow,
                            fontSize = 14.sp
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { submitTodo() }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = TextHigh,
                        unfocusedTextColor = TextHigh,
                        cursorColor = AccentBlue
                    ),
                    modifier = Modifier.weight(1f)
                )

                val canAdd = inputText.isNotBlank()
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(if (canAdd) AccentGreen else BgCardAlt)
                        .clickable(enabled = canAdd) { submitTodo() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        fontSize = 24.sp,
                        color = if (canAdd) Color.White else TextLow,
                        fontWeight = FontWeight.Light
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Task list ──────────────────────────────────────────────────────
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                val pending = todos.filter { !it.isCompleted }
                val completed = todos.filter { it.isCompleted }

                if (pending.isNotEmpty()) {
                    item {
                        SectionLabel("Open")
                        Spacer(Modifier.height(8.dp))
                    }
                    items(pending, key = { it.id }) { todo ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + expandVertically()
                        ) {
                            TodoCard(
                                todo = todo,
                                onToggle = { viewModel.toggleComplete(todo) },
                                onDelete = { viewModel.deleteTodo(todo.id) }
                            )
                        }
                    }
                }

                if (completed.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(12.dp))
                        SectionLabel("Completed")
                        Spacer(Modifier.height(8.dp))
                    }
                    items(completed, key = { it.id }) { todo ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + expandVertically()
                        ) {
                            TodoCard(
                                todo = todo,
                                onToggle = { viewModel.toggleComplete(todo) },
                                onDelete = { viewModel.deleteTodo(todo.id) }
                            )
                        }
                    }
                }
            }
        }

        // ── Empty state ────────────────────────────────────────────────────────
        if (todos.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(BgSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", fontSize = 32.sp, color = AccentGreen)
                }
                Spacer(Modifier.height(16.dp))
                Text("No tasks yet", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextHigh)
                Spacer(Modifier.height(6.dp))
                Text("Add one above to get started", fontSize = 14.sp, color = TextMid)
            }
        }
    }
}

@Composable
private fun SectionLabel(label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = TextLow,
            letterSpacing = 1.5.sp
        )
        Spacer(Modifier.width(8.dp))
        HorizontalDivider(color = BorderColor, thickness = 0.5.dp, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun TodoCard(
    todo: TodoData,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }
    val formattedDate = remember(todo.createdAt) {
        dateFormatter.format(Date.from(todo.createdAt))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(if (todo.isCompleted) AccentGreen else Color.Transparent)
                    .then(
                        if (!todo.isCompleted)
                            Modifier.background(Color.Transparent)
                        else Modifier
                    )
                    .clickable { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            if (todo.isCompleted) AccentGreen
                            else Color.Transparent
                        )
                        .run {
                            if (!todo.isCompleted)
                                this.then(
                                    Modifier.background(Color.Transparent)
                                        .clip(CircleShape)
                                )
                            else this
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (!todo.isCompleted) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            // Outlined circle
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(BorderColor)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(BgCard)
                                        .align(Alignment.Center)
                                )
                            }
                        }
                    } else {
                        Text("✓", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = todo.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (todo.isCompleted) TextLow else TextHigh,
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = formattedDate,
                    fontSize = 11.sp,
                    color = TextLow
                )
            }

            Spacer(Modifier.width(8.dp))

            // Delete
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(34.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_delete_24),
                    contentDescription = "Delete task",
                    tint = TextLow,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
    }
}