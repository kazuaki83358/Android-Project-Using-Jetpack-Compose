package com.example.chatapp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import io.ktor.client.engine.cio.CIO
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction

@Composable
fun ChatScreen() {
    val messages = remember { mutableStateListOf<Pair<String, Boolean>>() }
    var messageInput by remember { mutableStateOf("") }
    var client: HttpClient? by remember { mutableStateOf(null) }
    var session: DefaultClientWebSocketSession? by remember { mutableStateOf(null) }

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        client = HttpClient(CIO) {
            install(WebSockets)
        }

        client?.webSocket(
            method = HttpMethod.Get,
            host = "192.168.1.38",  // Replace with your WebSocket server IP
            port = 8081,
            path = "/ws"
        ) {
            session = this

            session?.send(Frame.Text("Hello Server!"))

            for (frame in session!!.incoming) {
                try {
                    if (frame is Frame.Text) {
                        val receivedMessage = frame.readText()
                        launch(Dispatchers.Main) {
                            messages.add(Pair(receivedMessage, false)) // false for messages from friend
                            coroutineScope.launch {
                                listState.animateScrollToItem(messages.size - 1)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("WebSocket", "Error receiving message: ${e.message}")
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top header: Profile picture and name
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Gray)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Welcome to my chat", style = MaterialTheme.typography.headlineMedium)
            }

            // Spacer to add space between header and messages
            Spacer(modifier = Modifier.height(16.dp))

            // LazyColumn to display the list of messages
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)  // Ensure the list takes remaining space
                    .padding(bottom = 80.dp)  // Padding to prevent overlapping with TextField
            ) {
                items(messages) { (message, isSentByMe) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = if (isSentByMe) Arrangement.End else Arrangement.Start
                    ) {
                        // Message bubble
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSentByMe) Color(0xFFDCF8C6) else Color(0xFFF0F0F0),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Text(
                                text = message,
                                color = Color.Black
                            )
                        }

                        if (isSentByMe) {
                            // Wrap the timestamp in a Box and align it at the bottom end
                            Box(modifier = Modifier.fillMaxWidth().padding(end = 16.dp)) {
                                Text(
                                    text = "12:00 PM", // You can set actual timestamp here
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                    modifier = Modifier.align(Alignment.BottomEnd) // Align at the bottom end
                                )
                            }
                        }
                    }
                }
            }

            // Message input area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                TextField(
                    value = messageInput,
                    onValueChange = { messageInput = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    label = { Text("Type a message") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            // Prevent sending empty messages
                            if (messageInput.isNotBlank()) {
                                session?.let { currentSession ->
                                    coroutineScope.launch(Dispatchers.IO) {
                                        try {
                                            currentSession.send(Frame.Text(messageInput)) // Send message
                                            // Add message to list (true for sent by me)
                                            messages.add(Pair(messageInput, true))
                                            messageInput = "" // Clear the input field after sending
                                        } catch (e: Exception) {
                                            Log.e("WebSocket", "Error sending message: ${e.message}")
                                        }
                                    }
                                }
                            }
                        }
                    )
                )

                IconButton(
                    onClick = {
                        // Prevent sending empty messages
                        if (messageInput.isNotBlank()) {
                            session?.let { currentSession ->
                                coroutineScope.launch(Dispatchers.IO) {
                                    try {
                                        currentSession.send(Frame.Text(messageInput)) // Send message
                                        // Add message to list (true for sent by me)
                                        messages.add(Pair(messageInput, true))
                                        messageInput = "" // Clear the input field after sending
                                    } catch (e: Exception) {
                                        Log.e("WebSocket", "Error sending message: ${e.message}")
                                    }
                                }
                            }
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
                }
            }
        }
    }
}
