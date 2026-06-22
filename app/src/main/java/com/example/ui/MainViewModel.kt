package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ChatDatabase
import com.example.data.ChatMessage
import com.example.data.ChatRepository
import com.example.data.GeminiService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class Screen(val id: String, val title: String, val iconName: String) {
    object Home : Screen("home", "Trang chủ", "home")
    object Citizen : Screen("citizen", "AI Công dân", "gavel")
    object Health : Screen("health", "AI Sức khỏe", "vaccines")
    object Tourism : Screen("tourism", "AI Du lịch", "map")
    object Crime : Screen("crime", "AI Tố giác vi phạm", "security")
    object Field : Screen("field", "AI Phản ánh hiện trường", "photo_camera")
    object Coffee : Screen("coffee", "AI Cà phê BMT", "coffee")
    object Agriculture : Screen("agriculture", "AI Nông nghiệp", "agriculture")
    object Forest : Screen("forest", "AI Bảo vệ rừng", "park")
    object Culture : Screen("culture", "AI Văn hóa Tây Nguyên", "music_note")
    object Camera : Screen("camera", "AI Camera thông minh", "videocam")
    object Planning : Screen("planning", "AI Quy hoạch", "layers")
    object Alert : Screen("alert", "AI Cảnh báo thông minh", "warning")
    object AdminDashboard : Screen("admin_dashboard", "Admin Web Dashboard (React)", "dashboard")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ChatRepository
    
    // Screens navigation backstack
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Query field for prompt inputs
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Current screen chat messages
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    // Loading/Writing indicator
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Voice simulation states
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()
    private val _voiceTimer = MutableStateFlow(0)
    val voiceTimer: StateFlow<Int> = _voiceTimer.asStateFlow()

    // Image upload dialog simulation states
    private val _uploadedImagePath = MutableStateFlow<String?>(null)
    val uploadedImagePath: StateFlow<String?> = _uploadedImagePath.asStateFlow()

    private var activeChatJob: Job? = null
    private var dbCollectJob: Job? = null
    private var voiceJob: Job? = null

    init {
        val database = ChatDatabase.getDatabase(application)
        repository = ChatRepository(database.chatDao())
        
        // Listen to changes in screen and collect appropriate messages
        viewModelScope.launch {
            _currentScreen.collect { screen ->
                dbCollectJob?.cancel()
                _uploadedImagePath.value = null
                _searchQuery.value = ""
                
                dbCollectJob = viewModelScope.launch {
                    repository.getMessagesForScreen(screen.id).collect { list ->
                        _messages.value = list
                    }
                }
            }
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun handleBack(): Boolean {
        return if (_currentScreen.value != Screen.Home) {
            _currentScreen.value = Screen.Home
            true
        } else {
            false
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun sendMessage() {
        val promptText = _searchQuery.value.trim()
        val currentSc = _currentScreen.value
        if (promptText.isEmpty() && _uploadedImagePath.value == null) return

        val finalPrompt = if (promptText.isEmpty() && _uploadedImagePath.value != null) {
            "Gửi hình ảnh đính kèm phân tích thực tế."
        } else {
            promptText
        }

        _searchQuery.value = ""
        val attachedImage = _uploadedImagePath.value
        _uploadedImagePath.value = null

        viewModelScope.launch {
            // Save User message
            val userMsg = ChatMessage(
                screenId = currentSc.id,
                text = finalPrompt,
                isUser = true,
                imagePath = attachedImage
            )
            repository.insertMessage(userMsg)

            _isLoading.value = true
            
            // Call Gemini/Fallback service
            val response = GeminiService.generateResponse(currentSc.id, finalPrompt)

            // Save AI message
            val aiMsg = ChatMessage(
                screenId = currentSc.id,
                text = response,
                isUser = false
            )
            repository.insertMessage(aiMsg)

            _isLoading.value = false
        }
    }

    // --- Simulation of Voice Input ---
    fun toggleVoiceRecording() {
        if (_isRecording.value) {
            // Stop recording, populate query with custom screen specific audio transcription
            _isRecording.value = false
            voiceJob?.cancel()
            val transcriptText = getVoiceTranscriptForScreen(_currentScreen.value.id)
            _searchQuery.value = transcriptText
        } else {
            // Start recording
            _isRecording.value = true
            _voiceTimer.value = 0
            voiceJob = viewModelScope.launch {
                while (_isRecording.value) {
                    delay(1000)
                    _voiceTimer.value += 1
                    if (_voiceTimer.value >= 6) { // Auto stop after 6 seconds
                        toggleVoiceRecording()
                    }
                }
            }
        }
    }

    // --- Simulation of Upload Image Selection ---
    fun setSimulatedUploadedImage(imageName: String) {
        _uploadedImagePath.value = imageName
    }

    fun clearUploadedImage() {
        _uploadedImagePath.value = null
    }

    fun clearChatHistory() {
        viewModelScope.launch {
            repository.clearHistoryForScreen(_currentScreen.value.id)
        }
    }

    private fun getVoiceTranscriptForScreen(screenId: String): String {
        return when (screenId) {
            "citizen" -> "Thủ tục xin cấp giấy phép xây dựng nhà ở riêng lẻ tại Đắk Lắk gồm những gì?"
            "health" -> "Các triệu chứng nhận biết sớm sốt xuất huyết ở trẻ nhỏ là gì?"
            "tourism" -> "Gợi ý cho tôi địa chỉ thưởng thức ẩm thực cơm lam gà nướng ngon nhất Buôn Ma Thuột."
            "crime" -> "Tôi phát hiện có nhóm người nghi ngờ đang buôn lậu gỗ tại hạt kiểm lâm."
            "field" -> "Nước thải từ nhà máy chế biến xả trực tiếp ra dòng suối Ea Kao đục ngầu."
            "coffee" -> "Cách pha chế cà phê robusta ngon đậm đà bằng phin nhôm chuẩn hương vị Buôn Ma Thuột?"
            "agriculture" -> "Làm cách nào trị nấm cổ rễ và nấm hồng hạt sương cho vườn sầu riêng Dona mùa khô?"
            "forest" -> "Phát hiện khói bốc lên tại tiểu khu rừng đặc dụng hạt kiểm lâm Yok Don."
            "culture" -> "Kể cho tôi nghe các trường ca sử thi cổ của nam bộ người Êđê."
            "camera" -> "Lưu lượng xe đang dồn ứ rất đông tại điểm nút giao ngã sáu Buôn Ma Thuột."
            "planning" -> "Khi nào dự án tuyến cao tốc Buôn Ma Thuột - Khánh Hòa chính thức thông xe?"
            "alert" -> "Cập nhật cảnh báo sạt lở tại đèo dốc M'Đrắk hôm nay mới nhất."
            else -> "Xin chào trợ lý Đắk Lắk AI"
        }
    }
}
