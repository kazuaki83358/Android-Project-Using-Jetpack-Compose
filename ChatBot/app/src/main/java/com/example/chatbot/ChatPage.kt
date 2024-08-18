package com.example.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatbot.model.MessageModel
import com.example.chatbot.model.ChatViewModel
import com.example.chatbot.ui.theme.Grey
import com.example.chatbot.ui.theme.HomeBackground
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbot.ui.theme.Model
import com.example.chatbot.ui.theme.Purple80
import com.example.chatbot.ui.theme.User

@Composable
fun ChatPage(modifier: Modifier = Modifier, viewModel: ChatViewModel = viewModel()) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HomeBackground)
    ) {
        AppBar()
        // Ensure padding at the bottom for the MessageList
        MessageList(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp), // Ensure padding
            messageList = viewModel.messageList
        )
        MessageInput(onMessageSend = {
            viewModel.sendMessage(it)
        })
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, messageList: List<MessageModel>) {
    if(messageList.isEmpty()){
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = R.drawable.baseline_message_24),
                contentDescription = "Icon",
                tint = com.example.chatbot.ui.theme.AppBar,
            )
            Text(text = "Ask me anything",style = TextStyle(
                fontSize = 16.sp,
                color = Color.White
            ))
        }
    }else{
        LazyColumn(
            modifier = modifier,
            reverseLayout = true
        ) {
            items(messageList.reversed()){
                MessageRow(messageModel = it)
            }
        }
    }

}

@Composable
fun MessageRow(messageModel: MessageModel) {
    val isModel = messageModel.role == "model"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd)
                    .padding(
                        start = if (isModel) 8.dp else 70.dp,
                        end = if (isModel) 70.dp else 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    .clip(RoundedCornerShape(38.dp))
                    .background(if (isModel) Model else User)
                    .padding(16.dp)
            ) {
                Text(
                    text = messageModel.message,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(onMessageSend: (String) -> Unit) {
    val send = painterResource(id = R.drawable.baseline_send_24)
    var message by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.White)
            .clip(RoundedCornerShape(18.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            value = message,
            onValueChange = { message = it },
            shape = RoundedCornerShape(28.dp), // Sets the rounded corners
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Grey, // Background color
                focusedIndicatorColor = Grey, // Color of the underline when focused
                unfocusedIndicatorColor = Grey.copy(alpha = 0.5f), // Color of the underline when not focused
                cursorColor = Grey // Color of the cursor
            )
        )

        IconButton(onClick = {
            if (message.isNotBlank()) {
                onMessageSend(message)
                message = "" // Clear the message after sending
            }
        }) {
            Icon(
                painter = send,
                contentDescription = "Send",
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun AppBar() {
    val customFont = FontFamily(Font(R.font.helvetica))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(com.example.chatbot.ui.theme.AppBar),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Chat Bot",
            fontSize = 24.sp,
            style = TextStyle(
                color = Color.LightGray,
                fontWeight = FontWeight.Bold,
                fontFamily = customFont
            ),
            modifier = Modifier.padding(10.dp)
                .padding(top = 15.dp)
        )
    }
}

// Preview function updates
@Preview(showBackground = true)
@Composable
fun ChatPagePreview() {
    // Provide a mock ViewModel or use an empty list for previews
    ChatPage(viewModel = viewModel())
}

@Preview(showBackground = true)
@Composable
fun MessageGetInputPreview() {
    MessageInput(onMessageSend = {})
}

@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
    AppBar()
}
