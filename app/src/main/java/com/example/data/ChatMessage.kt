package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val screenId: String,       // identifier for one of the 12 screens
    val text: String,           // text content of the message
    val isUser: Boolean,        // true if user sent, false if AI response
    val timestamp: Long = System.currentTimeMillis(),
    val imagePath: String? = null // local simulated path if image was uploaded
)
