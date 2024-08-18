package com.example.chatbot.model

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }
    private val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = Constants.apiKey
    )

    fun sendMessage(question: String) {
        viewModelScope.launch {
                // Start the chat session and get the response
           try {
               val chat = generativeModel.startChat(
                   history = messageList.map {
                       content(it.role) {text(it.message)}
                   }.toList()
               )
               messageList.add(MessageModel(question,"user"))
               messageList.add(MessageModel("Typing..","model"))
               val response = chat.sendMessage(question)
               messageList.removeLast()
               messageList.add(MessageModel(response.text.toString(),"model"))
               Log.i("Response",response.text.toString())
           }catch (e: Exception){
               messageList.removeLast()
               messageList.add(MessageModel("Error: "+e.message.toString(),"model"))
           }

        }
    }
}
