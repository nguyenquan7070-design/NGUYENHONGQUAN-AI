package com.example.data

import kotlinx.coroutines.flow.Flow

class ChatRepository(private val chatDao: ChatDao) {
    fun getMessagesForScreen(screenId: String): Flow<List<ChatMessage>> {
        return chatDao.getMessagesByScreen(screenId)
    }

    suspend fun insertMessage(message: ChatMessage): Long {
        return chatDao.insertMessage(message)
    }

    suspend fun clearHistoryForScreen(screenId: String) {
        chatDao.clearHistoryByScreen(screenId)
    }

    suspend fun deleteMessage(id: Long) {
        chatDao.deleteMessageById(id)
    }
}
