package com.example.chatapp

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import io.ktor.client.engine.cio.CIO
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope

@Composable
fun ChatScreen() {
    // State to hold messages, message input, WebSocket client, and session
    val messages = remember { mutableStateListOf<Pair<String, Boolean>>() }
    var messageInput by remember { mutableStateOf("") }
    var client: HttpClient? by remember { mutableStateOf(null) }
    val session: MutableState<DefaultClientWebSocketSession?> = remember { mutableStateOf(null) } // MutableState for session

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Establish WebSocket connection when the Composable is first launched
    LaunchedEffect(Unit) {
        // Initialize WebSocket client
        client = HttpClient(CIO) {
            install(WebSockets)
        }

        connectToWebSocket(client, messages, session, listState, coroutineScope)
    }

    // UI for the chat screen
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // LazyColumn to display the list of messages
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 60.dp)  // Padding to prevent overlapping with TextField
        ) {
            items(messages) { (message, isSentByMe) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = if (isSentByMe) Arrangement.End else Arrangement.Start
                ) {
                    Text(
                        text = if (isSentByMe) "You: $message" else "Friend: $message",
                        color = if (isSentByMe) Color.Blue else Color.Black,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        // TextField for user input
        TextField(
            value = messageInput,
            onValueChange = { messageInput = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(Alignment.BottomCenter),
            label = { Text("Type a message") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (messageInput.isNotBlank()) {
                        Log.d("WebSocket", "Sending message: $messageInput")

                        // Send the message through WebSocket if the session is active
                        sendMessage(session.value, messageInput, coroutineScope)

                        // Add the sent message to the list
                        messages.add(Pair(messageInput, true))  // true for the user
                        messageInput = ""  // Clear the input field after sending

                        // Scroll to the bottom after sending
                        coroutineScope.launch {
                            listState.animateScrollToItem(messages.size - 1)
                        }
                    } else {
                        Log.d("WebSocket", "Attempted to send an empty message.")
                    }
                }
            )
        )
    }
}

fun connectToWebSocket(
    client: HttpClient?,
    messages: MutableList<Pair<String, Boolean>>,
    session: MutableState<DefaultClientWebSocketSession?>,  // Corrected: MutableState<DefaultClientWebSocketSession?>
    listState: LazyListState,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        try {
            client?.webSocket(
                method = HttpMethod.Get,
                host = "192.168.1.38",  // Replace with your WebSocket server IP
                port = 8081,
                path = "/ws"
            ) {
                session.value = this  // Save the session reference to the MutableState
                Log.d("WebSocket", "Connected to WebSocket server")

                // Send a greeting message to the server once connected
                session.value?.send(Frame.Text("Hello Server!"))
                Log.d("WebSocket", "Sent greeting message: Hello Server!")

                // Handle incoming messages from the WebSocket server
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val receivedMessage = frame.readText()
                        Log.d("WebSocket", "Received message: $receivedMessage")

                        // Add the received message to the messages list
                        launch(Dispatchers.Main) {
                            messages.add(Pair(receivedMessage, false))  // false for messages from friend
                            // Scroll to the bottom when a new message arrives
                            coroutineScope.launch {
                                listState.animateScrollToItem(messages.size - 1)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("WebSocket", "Error connecting to WebSocket: ${e.message}")
        }
    }
}

fun sendMessage(
    session: DefaultClientWebSocketSession?,
    messageInput: String,
    coroutineScope: CoroutineScope
) {
    session?.let { currentSession ->
        coroutineScope.launch(Dispatchers.IO) {
            try {
                currentSession.send(Frame.Text(messageInput))  // Send message
                Log.d("WebSocket", "Message sent: $messageInput")
            } catch (e: Exception) {
                Log.e("WebSocket", "Error sending message: ${e.message}")
            }
        }
    } ?: Log.d("WebSocket", "WebSocket session not initialized.")
}
