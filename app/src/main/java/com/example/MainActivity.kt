package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontStyle
import android.widget.Toast
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.ChatMessage
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: MainViewModel = viewModel()
                val currentScreen by viewModel.currentScreen.collectAsState()
                
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("app_root_scaffold")
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = {
                                if (targetState == Screen.Home) {
                                    (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                                        slideOutHorizontally { width -> width } + fadeOut())
                                } else {
                                    (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                                        slideOutHorizontally { width -> -width } + fadeOut())
                                }
                            },
                            label = "screen_transition"
                        ) { screen ->
                            when (screen) {
                                Screen.Home -> HomeScreen(viewModel)
                                Screen.AdminDashboard -> AdminDashboardScreen(viewModel)
                                Screen.Camera -> CameraDashboardScreen(viewModel)
                                else -> ChatAIScreen(viewModel, screen)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Custom sample images simulating Western Highland scenarios in Đắk Lắk
data class SampleImage(val name: String, val vietnameseName: String, val emoji: String, val description: String)
val sampleElevatedImages = listOf(
    SampleImage("coffee_bean", "Cà phê Buôn Ma Thuột", "☕", "Cà phê nhân và hạt mộc đất đỏ bazan."),
    SampleImage("durian_leaf", "Lá Sầu riêng Dona", "🍃", "Khảo sát sâu bệnh rầy lá trên vườn tại Krông Pắc."),
    SampleImage("forest_smoke", "Khói cháy tại Yok Don", "🔥", "Cảnh báo sớm khói bốc lên tại phân khu 12."),
    SampleImage("citizen_id", "Mẫu Căn cước công dân", "🪪", "Thủ tục làm hồ sơ lưu trú hành chính số."),
    SampleImage("road_pothole", "Gạch đá, ổ gà hư hỏng", "🚧", "Phản ánh mặt đường sụt lún tại ngã sáu đô thị."),
    SampleImage("ethnic_gong", "Cồng chiêng cổ Tây Nguyên", "🥁", "Tích hợp nhận dạng hoa văn nhạc cụ dân tộc.")
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val scrollState = rememberLazyListState()
    
    LazyColumn(
        state = scrollState,
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen_column")
    ) {
        // High-fidelity header representing Đắk Lắk Smart City & AI
        item {
            GovernmentHeroHeader()
        }

        // System Health & Local Weather Quick Cards
        item {
            LocalInfoIndicators()
        }

        // Title for 12 Core Smart Features
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 4.dp, height = 20.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "DỊCH VỤ CỘNG ĐỒNG AI (12 MỤC)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // 12 Functions displayed in a beautiful grid (represented as list items of rows dynamically)
        val allScreens = listOf(
            Screen.Citizen, Screen.Health, Screen.Tourism,
            Screen.Crime, Screen.Field, Screen.Coffee,
            Screen.Agriculture, Screen.Forest, Screen.Culture,
            Screen.Camera, Screen.Planning, Screen.Alert
        )

        val chunkedScreens = allScreens.chunked(3)
        items(chunkedScreens) { rowScreens ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (screen in rowScreens) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        FunctionGridCard(
                            screen = screen,
                            onClick = { viewModel.navigateTo(screen) }
                        )
                    }
                }
                // Handle cases where chunk length is less than 3
                if (rowScreens.size < 3) {
                    for (i in 0 until (3 - rowScreens.size)) {
                        Spacer(modifier = Modifier.weight(1f).padding(horizontal = 4.dp))
                    }
                }
            }
        }

        // Firebase Firestore Scheme and Interactive Testing Center
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 4.dp, height = 20.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colorScheme.tertiary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CỔNG ĐIỀU HÀNH & GIÁM SÁT TRỰC TUYẾN",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            // React-Inspired Admin Portal Entry Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
                ),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Dashboard,
                                contentDescription = "Admin icon",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Admin Web Dashboard (React Engine)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Hệ thống kiểm soát đồng bộ 12 dịch vụ AI & Cơ sở dữ liệu Cloud Firestore trực tiếp",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Simple simulated dynamic system counters
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("Firestore Live", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("12 Collections", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("Trạng thái", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Hoạt động tốt", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                            }
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("Phản hồi AI", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("< 1.8 giây", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = { viewModel.navigateTo(Screen.AdminDashboard) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_dashboard_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Icon(Icons.Default.Login, contentDescription = "Đăng nhập")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Truy cập Hệ Thống Điều Hành Admin", fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            FirestoreDatabaseHub()
        }

        // Dynamic Citizen Charter Footer
        item {
            Spacer(modifier = Modifier.height(24.dp))
            GovernmentFooter()
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun GovernmentHeroHeader() {
    val primaryColor = MaterialTheme.colorScheme.primary
    val context = LocalContext.current
    val customBgId = remember(context) {
        context.resources.getIdentifier("background", "drawable", context.packageName)
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0C2440), // Earthy night blue start of sunset
                        Color(0xFF8A3D12), // Deep copper horizon
                        Color(0xFFFF9100), // Bright golden sunset glow
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        if (customBgId != 0) {
            Image(
                painter = painterResource(id = customBgId),
                contentDescription = "Background Buôn Ma Thuột",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Soft overlay gradient at the bottom for readability of the text
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                        )
                    )
            )
        } else {
            // Beautiful Buôn Ma Thuột Roundabout Silhouette and Sunset Sun Glow painting
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                
                // 1. Draw direct sunset sun light source
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFFF7C2), Color(0xFFFF9E1B).copy(alpha = 0.7f), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(w * 0.75f, h * 0.35f),
                        radius = w * 0.35f
                    ),
                    radius = w * 0.35f,
                    center = androidx.compose.ui.geometry.Offset(w * 0.75f, h * 0.45f)
                )

                // 2. Draw Buôn Ma Thuột city silhouette overlay (landmark buildings, towers, and palm trees)
                val cityPath = Path().apply {
                    moveTo(0f, h * 0.88f)
                    // Left building box
                    lineTo(w * 0.08f, h * 0.88f)
                    lineTo(w * 0.08f, h * 0.72f)
                    lineTo(w * 0.16f, h * 0.72f)
                    lineTo(w * 0.16f, h * 0.88f)
                    // MobileFone Tower silhouette
                    lineTo(w * 0.28f, h * 0.88f)
                    lineTo(w * 0.28f, h * 0.65f)
                    lineTo(w * 0.32f, h * 0.65f)
                    lineTo(w * 0.32f, h * 0.58f) // antenna peak
                    lineTo(w * 0.33f, h * 0.65f)
                    lineTo(w * 0.36f, h * 0.65f)
                    lineTo(w * 0.36f, h * 0.88f)
                    // Middle background trees
                    lineTo(w * 0.45f, h * 0.88f)
                    // Central landmark monument pillar (BMT Victory Monument arch base)
                    lineTo(w * 0.48f, h * 0.88f)
                    lineTo(w * 0.49f, h * 0.55f) // slender pillar peak
                    lineTo(w * 0.51f, h * 0.55f)
                    lineTo(w * 0.52f, h * 0.88f)
                    // Right high-rise buildings
                    lineTo(w * 0.65f, h * 0.88f)
                    lineTo(w * 0.65f, h * 0.62f)
                    lineTo(w * 0.75f, h * 0.62f)
                    lineTo(w * 0.75f, h * 0.88f)
                    // Small cottages/trees on far right
                    lineTo(w * 0.85f, h * 0.88f)
                    lineTo(w * 0.88f, h * 0.78f) // tree outline
                    lineTo(w * 0.91f, h * 0.88f)
                    lineTo(w, h * 0.88f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(
                    path = cityPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF532402).copy(alpha = 0.5f),
                            Color(0xFF1E3A5F).copy(alpha = 0.65f)
                        )
                    )
                )

                // Draw a few cute stylized palm tree fan vector decorations
                val drawPalmTree: (Float, Float, Float) -> Unit = { cx, cy, scale ->
                    val trPath = Path()
                    trPath.moveTo(cx, cy)
                    trPath.quadraticTo(cx - 5f * scale, cy - 25f * scale, cx - 12f * scale, cy - 35f * scale)
                    // Draw 5 leaves spreading
                    drawCircle(Color(0xFF2E7D32).copy(alpha = 0.4f), radius = 5f * scale, center = androidx.compose.ui.geometry.Offset(cx - 12f * scale, cy - 35f * scale))
                    drawCircle(Color(0xFF2E7D32).copy(alpha = 0.4f), radius = 5f * scale, center = androidx.compose.ui.geometry.Offset(cx - 18f * scale, cy - 30f * scale))
                    drawCircle(Color(0xFF2E7D32).copy(alpha = 0.4f), radius = 5f * scale, center = androidx.compose.ui.geometry.Offset(cx - 4f * scale, cy - 36f * scale))
                    drawLine(Color(0xFF5E3411).copy(alpha = 0.6f), androidx.compose.ui.geometry.Offset(cx, cy), androidx.compose.ui.geometry.Offset(cx - 8f * scale, cy - 30f * scale), 2f * scale)
                }
                drawPalmTree(w * 0.22f, h * 0.88f, 1.2f)
                drawPalmTree(w * 0.42f, h * 0.88f, 0.9f)
                drawPalmTree(w * 0.78f, h * 0.88f, 1.1f)
                
                // Sunset mountain lines
                val path = Path().apply {
                    moveTo(0f, h * 0.6f)
                    cubicTo(
                        w * 0.25f, h * 0.48f,
                        w * 0.5f, h * 0.72f,
                        w * 0.75f, h * 0.52f
                    )
                    lineTo(w, h * 0.68f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(
                    path = path,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFF9100).copy(alpha = 0.22f),
                            primaryColor.copy(alpha = 0.08f)
                        )
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            // Emblem and Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val customLogoId = remember(context) {
                    context.resources.getIdentifier("logo", "drawable", context.packageName)
                }
                
                // 3D Circular Metallic Bronze/Copper Monument Medallion Logo
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFFF9C075), Color(0xFF91511A))
                            )
                        )
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFFFF0D4), Color(0xFF6B3B10))
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (customLogoId != 0) {
                        Image(
                            painter = painterResource(id = customLogoId),
                            contentDescription = "Logo Đắk Lắk AI",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            
                            // Draw central A-frame monument triangle
                            val trianglePath = Path().apply {
                                moveTo(w * 0.5f, h * 0.16f)
                                lineTo(w * 0.86f, h * 0.68f)
                                lineTo(w * 0.72f, h * 0.68f)
                                lineTo(w * 0.5f, h * 0.35f)
                                lineTo(w * 0.28f, h * 0.68f)
                                lineTo(w * 0.14f, h * 0.68f)
                                close()
                            }
                            drawPath(
                                path = trianglePath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFFFFF2D4), Color(0xFFD39655))
                                )
                            )
                            
                            // Draw traditional central highland 8-pointed star/drum motif
                            val numPoints = 8
                            val outerRadius = w * 0.15f
                            val innerRadius = w * 0.06f
                            val centerX = w * 0.5f
                            val centerY = h * 0.46f
                            
                            val starPath = Path()
                            for (i in 0 until numPoints * 2) {
                                val r = if (i % 2 == 0) outerRadius else innerRadius
                                val angle = i * Math.PI.toFloat() / numPoints
                                val px = centerX + r * Math.cos(angle.toDouble()).toFloat()
                                val py = centerY + r * Math.sin(angle.toDouble()).toFloat()
                                if (i == 0) starPath.moveTo(px, py) else starPath.lineTo(px, py)
                            }
                            starPath.close()
                            drawPath(
                                path = starPath,
                                color = Color(0xFFFFF2D4)
                            )
                            
                            // Drum center core
                            drawCircle(
                                color = Color(0xFF91511A),
                                radius = w * 0.05f,
                                center = androidx.compose.ui.geometry.Offset(centerX, centerY)
                            )
                            drawCircle(
                                color = Color(0xFFFFF2D4),
                                radius = w * 0.02f,
                                center = androidx.compose.ui.geometry.Offset(centerX, centerY)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "ỦY BAN NHÂN DÂN TỈNH ĐẮK LẮK",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.92f),
                        letterSpacing = 0.7.sp
                    )
                    Text(
                        text = "ĐẮK LẮK AI",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "AI VÌ CUỘC SỐNG • AI FOR LIFE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFB300), // Rich copper gold color
                        letterSpacing = 1.8.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Administrative Search Mock Input Box decoration
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, primaryColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search icon",
                        tint = primaryColor
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Tìm kiếm dịch vụ công, an ninh, thời tiết...",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.61f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFF6D00).copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "AI SEARCH",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6D00)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocalInfoIndicators() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Temperature/Weather Widget card
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF9100).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny, 
                        contentDescription = "Weather", 
                        tint = Color(0xFFFF6D00)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = "Buôn Ma Thuột", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "28°C", fontSize = 15.sp, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "• Nắng nhẹ", fontSize = 10.sp, color = Color(0xFF4CAF50))
                    }
                }
            }
        }

        // System OK IOC Status
        Card(
            modifier = Modifier.weight(1.1f),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                    val pulseScale by infiniteTransition.animateFloat(
                        initialValue = 0.85f,
                        targetValue = 1.15f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1200, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "pulsescale"
                    )
                    Icon(
                        imageVector = Icons.Default.CloudQueue, 
                        contentDescription = "System Status", 
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.scale(pulseScale)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = "IOC Kết Nối", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    Text(
                        text = "HOẠT ĐỘNG TỐT", 
                        fontSize = 11.sp, 
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun VividRealisticIcon(
    screenId: String,
    modifier: Modifier = Modifier,
    sizeDp: Int = 44,
    emojiSizeSp: Int = 24
) {
    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .clip(RoundedCornerShape((sizeDp * 0.27f).dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        getScreenBrandingColor(screenId).copy(alpha = 0.22f),
                        getScreenBrandingColor(screenId).copy(alpha = 0.06f)
                    )
                )
            )
            .border(
                width = 1.2.dp,
                color = getScreenBrandingColor(screenId).copy(alpha = 0.45f),
                shape = RoundedCornerShape((sizeDp * 0.27f).dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            when (screenId) {
                "citizen" -> {
                    drawRect(
                        color = Color(0xFF0E4A8A).copy(alpha = 0.08f),
                        size = size
                    )
                }
                "health" -> {
                    val path = Path().apply {
                        moveTo(0f, h * 0.5f)
                        lineTo(w * 0.25f, h * 0.5f)
                        lineTo(w * 0.4f, h * 0.2f)
                        lineTo(w * 0.55f, h * 0.8f)
                        lineTo(w * 0.7f, h * 0.5f)
                        lineTo(w, h * 0.5f)
                    }
                    drawPath(
                        path = path,
                        color = Color(0xFFE53935).copy(alpha = 0.35f),
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                }
                "tourism" -> {
                    val path = Path().apply {
                        moveTo(w * 0.1f, h * 0.85f)
                        lineTo(w * 0.5f, h * 0.35f)
                        lineTo(w * 0.9f, h * 0.85f)
                        close()
                    }
                    drawPath(path = path, color = Color(0xFFE08B00).copy(alpha = 0.25f))
                }
                "coffee" -> {
                    drawCircle(
                        color = Color(0xFF7D5030).copy(alpha = 0.12f),
                        radius = w * 0.38f
                    )
                }
                "camera" -> {
                    drawLine(
                        color = Color(0xFF00ACC1).copy(alpha = 0.4f),
                        start = androidx.compose.ui.geometry.Offset(0f, h * 0.5f),
                        end = androidx.compose.ui.geometry.Offset(w, h * 0.5f),
                        strokeWidth = 1.dp.toPx()
                    )
                    drawLine(
                        color = Color(0xFF00ACC1).copy(alpha = 0.4f),
                        start = androidx.compose.ui.geometry.Offset(w * 0.5f, 0f),
                        end = androidx.compose.ui.geometry.Offset(w * 0.5f, h),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                "planning" -> {
                    drawLine(color = Color(0xFF3949AB).copy(alpha = 0.15f), start = androidx.compose.ui.geometry.Offset(w * 0.33f, 0f), end = androidx.compose.ui.geometry.Offset(w * 0.33f, h))
                    drawLine(color = Color(0xFF3949AB).copy(alpha = 0.15f), start = androidx.compose.ui.geometry.Offset(w * 0.66f, 0f), end = androidx.compose.ui.geometry.Offset(w * 0.66f, h))
                    drawLine(color = Color(0xFF3949AB).copy(alpha = 0.15f), start = androidx.compose.ui.geometry.Offset(0f, h * 0.33f), end = androidx.compose.ui.geometry.Offset(w, h * 0.33f))
                    drawLine(color = Color(0xFF3949AB).copy(alpha = 0.15f), start = androidx.compose.ui.geometry.Offset(0f, h * 0.66f), end = androidx.compose.ui.geometry.Offset(w, h * 0.66f))
                }
                "alert" -> {
                    drawCircle(
                        color = Color(0xFFDD2C00).copy(alpha = 0.08f),
                        radius = w * 0.45f
                    )
                }
                "agriculture" -> {
                    drawArc(
                        color = Color(0xFF4CAF50).copy(alpha = 0.15f),
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = true,
                        size = androidx.compose.ui.geometry.Size(w, h * 0.7f),
                        topLeft = androidx.compose.ui.geometry.Offset(0f, h * 0.15f)
                    )
                }
                "forest" -> {
                    val path = Path().apply {
                        moveTo(w * 0.2f, h * 0.8f)
                        lineTo(w * 0.5f, h * 0.2f)
                        lineTo(w * 0.8f, h * 0.8f)
                        close()
                    }
                    drawPath(path = path, color = Color(0xFF2E7D32).copy(alpha = 0.2f))
                }
                "culture" -> {
                    drawCircle(color = Color(0xFF9C27B0).copy(alpha = 0.12f), radius = w * 0.44f, style = Stroke(width = 1.dp.toPx()))
                    drawCircle(color = Color(0xFF9C27B0).copy(alpha = 0.12f), radius = w * 0.28f, style = Stroke(width = 1.dp.toPx()))
                }
                "crime" -> {
                    drawRect(color = Color(0xFF212121).copy(alpha = 0.08f))
                }
                "field" -> {
                    val len = w * 0.2f
                    drawLine(Color(0xFFFF6D00).copy(alpha = 0.35f), androidx.compose.ui.geometry.Offset(0f, 0f), androidx.compose.ui.geometry.Offset(len, 0f), 1.5.dp.toPx())
                    drawLine(Color(0xFFFF6D00).copy(alpha = 0.35f), androidx.compose.ui.geometry.Offset(0f, 0f), androidx.compose.ui.geometry.Offset(0f, len), 1.5.dp.toPx())
                    drawLine(Color(0xFFFF6D00).copy(alpha = 0.35f), androidx.compose.ui.geometry.Offset(w, h), androidx.compose.ui.geometry.Offset(w - len, h), 1.5.dp.toPx())
                    drawLine(Color(0xFFFF6D00).copy(alpha = 0.35f), androidx.compose.ui.geometry.Offset(w, h), androidx.compose.ui.geometry.Offset(w, h - len), 1.5.dp.toPx())
                }
            }
        }
        
        val emojiToShow = when (screenId) {
            "citizen" -> "🪪"      // Administration card
            "health" -> "🩺"       // Medical stethoscope
            "tourism" -> "🧭"      // Compass/Path finder
            "crime" -> "🚨"        // Warning siren flash
            "field" -> "📸"        // Camera snapshot
            "coffee" -> "☕"       // Robusta hot phin cup
            "agriculture" -> "🥑"  // Highland organic fruit avocado/durian
            "forest" -> "🌲"       // Yok Don giant forest pine
            "culture" -> "🥁"      // Cultural cồng chiêng drum
            "camera" -> "📹"       // AI optical lens eye
            "planning" -> "🗺️"      // Blue structural planning map
            "alert" -> "⚡"        // Weather warning discharge
            else -> "✨"
        }
        
        Text(
            text = emojiToShow,
            fontSize = emojiSizeSp.sp,
            modifier = Modifier.scale(1.15f)
        )
    }
}

@Composable
fun FunctionGridCard(
    screen: Screen,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .border(
                width = 1.dp, 
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .testTag("function_card_${screen.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Vivid realistic custom graphical icon
            VividRealisticIcon(screenId = screen.id, sizeDp = 44, emojiSizeSp = 24)
            
            Spacer(modifier = Modifier.height(10.dp))
            
            Text(
                text = screen.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun GovernmentFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f))
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Bản quyền thuộc về UBND Tỉnh Đắk Lắk",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.44f)
        )
        Text(
            text = "Hệ thống thí điểm chuyển đổi số cộng đồng thông minh kết hợp trí tuệ nhân tạo Gemini.",
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        // High quality seal badge indicating safe state secure
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF4CAF50).copy(alpha = 0.1f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.VerifiedUser,
                contentDescription = "Safe Security",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "BẢO MẬT & CHÍNH TRỰC",
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF4CAF50)
            )
        }
    }
}

// ----------------CHAT DETAILS AI SCREEN -----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatAIScreen(viewModel: MainViewModel, screen: Screen) {
    val messages by viewModel.messages.collectAsState()
    val isRecording by viewModel.isRecording.collectAsState()
    val voiceTimer by viewModel.voiceTimer.collectAsState()
    val uploadedImagePath by viewModel.uploadedImagePath.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Auto-scroll chat to the end on new messages
    LaunchedEffect(messages.size, isLoading) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    var showImagePickerDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("chat_screen_${screen.id}")
    ) {
        // Core Header
        CenterAlignedTopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    VividRealisticIcon(
                        screenId = screen.id,
                        sizeDp = 30,
                        emojiSizeSp = 18
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = screen.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = { viewModel.handleBack() },
                    modifier = Modifier.testTag("back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Trở lại"
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = { viewModel.clearChatHistory() },
                    modifier = Modifier.testTag("clear_history_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Xóa lịch sử",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.82f)
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

        // Screen Info Bar / Context helper
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(getScreenBrandingColor(screen.id).copy(alpha = 0.06f))
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(getScreenBrandingColor(screen.id))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = getScreenSlogan(screen.id),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = getScreenBrandingColor(screen.id).copy(alpha = 0.85f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Dữ liệu địa phương",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
            }
        }

        // Chat Bubble message list / History
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (messages.isEmpty()) {
                // Empty state instructions
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .testTag("chat_empty_state"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    VividRealisticIcon(
                        screenId = screen.id,
                        sizeDp = 64,
                        emojiSizeSp = 36
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Khởi chạy phiên làm việc AI",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Vui lòng nhập câu hỏi, chụp ảnh hiện trường hoặc bấm phím micro nói trực tiếp bên dưới.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.54f),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Suggestion suggestions pills based on typical highland requirements
                    Text(
                        text = "CÂU HỎI THƯỜNG GẶP KHUYẾN NGHỊ:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = getScreenBrandingColor(screen.id),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    val suggestions = getSuggestionsForScreen(screen.id)
                    suggestions.forEach { suggestion ->
                        Card(
                            onClick = { viewModel.updateSearchQuery(suggestion) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            )
                        ) {
                            Text(
                                text = suggestion,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(10.dp),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                        .testTag("chat_messages_lazycolumn")
                ) {
                    items(messages) { message ->
                        ChatBubbleItem(message, screen.id)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    if (isLoading) {
                        item {
                            AILoadingIndicator(screen.id)
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }

        // Recording Pulse Interface Overlay (if active)
        if (isRecording) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.88f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Đang lắng nghe âm thanh giọng nói của bạn...",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Thời gian ghi âm: 00:0${voiceTimer}s",
                        color = Color(0xFFFF9100),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Simulating cool moving audio graph waves in canvas
                    Box(modifier = Modifier.size(width = 180.dp, height = 40.dp)) {
                        val animatedTime = rememberInfiniteTransition("audio_wave").animateFloat(
                            initialValue = 0f,
                            targetValue = 2f * Math.PI.toFloat(),
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart
                            ),
                            label = "waves"
                        )
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val strokeWidth = 2.dp.toPx()
                            val path = Path()
                            path.moveTo(0f, h / 2f)
                            for (x in 0..w.toInt() step 5) {
                                val y = (h / 2f) + (Math.sin((x * 0.05 + animatedTime.value).toDouble()) * 15f).toFloat()
                                path.lineTo(x.toFloat(), y)
                            }
                            drawPath(
                                path = path,
                                color = Color(0xFF00B0FF),
                                style = Stroke(width = strokeWidth)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.toggleVoiceRecording() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(imageVector = Icons.Default.Stop, contentDescription = "Dừng")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Dừng & Phân tích")
                    }
                }
            }
        }

        // Preview of attached upload image (if present)
        if (uploadedImagePath != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val matchingImg = sampleElevatedImages.find { it.name == uploadedImagePath }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = matchingImg?.emoji ?: "🖼️", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = matchingImg?.vietnameseName ?: "Hình ảnh đính kèm",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = matchingImg?.description ?: "Dữ liệu khảo sát thực địa Đắk Lắk",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.54f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    IconButton(onClick = { viewModel.clearUploadedImage() }) {
                        Icon(
                            imageVector = Icons.Default.Cancel, 
                            contentDescription = "Hủy đính kèm",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        // Input bottom actions bar
        Card(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Upload Image icon
                IconButton(
                    onClick = { showImagePickerDialog = true },
                    modifier = Modifier.testTag("upload_image_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Đính kèm ảnh",
                        tint = getScreenBrandingColor(screen.id)
                    )
                }

                // Voice input icon
                IconButton(
                    onClick = { viewModel.toggleVoiceRecording() },
                    modifier = Modifier.testTag("voice_input_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Ghi âm giọng nói Việt",
                        tint = getScreenBrandingColor(screen.id)
                    )
                }

                // TextField message
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = { Text("Hỏi AI Đắk Lắk...", fontSize = 13.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .testTag("chat_input_textfield"),
                    shape = RoundedCornerShape(20.dp),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        viewModel.sendMessage()
                        keyboardController?.hide()
                    }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = getScreenBrandingColor(screen.id),
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                    )
                )

                // Send button
                IconButton(
                    onClick = {
                        viewModel.sendMessage()
                        keyboardController?.hide()
                    },
                    modifier = Modifier.testTag("send_msg_btn"),
                    enabled = searchQuery.isNotBlank() || uploadedImagePath != null
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Gửi",
                        tint = if (searchQuery.isNotBlank() || uploadedImagePath != null) {
                            getScreenBrandingColor(screen.id)
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                        }
                    )
                }
            }
        }
    }

    // Modal Image Picker Dialog
    if (showImagePickerDialog) {
        AlertDialog(
            onDismissRequest = { showImagePickerDialog = false },
            title = {
                Text(
                    text = "Khảo sát Hiện trường Đắk Lắk AI",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = getScreenBrandingColor(screen.id)
                )
            },
            text = {
                Column {
                    Text(
                        text = "Vui lòng chọn 1 trong các hình ảnh vệ tinh/hiện trường có sẵn của Tây Nguyên bên dưới để đồng kiểm tra:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    LazyRow {
                        items(sampleElevatedImages) { sample ->
                            Card(
                                modifier = Modifier
                                    .width(130.dp)
                                    .height(140.dp)
                                    .padding(end = 8.dp)
                                    .clickable {
                                        viewModel.setSimulatedUploadedImage(sample.name)
                                        showImagePickerDialog = false
                                    },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = sample.emoji, fontSize = 28.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = sample.vietnameseName,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = sample.description,
                                        fontSize = 9.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.54f),
                                        textAlign = TextAlign.Center,
                                        lineHeight = 11.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showImagePickerDialog = false }) {
                    Text("Đóng Hủy", color = getScreenBrandingColor(screen.id))
                }
            }
        )
    }
}

@Composable
fun ChatBubbleItem(message: ChatMessage, screenId: String) {
    val isUser = message.isUser
    val primaryBranding = getScreenBrandingColor(screenId)
    
    // Bubble date formatter
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val formattedTime = sdf.format(Date(message.timestamp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(if (isUser) "user_msg_bubble" else "ai_msg_bubble"),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            // Little decorative circular emblem avatar for local AI
            Box(
                modifier = Modifier
                    .padding(end = 8.dp, top = 4.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(primaryBranding.copy(alpha = 0.15f))
                    .border(1.dp, primaryBranding, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "DL", 
                    fontSize = 11.sp, 
                    fontWeight = FontWeight.Black, 
                    color = primaryBranding
                )
            }
        }

        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Card(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isUser) 16.dp else 2.dp,
                    bottomEnd = if (isUser) 2.dp else 16.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUser) {
                        primaryBranding
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = if (!isUser) {
                    BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                } else null
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                    // Display image if attached
                    if (message.imagePath != null) {
                        val matchingImg = sampleElevatedImages.find { it.name == message.imagePath }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(bottom = 6.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (isUser) Color.White.copy(alpha = 0.15f)
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .padding(8.dp)
                        ) {
                            Text(text = matchingImg?.emoji ?: "🖼️", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = matchingImg?.vietnameseName ?: "Ảnh",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Text(
                        text = message.text,
                        fontSize = 13.5.sp,
                        color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                        lineHeight = 19.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(3.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = formattedTime,
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
                if (isUser) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = "Gửi thành công",
                        tint = primaryBranding,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AILoadingIndicator(screenId: String) {
    val primaryColor = getScreenBrandingColor(screenId)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ai_loading_indicator"),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .size(28.dp)
                .clip(CircleShape)
                .background(primaryColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            val infiniteTransition = rememberInfiniteTransition("indicator_rotate")
            val angle by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "rotate"
            )
            Icon(
                imageVector = Icons.Default.Autorenew,
                contentDescription = "Loading",
                tint = primaryColor,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(angle)
            )
        }
        
        Card(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Đắk Lắk AI đang viết phản hồi",
                    fontSize = 11.5.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.61f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                // Simulated bouncing typing indicators
                val infiniteTransition = rememberInfiniteTransition("bounce")
                val yOffset by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = -4f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(400, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "bounce"
                )
                Box(
                    modifier = Modifier
                        .offset(y = yOffset.dp)
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(primaryColor)
                )
            }
        }
    }
}

// Custom Colors per specific screen representing smart government and ecological highlights
fun getScreenBrandingColor(screenId: String): Color {
    return when (screenId) {
        "citizen" -> Color(0xFF0E4A8A)       // Deep Administration Blue
        "health" -> Color(0xFFE53935)        // Medical Red
        "tourism" -> Color(0xFFE08B00)       // Highland Golden Sand
        "crime" -> Color(0xFF212121)         // Dark Security Steel
        "field" -> Color(0xFFFF6D00)         // Orange Alert
        "coffee" -> Color(0xFF7D5030)        // Rich Robusta Brown
        "agriculture" -> Color(0xFF4CAF50)   // Farm Organic Green
        "forest" -> Color(0xFF2E7D32)        // Deep Jade Forest Green
        "culture" -> Color(0xFF9C27B0)       // Cultural Purple
        "camera" -> Color(0xFF00ACC1)        // Intelligent Camera Cyan
        "planning" -> Color(0xFF3949AB)      // Map Deep Purple-Blue
        "alert" -> Color(0xFFDD2C00)         // Urgent Warning Orange-Red
        else -> Color(0xFF0E4A8A)
    }
}

fun getScreenSlogan(screenId: String): String {
    return when (screenId) {
        "citizen" -> "Hồ sơ hành chính minh bạch - Đơn giản hóa thủ tục công"
        "health" -> "Tư vấn sức khỏe chủ động - Đồng hành y tế Tây Nguyên"
        "tourism" -> "Khám phá danh lam thắng cảnh và bản sắc Ê-đê mộc mạc"
        "crime" -> "Bảo vệ trật tự an ninh - Tiếp tiếp tố giác được mã hóa"
        "field" -> "Kết nối hiện trường thông minh - Vì Việt Nam văn minh đô thị"
        "coffee" -> "Tìm hiểu cà phê Robusta ngon nhất thế giới Buôn Ma Thuột"
        "agriculture" -> "Nông nghiệp bền vững - Hướng dẫn kỹ thuật nông lâm sản"
        "forest" -> "Bảo vệ lá phổi xanh Tây Nguyên - Yok Don National Park"
        "culture" -> "Giao duyên Không gian văn hóa Cồng chiêng linh thiêng"
        "camera" -> "Mắt thần đô thị - Kiểm soát giám sát thông minh 24/7"
        "planning" -> "Định vị quy hoạch Buôn Ma Thuột đô thị sinh thái xanh"
        "alert" -> "Cảnh báo sớm khẩn cấp thiên tai, giông lốc và dịch hại"
        else -> "Trợ lý AI vì cuộc sống người dân Đắk Lắk"
    }
}

fun getSuggestionsForScreen(screenId: String): List<String> {
    return when (screenId) {
        "citizen" -> listOf(
            "Làm cách nào đăng ký tạm trú trực tuyến qua mạng?",
            "Thủ tục cấp đổi Căn cước công dân gắn chíp tại Buôn Ma Thuột?"
        )
        "health" -> listOf(
            "Biểu hiện sốt xuất huyết khác sốt rét như thế nào?",
            "Khuyến cáo phòng muỗi đốt vào mùa mưa ẩm Tây Nguyên?"
        )
        "tourism" -> listOf(
            "Địa chỉ trải nghiệm du lịch cưỡi voi thân thiện Buôn Đôn?",
            "Lịch trình tour du lịch thác Dray Nur - Dray Sáp 1 ngày?"
        )
        "crime" -> listOf(
            "Phát hiện nghi phạm đánh bạc, nộp tin tố giác có bảo mật?",
            "Quy định khai báo vi phạm lâm sản lâm tặc phá rừng gỗ quý?"
        )
        "field" -> listOf(
            "Mặt đường giao thông Lê Duẩn sạt lở ổ gà lớn, gửi phản ánh?",
            "Người dân xả rác bừa bãi ra lòng hồ Ea Kao phản ánh thế nào?"
        )
        "coffee" -> listOf(
            "Vì sao cà phê vối Robusta Buôn Ma Thuột có hương vị đặc biệt?",
            "Công thức pha chế cà phê sữa đá phin chuẩn ngon Buôn Ma Thuột?"
        )
        "agriculture" -> listOf(
            "Kỹ thuật kích thích sầu riêng ra hoa trái vụ mùa khô cằn?",
            "Trị rệp sáp cho cây cà phê bằng chế phẩm sinh học thế nào?"
        )
        "forest" -> listOf(
            "Cần báo cho ai khi thấy hành vi đốt nương rẫy mấp mé bìa rừng?",
            "Quy định tuần tra bảo tồn gen quý tại rừng đặc dụng Yok Don?"
        )
        "culture" -> listOf(
            "Ý nghĩa hình dáng con thuyền của nhà dài Ê-đê bản địa?",
            "Nghe kể nhạc khí Knah trong không gian văn hóa cồng chiêng?"
        )
        "camera" -> listOf(
            "Hệ thống camera an ninh phạt nguội nằm ở những cung đường nào?",
            "Nguyên lý camera AI phân luồng giao thông chống ùn tắc giờ cao điểm?"
        )
        "planning" -> listOf(
            "Bản đồ quy hoạch Buôn Ma Thuột hướng thành đô thị sinh thái?",
            "Cao tốc Khánh Hòa - Buôn Ma Thuột đi qua những phân khúc nào?"
        )
        "alert" -> listOf(
            "Cập nhật thời tiết giông sét tại Krông Pắc chiều nay?",
            "Cảnh báo dịch sâu cuốn lá lúa hại nông nghiệp ở Ea Súp?"
        )
        else -> listOf("Xin chào trợ lý Đắk Lắk AI")
    }
}

fun getIconForScreen(symbol: String): ImageVector {
    return when (symbol) {
        "gavel" -> Icons.Default.Gavel
        "vaccines" -> Icons.Default.MedicalServices
        "map" -> Icons.Default.Place
        "security" -> Icons.Default.Shield
        "photo_camera" -> Icons.Default.CameraAlt
        "coffee" -> Icons.Default.Coffee
        "agriculture" -> Icons.Default.Grass
        "park" -> Icons.Default.Park
        "music_note" -> Icons.Default.MusicNote
        "videocam" -> Icons.Default.Videocam
        "layers" -> Icons.Default.Layers
        "warning" -> Icons.Default.Warning
        else -> Icons.Default.Info
    }
}

@Composable
fun FirestoreDatabaseHub(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val firestoreService = remember { com.example.data.FirestoreService() }
    val isLive = firestoreService.isCloudConnected()
    
    var selectedIndex by remember { mutableIntStateOf(0) }
    var isSyncing by remember { mutableStateOf(false) }
    var syncResultLog by remember { mutableStateOf("Nhấp 'Gửi Dữ Liệu Mẫu' để kiểm thử kết nối...") }
    val coroutineScope = rememberCoroutineScope()
    
    val schemas = listOf(
        Triple("AI Công dân", "/administrative_procedures", "Id, UserId, ProcedureType, ApplicantName, NationalId, Address, Details, Status, SubmittedAt"),
        Triple("AI Sức khỏe", "/health_records", "Id, UserId, SymptomSummary, ReportedSymptoms, AiMedicalAdvice, NeedsHospitalCheckup, CreatedAt"),
        Triple("AI Du lịch", "/tourism_checkins", "Id, UserId, LocationName, ExperienceReview, Rating, VisitedAt"),
        Triple("AI Tố giác vi phạm", "/crime_reports", "Id, UserId, ReportType, Description, IncidentLocation, SuspectDetails, Status, ReportedAt"),
        Triple("AI Phản ánh hiện trường", "/field_incidents", "Id, UserId, Category, Description, ImageUrl, ReportedLocationAddress, Latitude, Longitude, Status, CreatedAt"),
        Triple("AI Cà phê Buôn Ma Thuột", "/coffee_ratings", "Id, UserId, CoffeeShopName, RobustaRating, Reviews, SubmittedAt"),
        Triple("AI Nông nghiệp", "/agri_support_issues", "Id, UserId, CropType, SymptomDescription, AiDiagnosticResult, NeedsAgronomist, Status, CreatedAt"),
        Triple("AI Bảo vệ rừng", "/forest_alerts", "Id, ReportedBy, SubForestZone, AlertLevel, SmokeDetected, LocationCoords, Status, Timestamp"),
        Triple("AI Văn hóa Tây Nguyên", "/cultural_artifacts", "Id, Name, Category, TribeOrigin, Description, LastMaintainedYear, ApprovedForTourism"),
        Triple("AI Camera thông minh", "/smart_camera_detections", "Id, CameraId, LocationIntersection, CongestionLevel, SpeedingAlert, ObstacleDetected, DetectedAt"),
        Triple("AI Quy hoạch", "/planning_records", "Id, ProjectTitle, IntendedZone, AffectedAreaHectares, CommenceYear, CompletionYear, Status, ApprovedBy"),
        Triple("AI Cảnh báo thiên tai", "/weather_warnings", "Id, ImpactedSubdivision, HazardType, Severity, WarningMessage, ValidUntil, PublishedAt")
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with cloud indicators
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isLive) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isLive) Icons.Default.CloudQueue else Icons.Default.CloudOff,
                        contentDescription = "Firestore status",
                        tint = if (isLive) Color(0xFF2E7D32) else Color(0xFFE65100),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Firebase Firestore Hub",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isLive) "Đang kết nối Cloud Firestore tuyến tuyển" else "Chế độ ngoại tuyến giả lập (Simulated Offline)",
                        fontSize = 11.sp,
                        color = if (isLive) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))
            
            // Selected schema spinner or category row
            Text(
                text = "Cấu trúc dữ liệu liên kết (12 Mục):",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(schemas.size) { index ->
                    val schema = schemas[index]
                    val isSelected = index == selectedIndex
                    AssistChip(
                        onClick = { selectedIndex = index },
                        label = {
                            Text(text = schema.first, fontSize = 11.sp)
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                            labelColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(10.dp))
            
            // Schema visualizer box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Collection Path: ",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        SelectionContainer {
                            Text(
                                text = schemas[selectedIndex].second,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Document Fields:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = schemas[selectedIndex].third,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Run test button & Logs
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        isSyncing = true
                        coroutineScope.launch {
                            val time = System.currentTimeMillis()
                            val success = when (selectedIndex) {
                                0 -> firestoreService.submitProcedure(
                                    com.example.data.AdministrativeProcedure(
                                        procedureType = "Cấp giấy phép xây dựng đô thị",
                                        applicantName = "Đắk Lắk AI Tester",
                                        nationalId = "040096123456",
                                        address = "12 Phan Bội Châu, TP. Buôn Ma Thuột",
                                        details = "Yêu cầu kiểm thử tự động hệ thống dịch vụ hành chính công dân"
                                    )
                                )
                                1 -> firestoreService.saveHealthRecord(
                                    com.example.data.HealthRecord(
                                        symtomSummary = "Sốt cao, đau mỏi hốc mắt",
                                        reportedSymptoms = "Sốt kèm nốt ban li ti dưới da",
                                        aiMedicalAdvice = "Có nguy cơ sốt xuất huyết Dengue. Hãy đến Bệnh viện Đa khoa Vùng Tây Nguyên để làm xét nghiệm tiểu cầu.",
                                        needsHospitalCheckup = true
                                    )
                                )
                                2 -> firestoreService.saveTourismCheckIn(
                                    com.example.data.TourismCheckIn(
                                        locationName = "Thác Dray Nur",
                                        experienceReview = "Vẻ đẹp hùng vĩ của dòng Sêrêpôk, dịch vụ du lịch bản địa rất độc đáo và đậm nét Tây Nguyên.",
                                        rating = 5
                                    )
                                )
                                3 -> firestoreService.submitCrimeReport(
                                    com.example.data.CrimeReport(
                                        reportType = "Lâm tặc phá rừng",
                                        description = "Phát hiện vết xe kéo và tiếng cưa máy cắt hạ gỗ quý tại vùng giáp ranh kiểm lâm.",
                                        incidentLocation = "Dọc ven suối sâu Yok Don",
                                        suspectDetails = "Nhóm 3 người lạ mặt trang bị cưa xích cầm tay"
                                    )
                                )
                                4 -> firestoreService.submitFieldIncident(
                                    com.example.data.FieldIncident(
                                        category = "Môi trường và Rác Thải",
                                        description = "Ngập bãi rác tự phát gây bốc mùi hôi thối gần sông Ea Kao.",
                                        reportedLocationAddress = "Khu du lịch sinh thái Ea Kao, Buôn Ma Thuột"
                                    )
                                )
                                5 -> firestoreService.saveCoffeeRating(
                                    com.example.data.CoffeeRating(
                                        coffeeShopName = "Cà phê Phin Nhôm Buôn Ma Thuột",
                                        robustaRating = 5,
                                        reviews = "Mùi khói nhẹ gỗ thông, vị Robusta Highland vô cùng đậm đà nồng nàn!"
                                    )
                                )
                                6 -> firestoreService.submitAgriSupportIssue(
                                    com.example.data.AgriSupportIssue(
                                        cropType = "Sầu riêng Dona giống mới",
                                        symptomDescription = "Xì mủ thân sầu riêng Dona thối rễ mùa ẩm ướt",
                                        aiDiagnosticResult = "Bệnh nấm Phytophthora gây ra. Khuyên bôi thuốc đặc trị và dọn cỏ gốc xới đất thoát nước."
                                    )
                                )
                                7 -> firestoreService.createForestAlert(
                                    com.example.data.ForestAlert(
                                        reportedBy = "Trạm Kiểm Lâm Trung Tâm",
                                        subForestZone = "Phân khu phục hồi sinh thái 4A Yok Don",
                                        alertLevel = "CRITICAL",
                                        smokeDetected = true,
                                        reportedLocationCoords = "12.71234, 107.82143"
                                    )
                                )
                                8 -> firestoreService.registerCulturalArtifact(
                                    com.example.data.CulturalArtifact(
                                        name = "Chiêng dăng (Knah) quý cổ",
                                        category = "Nhạc khí gõ",
                                        tribeOrigin = "Người Êđê",
                                        description = "Sở hữu bởi già làng Buôn AKôn, đại biểu tinh hoa của cội nguồn âm thanh cồng chiêng đại ngàn Tây Nguyên."
                                    )
                                )
                                9 -> firestoreService.submitSmartCameraDetection(
                                    com.example.data.SmartCameraDetection(
                                        cameraId = "CAM_BMT_ROUNDABOUT_01",
                                        locationIntersection = "Vòng xoay Ngã sáu Buôn Ma Thuột",
                                        congestionLevel = "CRITICAL",
                                        vehicleSpeedingAlert = true
                                    )
                                )
                                10 -> firestoreService.registerPlanningRecord(
                                    com.example.data.PlanningRecord(
                                        projectTitle = "Cao tốc Buôn Ma Thuột - Khánh Hòa",
                                        intendedZone = "Huyện Krông Pắc và Ea Kar",
                                        affectedAreaHectares = 425.8,
                                        commenceYear = 2024,
                                        completionYear = 2027
                                    )
                                )
                                11 -> firestoreService.publishWeatherWarning(
                                    com.example.data.WeatherWarning(
                                        impactedSubdivision = "Dốc đèo M'Đrắk, Quốc lộ 26",
                                        hazardType = "Sạt lở đất",
                                        severity = "EXTREME",
                                        warningMessage = "Mưa dông cực lớn gây nguy hiểm sạt lở mạnh dốc núi dốc M'Đrắk. Xe cộ chú ý hạn chế di chuyển!"
                                    )
                                )
                                else -> false
                            }
                            isSyncing = false
                            syncResultLog = if (success) {
                                "Đã đồng bộ mẫu thử đến hệ thống: ${schemas[selectedIndex].second}/sim_${time}\nTrạng thái: Hoàn tất"
                            } else {
                                "Đột phá lỗi đồng bộ Firestore. Hãy xem lại kết nối mạng của thiết bị!"
                            }
                        }
                    },
                    modifier = Modifier.testTag("firestore_sync_button"),
                    enabled = !isSyncing,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (isSyncing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 1.8.dp
                        )
                    } else {
                        Text(text = "Gửi Dữ Liệu Mẫu", fontSize = 12.sp)
                    }
                }
                
                Spacer(modifier = Modifier.width(10.dp))
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = syncResultLog,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// React-Inspired Admin Web Dashboard for managing the 12 AI services of Dak Lak
data class DashboardItem(
    val id: String,
    val title: String,
    val submitter: String,
    val status: String,
    val department: String,
    val time: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminDashboardScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    // Tab list mimicking the 12 dynamic AI integrations
    val collectionTabs = listOf(
        "AI Công dân", "AI Sức khỏe", "AI Du lịch", "AI Tối giác", "AI Hiện trường", "AI Cà phê BMT",
        "AI Nông nghiệp", "AI Bảo vệ rừng", "AI Văn hóa", "AI Camera", "AI Quy hoạch", "AI Cảnh báo"
    )
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    
    // Dynamic lists that can be modified by the admin in real-time
    var adminDataState by remember {
        mutableStateOf(
            mapOf(
                0 to listOf(
                    DashboardItem("PROC-7489-BMT", "Cấp phép đất đai phường Tân Lợi", "Nguyễn Văn Hùng", "Chờ duyệt", "Sở TNMT", "22/06 09:12"),
                    DashboardItem("PROC-7490-BMT", "Khai sinh điện tử trực tuyến", "H'Yến Niê", "Đã duyệt", "Phường Thắng Lợi", "22/06 08:30"),
                    DashboardItem("PROC-7491-BMT", "Đăng ký thành lập HTX Cà phê", "Y-Khương Êban", "Chờ duyệt", "Sở Kế hoạch", "22/06 07:45")
                ),
                1 to listOf(
                    DashboardItem("HEALTH-201-YKD", "Chẩn đoán bệnh Sốt xuất huyết", "Bệnh nhân Y-Minh", "Đã xử lý", "Trạm Y tế Krông Pắc", "22/06 09:05"),
                    DashboardItem("HEALTH-202-YKD", "Cấp cứu Tai biến đột ngột", "Bệnh nhân Trần Thị Lan", "Khẩn cấp", "Bệnh viện Vùng Tây Nguyên", "22/06 08:22")
                ),
                2 to listOf(
                    DashboardItem("TOUR-881", "Bình luận danh thắng Thác Dray Nur", "Lê Thanh Kiều", "Nổi bật", "Xúc tiến Du lịch Đắk Lắk", "21/06 18:40"),
                    DashboardItem("TOUR-882", "Review Trải nghiệm Buôn Đôn", "Y-Thái Êban", "Chờ duyệt", "Phòng Văn hóa Du lịch", "21/06 17:15")
                ),
                3 to listOf(
                    DashboardItem("CRIME-909", "Chặt phá lâm sản Yok Don", "Trạm tuần tra G03", "Báo động đỏ", "Hạt kiểm lâm Yok Don", "22/06 09:00"),
                    DashboardItem("CRIME-910", "Xả rác thải xây dựng sai quy định", "Người dân giấu tên", "Đang xử lý", "Công an môi trường tỉnh", "22/06 05:30")
                ),
                4 to listOf(
                    DashboardItem("FIELD-332", "Ổ gà sạt đường Phan Bội Châu", "Công dân tự phát", "Đang sửa", "Phòng Đô thị Thành phố", "22/06 08:15"),
                    DashboardItem("FIELD-333", "Cột điện gãy đổ nguy hiểm", "Bùi Thế Bảo", "Đã thu dọn", "Điện lực Đắk Lắk", "22/06 06:10")
                ),
                5 to listOf(
                    DashboardItem("COFFEE-101", "Kiểm định Robusta Phin Nhôm", "Hộ trồng Ea Kao", "Đã cấp chứng chỉ", "Hiệp hội Cà phê BMT", "21/06 20:30"),
                    DashboardItem("COFFEE-102", "Mẫu thử Arabica Đắk Lắk", "Viện Eakmat", "Đang phân tích", "Sở Khoa học Công nghệ", "21/06 19:15")
                ),
                6 to listOf(
                    DashboardItem("AGRI-501", "Bệnh thối rễ Sầu riêng Dona", "Nhà vườn Cư M'gar", "Đã hướng dẫn", "Trung tâm Khuyến nông tỉnh", "22/06 08:50"),
                    DashboardItem("AGRI-502", "Rệp sáp dịch bùng cà phê", "Hợp tác xã Krông Năng", "Cảnh báo dịch", "Trạm Bảo vệ Thực vật", "22/06 07:11")
                ),
                7 to listOf(
                    DashboardItem("FOREST-09", "Phát hiện lò than lén bìa rừng", "Drone Hồng ngoại", "Đang xử lý", "Hạt Kiểm lâm Ea Súp", "22/06 09:14"),
                    DashboardItem("FOREST-10", "Dò vết xe kéo trộm gỗ Yok Don", "Vệ tinh giám sát", "Theo dõi", "Đội cơ động đặc dụng", "22/06 04:20")
                ),
                8 to listOf(
                    DashboardItem("CULTURE-44", "Bảo tàng Cồng chiêng Buôn AKôn", "Nghệ nhân Y-Mút", "Đã số hóa", "Sở Văn hóa Thể thao Du lịch", "21/06 14:30"),
                    DashboardItem("CULTURE-45", "Tổ chức Lễ hội Đua voi truyền thống", "Buôn trưởng Buôn Đôn", "Ưu tiên duyệt", "Cổng thông tin Tây Nguyên", "21/06 11:20")
                ),
                9 to listOf(
                    DashboardItem("CAM-88", "Ùn tắc cục bộ Ngã sáu", "Camera AI số 01", "Gợi ý phân luồng", "Trung tâm Điều hành Đô thị IOC", "22/06 09:22"),
                    DashboardItem("CAM-89", "Xe ô tô vượt vạch đèn đỏ", "Camera thông minh số 04", "Đang gửi phạt nguội", "Công an giao thông BMT", "22/06 09:01")
                ),
                10 to listOf(
                    DashboardItem("PLAN-2026", "Cao tốc trục ngang BMT - Khánh Hòa", "Ban QLDA Quốc lộ 26", "Đang xây dựng", "Sở Giao thông Vận tải", "21/06 09:30"),
                    DashboardItem("PLAN-2027", "Hồ điều hòa sinh thái Ea Kao", "Phòng quy hoạch đô thị", "Dự thảo lấy ý kiến", "UBND Thành phố BMT", "20/06 15:45")
                ),
                11 to listOf(
                    DashboardItem("ALERT-701", "Lũ ống cục bộ sạt đèo M'Đrắk", "Sở khí tượng thủy văn", "Đã phát báo động", "Truyền thông Quốc lộ 26", "22/06 09:15"),
                    DashboardItem("ALERT-702", "Cảnh báo hạn hán hạn mặn vùng thấp", "Trạm đo trung tâm", "Cảnh báo cao", "Sở Nông nghiệp & PTNT", "22/06 08:35")
                )
            )
        )
    }

    val primaryBranding = MaterialTheme.colorScheme.tertiary
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)) // Sleek Dark Slate Web Style (Tailwind Style)
    ) {
        // Simulated React/Tailwind Web Browser Top Address Bar Line
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E293B))
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF10B981)))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF0F172A))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Secure",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "https://admin-hub.daklak.ai/dashboard/react-core",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            IconButton(
                onClick = { viewModel.navigateTo(Screen.Home) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Hủy",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Dashboard Navigation Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E293B))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(Screen.Home) },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "ĐẮK LẮK SMART AI CORE",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF10B981)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Live Admin Panel React Node (v2.6)",
                        color = Color(0xFF10B981),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Live Stats Cards Row (Total Items, AI Status, Ping, Dynamic DB)
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Total items
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Tổng tác vụ AI", color = Color(0xFF94A3B8), fontSize = 10.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("2,492", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("+15% tuần này", color = Color(0xFF10B981), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    // AI Status
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Băng thông AI", color = Color(0xFF94A3B8), fontSize = 10.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("28.4 TFLOPS", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Hệ thống: Bình thường", color = Color(0xFF38BDF8), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    // Network latency ping
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Độ trễ Mạng", color = Color(0xFF94A3B8), fontSize = 10.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("32 ms", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Kết nối Cloud: Tốt", color = Color(0xFF10B981), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Interactive Live Canvas Network Data Traffic Chart Visualizer
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("BIỂU ĐỒ HOẠT ĐỘNG TRUY CẬP AI DỮ LIỆU", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("Lượt đồng bộ hóa dữ liệu hành chính & phản ánh 24h qua", color = Color(0xFF94A3B8), fontSize = 10.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF0F172A))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Live Syncing", color = Color(0xFF10B981), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Beautiful Custom Canvas Line Chart drawing native Sparkline
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        ) {
                            val w = size.width
                            val h = size.height
                            
                            // Draw Grid Lines (Horizontal Background Lines)
                            for (i in 0..4) {
                                val lineH = h * (i / 4f)
                                drawLine(
                                    color = Color(0xFF334155).copy(alpha = 0.5f),
                                    start = Offset(0f, lineH),
                                    end = Offset(w, lineH),
                                    strokeWidth = 1f
                                )
                            }
                            
                            // Draw the dynamic glowing activity spark path line
                            val chartPoints = listOf(
                                Offset(w * 0.0f, h * 0.85f),
                                Offset(w * 0.1f, h * 0.70f),
                                Offset(w * 0.2f, h * 0.75f),
                                Offset(w * 0.3f, h * 0.40f),
                                Offset(w * 0.4f, h * 0.55f),
                                Offset(w * 0.5f, h * 0.25f),
                                Offset(w * 0.6f, h * 0.35f),
                                Offset(w * 0.7f, h * 0.15f),
                                Offset(w * 0.8f, h * 0.45f),
                                Offset(w * 0.9f, h * 0.10f),
                                Offset(w * 1.0f, h * 0.05f)
                            )
                            
                            val path = Path().apply {
                                moveTo(chartPoints.first().x, chartPoints.first().y)
                                for (p in chartPoints) {
                                    lineTo(p.x, p.y)
                                }
                            }
                            
                            // Draw glowing fill gradient underneath line
                            val fillPath = Path().apply {
                                moveTo(chartPoints.first().x, h)
                                for (p in chartPoints) {
                                    lineTo(p.x, p.y)
                                }
                                lineTo(w, h)
                                close()
                            }
                            
                            drawPath(
                                path = fillPath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFF10B981).copy(alpha = 0.25f), Color.Transparent)
                                )
                            )
                            
                            // Draw primary stroke path
                            drawPath(
                                path = path,
                                color = Color(0xFF10B981),
                                style = Stroke(width = 3.dp.toPx())
                            )
                            
                            // Draw a glowing circle orb representing latest real-time point
                            drawCircle(
                                color = Color(0xFF34D399),
                                radius = 5.dp.toPx(),
                                center = chartPoints.last()
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 2.dp.toPx(),
                                center = chartPoints.last()
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("00:00", color = Color(0xFF475569), fontSize = 10.sp)
                            Text("06:00", color = Color(0xFF475569), fontSize = 10.sp)
                            Text("12:00", color = Color(0xFF475569), fontSize = 10.sp)
                            Text("18:00", color = Color(0xFF475569), fontSize = 10.sp)
                            Text("Hiện tại", color = Color(0xFF10B981), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Table Header Name + Category dynamic Tabs selector row (12 Collections)
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "BẢNG ĐIỀU HÀNH DỮ LIỆU CỐT LÕI (12 SERVICES)",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(collectionTabs.size) { index ->
                        val label = collectionTabs[index]
                        val isSelected = index == selectedTab
                        
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) Color(0xFF10B981) else Color(0xFF1E293B))
                                .border(
                                    1.dp,
                                    if (isSelected) Color.Transparent else Color(0xFF334155),
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { selectedTab = index }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else Color(0xFF94A3B8),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Live Data Search Box & Action Controls
            item {
                Spacer(modifier = Modifier.height(14.dp))
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Lọc nhanh ID, nội dung hoặc phòng ban...", color = Color(0xFF64748B), fontSize = 12.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E293B)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E293B),
                        unfocusedContainerColor = Color(0xFF1E293B),
                        focusedIndicatorColor = Color(0xFF10B981),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp),
                    singleLine = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Tìm kiếm", tint = Color(0xFF64748B))
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Real-time table content rendering matching selected category database
            val itemsForTab = adminDataState[selectedTab] ?: emptyList()
            val filteredItems = itemsForTab.filter {
                it.id.contains(searchQuery, ignoreCase = true) ||
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.submitter.contains(searchQuery, ignoreCase = true) ||
                it.department.contains(searchQuery, ignoreCase = true)
            }

            if (filteredItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Không tìm thấy hồ sơ nào khớp với bộ lọc.",
                            color = Color(0xFF64748B),
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                items(filteredItems.size) { idx ->
                    val docItem = filteredItems[idx]
                    
                    // Web-Styled document list card block representing React table records
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        border = BorderStroke(1.dp, Color(0xFF334155)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // First Row with ID and Time stamp
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = docItem.id,
                                    color = Color(0xFF10B981),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = docItem.time,
                                    color = Color(0xFF64748B),
                                    fontSize = 10.sp
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(6.dp))
                            
                            // Title & Description
                            Text(
                                text = docItem.title,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            // Submitter & Department Info
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Đệ trình: ${docItem.submitter}",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Box(modifier = Modifier.size(3.dp).clip(CircleShape).background(Color(0xFF475569)))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = docItem.department,
                                    color = Color(0xFF64748B),
                                    fontSize = 11.sp,
                                    fontStyle = FontStyle.Italic
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = Color(0xFF334155), thickness = 0.8.dp)
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            // Action controls and active status indicator
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when (docItem.status) {
                                                    "Đã duyệt", "Đã giải quyết", "Đã cấp chứng chỉ", "Đã số hóa" -> Color(0xFF10B981)
                                                    "Báo động đỏ", "Khẩn cấp", "Yêu cầu kỹ sư" -> Color(0xFFEF4444)
                                                    else -> Color(0xFFF59E0B)
                                                }
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = docItem.status,
                                        color = when (docItem.status) {
                                            "Đã duyệt", "Đã giải quyết", "Đã cấp chứng chỉ", "Đã số hóa" -> Color(0xFF10B981)
                                            "Báo động đỏ", "Khẩn cấp", "Yêu cầu kỹ sư" -> Color(0xFFEF4444)
                                            else -> Color(0xFFF59E0B)
                                        },
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                // Interactive Action buttons
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    if (docItem.status != "Đã duyệt" && docItem.status != "Đã giải quyết" && docItem.status != "Đã số hóa" && docItem.status != "Đã cấp chứng chỉ" && docItem.status != "Đã số hóa") {
                                        Button(
                                            onClick = {
                                                // Update status to solved in live map
                                                val updatedList = itemsForTab.map {
                                                    if (it.id == docItem.id) {
                                                        val nextStatus = when (selectedTab) {
                                                            0 -> "Đã duyệt"
                                                            4 -> "Đã giải quyết"
                                                            8 -> "Đã số hóa"
                                                            5 -> "Đã cấp chứng chỉ"
                                                            else -> "Đã giải quyết"
                                                        }
                                                        it.copy(status = nextStatus)
                                                    } else it
                                                }
                                                adminDataState = adminDataState.toMutableMap().apply {
                                                    put(selectedTab, updatedList)
                                                }
                                                coroutineScope.launch {
                                                    Toast.makeText(context, "Đã xử lý hồ sơ ${docItem.id} thành công!", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E)), // Teal Approve Button
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                            modifier = Modifier.height(26.dp)
                                        ) {
                                            Text("Giải Quyết", fontSize = 10.sp, color = Color.White)
                                        }
                                    }
                                    
                                    OutlinedButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                Toast.makeText(context, "Yêu cầu AI chẩn chẩn/thẩm định lại tệp ${docItem.id}...", Toast.LENGTH_LONG).show()
                                            }
                                        },
                                        border = BorderStroke(1.dp, Color(0xFF475569)),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier.height(26.dp)
                                    ) {
                                        Text("AI Thẩm Định Lại", fontSize = 10.sp, color = Color(0xFF94A3B8))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// Highly interactive and polished AI Camera Dashboard Module for Dak Lak Smart Cities
data class SimulatedVehicle(
    val plate: String,
    val type: String,
    val speed: Int,
    val isViolation: Boolean,
    val rx: Float, // relative x coord
    val ry: Float, // relative y coord
    val rw: Float, // relative width
    val rh: Float  // relative height
)

data class ViolationLog(
    val id: String,
    val plate: String,
    val type: String,
    val offense: String,
    val time: String,
    val location: String,
    val isProcessed: Boolean
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CameraDashboardScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    // Available Camera locations
    val cameraLocations = listOf(
        "Nga San Buon Ma Thuot (Cam Central 01)",
        "Nguyen Tat Thanh Plaza (Cam Speed 02)",
        "Nut Giao Duy Hoa Gateway (Cam ANPR 03)",
        "Vong xoay Km3 Dak Lak (Cam Traffic 04)"
    )
    var selectedCam by remember { mutableIntStateOf(0) }
    
    // Toggle active AI capabilities
    var shieldOnANPR by remember { mutableStateOf(true) }
    var shieldOnLanes by remember { mutableStateOf(true) }
    var shieldOnRadar by remember { mutableStateOf(true) }
    
    // Quick Chat entry integration
    var chatQuery by remember { mutableStateOf("") }
    
    // Simulation state that changes on clicking "Rescan Traffic" / "Quét Lại Dòng Xe"
    var scanVersion by remember { mutableIntStateOf(1) }
    
    val simulatedVehicles = remember(selectedCam, scanVersion) {
        when (selectedCam) {
            0 -> listOf(
                SimulatedVehicle("47A-390.28", "Xe Taxi", 42, false, 0.15f, 0.45f, 0.18f, 0.22f),
                SimulatedVehicle("47C-201.55", "Xe Tai Cat", 63, true, 0.55f, 0.38f, 0.24f, 0.28f), // violation in 50 limit!
                SimulatedVehicle("47F1-888.88", "Xe May", 35, false, 0.40f, 0.65f, 0.10f, 0.16f)
            )
            1 -> listOf(
                SimulatedVehicle("43B-012.39", "Xe Khach BMT", 52, false, 0.25f, 0.35f, 0.26f, 0.32f),
                SimulatedVehicle("47A-999.99", "Sieu xe Benz", 88, true, 0.60f, 0.48f, 0.20f, 0.24f), // massive speeding!
                SimulatedVehicle("19A-404.12", "SUV Trang", 48, false, 0.08f, 0.58f, 0.18f, 0.22f)
            )
            2 -> listOf(
                SimulatedVehicle("47C-129.80", "Xe Tai Ca phe", 40, false, 0.32f, 0.32f, 0.22f, 0.28f),
                SimulatedVehicle("47R2-401.11", "Xe May Cay", 25, false, 0.10f, 0.60f, 0.16f, 0.22f),
                SimulatedVehicle("47F-555.22", "Mo to PKL", 74, true, 0.65f, 0.55f, 0.12f, 0.18f) // Speeding
            )
            else -> listOf(
                SimulatedVehicle("47A-686.86", "Xe SUV", 45, false, 0.20f, 0.40f, 0.18f, 0.22f),
                SimulatedVehicle("47R1-002.13", "Mo to Vision", 58, true, 0.50f, 0.58f, 0.12f, 0.16f) // Speeding in 40 limit zone
            )
        }
    }
    
    // Active Violation Tickets Logs State
    var violationLogs by remember {
        mutableStateOf(
            listOf(
                ViolationLog("VIO-7933", "47C-201.55", "Xe Tai Cat", "Chay qua toc do (63/50 km/h)", "22/06 09:41", "Nga Sau BMT", false),
                ViolationLog("VIO-7934", "47A-999.99", "Sieu xe Benz", "Vuot den do & Toc do cao (88/60 km/h)", "22/06 09:35", "Nguyen Tat Thanh", false),
                ViolationLog("VIO-7935", "47F-555.22", "Mo to PKL", "Khong chap hanh tin hieu & Qua toc do", "22/06 09:22", "Cua Ngo Duy Hoa", false),
                ViolationLog("VIO-7936", "47D1-404.12", "Xe Ban Tai", "Cat lan dot ngot de vach lien", "22/06 08:50", "Vong xoay Km3", true)
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)) // Aesthetic Slate Dark Web Theme
    ) {
        // App top connection simulation bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E293B))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(Screen.Home) },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Quay lai",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "AI CAMERA CORE ENGINE",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.SansSerif
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF10B981)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Phan tich giao thong truc tuyen (Live Jetpack CPU)",
                        color = Color(0xFF10B981),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFEF4444).copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "ANPR MODEL ONLINE",
                    color = Color(0xFFEF4444),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Horizontal Selection Bar for Cameras
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "DANH SACH CAMERA GIAM SAT TOAN TINH DAK LAK",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(cameraLocations.size) { idx ->
                        val loc = cameraLocations[idx]
                        val isSelected = selectedCam == idx
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0xFF10B981) else Color(0xFF1E293B))
                                .clickable { selectedCam = idx }
                                .border(
                                    1.dp,
                                    if (isSelected) Color.Transparent else Color(0xFF334155),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = loc.substringBefore(" ("),
                                color = if (isSelected) Color.White else Color(0xFF94A3B8),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Simulated HUD Camera Viewport
            item {
                Spacer(modifier = Modifier.height(14.dp))
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, Color(0xFF1E293B))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF0F172A))
                    ) {
                        // Drawing static/background representation matching selected locations
                        // Procedural camera city landscape background drawing
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            
                            // Draw grey road lanes meeting at perspectivet convergence point
                            val roadPath = Path().apply {
                                moveTo(w * 0.45f, h * 0.2f)
                                lineTo(w * 0.55f, h * 0.2f)
                                lineTo(w * 0.95f, h)
                                lineTo(w * 0.05f, h)
                                close()
                            }
                            drawPath(roadPath, Color(0xFF334155))
                            
                            // Draw yellow lane strip divisions
                            drawLine(Color(0xFFF59E0B), Offset(w * 0.5f, h * 0.2f), Offset(w * 0.5f, h), strokeWidth = 3f)
                            drawLine(Color.White.copy(alpha = 0.5f), Offset(w * 0.47f, h * 0.2f), Offset(w * 0.25f, h), strokeWidth = 1.5f)
                            drawLine(Color.White.copy(alpha = 0.5f), Offset(w * 0.53f, h * 0.2f), Offset(w * 0.75f, h), strokeWidth = 1.5f)
                            
                            // Horizontal sky horizon backdrop
                            drawRect(
                                color = Color(0xFF1E3A8A).copy(alpha = 0.25f),
                                topLeft = Offset(0f, 0f),
                                size = androidx.compose.ui.geometry.Size(w, h * 0.2f)
                            )
                        }

                        // Drawing Live Detections Boxes over the canvas (Relative Coordinate Scaling)
                        simulatedVehicles.forEach { vehicle ->
                            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                                val vw = maxWidth
                                val vh = maxHeight
                                
                                val boxLeft = vw * vehicle.rx
                                val boxTop = vh * vehicle.ry
                                val boxWidth = vw * vehicle.rw
                                val boxHeight = vh * vehicle.rh
                                
                                val borderColor = if (vehicle.isViolation) Color(0xFFEF4444) else Color(0xFF10B981)
                                
                                // Draw bounding box with border
                                Box(
                                    modifier = Modifier
                                        .absoluteOffset(x = boxLeft, y = boxTop)
                                        .size(width = boxWidth, height = boxHeight)
                                        .border(1.5.dp, borderColor, RoundedCornerShape(4.dp))
                                        .background(borderColor.copy(alpha = 0.12f))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(2.dp),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        // Label tag block
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(borderColor)
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = "${vehicle.type} (${vehicle.speed} km/h)",
                                                color = Color.White,
                                                fontSize = 7.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        
                                        // License Plate Sub-Tag Anchor
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(Color.White)
                                                .border(0.5.dp, Color.Black, RoundedCornerShape(2.dp))
                                                .padding(horizontal = 4.dp, vertical = 0.5.dp)
                                        ) {
                                            Text(
                                                text = vehicle.plate,
                                                color = Color.Black,
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 7.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Overlay surveillance HUD markers (Corners brackets, REC circle, timestamp)
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val inset = 12.dp.toPx()
                            val len = 16.dp.toPx()
                            val weight = 2.dp.toPx()
                            
                            // Top Left corner bracket
                            drawLine(Color(0xFF38BDF8), Offset(inset, inset), Offset(inset + len, inset), strokeWidth = weight)
                            drawLine(Color(0xFF38BDF8), Offset(inset, inset), Offset(inset, inset + len), strokeWidth = weight)
                            
                            // Top Right corner bracket
                            drawLine(Color(0xFF38BDF8), Offset(w - inset, inset), Offset(w - inset - len, inset), strokeWidth = weight)
                            drawLine(Color(0xFF38BDF8), Offset(w - inset, inset), Offset(w - inset, inset + len), strokeWidth = weight)
                            
                            // Bottom Left bracket
                            drawLine(Color(0xFF38BDF8), Offset(inset, h - inset), Offset(inset + len, h - inset), strokeWidth = weight)
                            drawLine(Color(0xFF38BDF8), Offset(inset, h - inset), Offset(inset, h - inset - len), strokeWidth = weight)
                            
                            // Bottom Right bracket
                            drawLine(Color(0xFF38BDF8), Offset(w - inset, h - inset), Offset(w - inset - len, h - inset), strokeWidth = weight)
                            drawLine(Color(0xFF38BDF8), Offset(w - inset, h - inset), Offset(w - inset, h - inset - len), strokeWidth = weight)
                        }

                        // Text indicators layer
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color.Red)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "LIVE FEED [${cameraLocations[selectedCam].takeLast(7).dropLast(1)}]",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Text(
                                text = "FPS: 30 // HD1080p // 22-06-2026 09:42",
                                color = Color(0xFF38BDF8),
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Speed indicator at bottom corner
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Black.copy(alpha = 0.75f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "GIOP HAN TOC DO: ${if (selectedCam == 0) "50" else if (selectedCam == 1) "60" else if (selectedCam == 2) "80" else "40"} km/h",
                                color = Color(0xFFF59E0B),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            // Quick Simulation Actions Row
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            scanVersion++
                            Toast.makeText(context, "Da cap nhat quet luong xe moi!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Quet lai", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Quet Lai Dong Xe", fontSize = 11.sp, color = Color.White)
                    }
                    Button(
                        onClick = {
                            // Find any violation in vehicles and add it to list
                            val newVios = simulatedVehicles.filter { it.isViolation }.map {
                                ViolationLog(
                                    id = "VIO-" + (7937..9999).random(),
                                    plate = it.plate,
                                    type = it.type,
                                    offense = "Tu dong phat hien boi ANPR Speed: ${it.speed} km/h",
                                    time = "Bay gio 09:42",
                                    location = cameraLocations[selectedCam].substringBefore(" ("),
                                    isProcessed = false
                                )
                            }
                            if (newVios.isNotEmpty()) {
                                violationLogs = newVios + violationLogs
                                Toast.makeText(context, "Phat hien ${newVios.size} vi pham moi!", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Khong co loi vi pham moi trong luong xe hien tai.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBC1C1C)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = "Ghi hinh", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Chup Vi Pham AI", fontSize = 11.sp, color = Color.White)
                    }
                }
            }

            // Traffic Telemetry Stats Block
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Traffic density
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Hieu suat dong xe", color = Color(0xFF64748B), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("72 xe/phut", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("On dinh luu thong", color = Color(0xFF10B981), fontSize = 10.sp)
                        }
                    }
                    // AI violation rating
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Ty le vi pham AI", color = Color(0xFF64748B), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("2.8% phat nguoi", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Thap hon tuan truoc", color = Color(0xFF38BDF8), fontSize = 10.sp)
                        }
                    }
                }
            }

            // AI Checkboxes/Toggles Core
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    border = BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "DIEU CHINH CHE DO AN NINH AI",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Nhan dien bien so tu dong (ANPR Model)", color = Color(0xFF94A3B8), fontSize = 12.sp)
                            Switch(
                                checked = shieldOnANPR,
                                onCheckedChange = { shieldOnANPR = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF10B981), checkedTrackColor = Color(0xFF10B981).copy(alpha = 0.5f))
                            )
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Phat hien de vach / lan duong", color = Color(0xFF94A3B8), fontSize = 12.sp)
                            Switch(
                                checked = shieldOnLanes,
                                onCheckedChange = { shieldOnLanes = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF10B981), checkedTrackColor = Color(0xFF10B981).copy(alpha = 0.5f))
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Kiem tra toc do radar tu dong", color = Color(0xFF94A3B8), fontSize = 12.sp)
                            Switch(
                                checked = shieldOnRadar,
                                onCheckedChange = { shieldOnRadar = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF10B981), checkedTrackColor = Color(0xFF10B981).copy(alpha = 0.5f))
                            )
                        }
                    }
                }
            }

            // Real-time Traffic Tickets/Notice Logs Table
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "LICH SU PHAT NGUOI MAT PHONG TOAN AI",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(violationLogs.size) { index ->
                val log = violationLogs[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    border = BorderStroke(1.dp, if (log.isProcessed) Color(0xFF334155) else Color(0xFFEF4444).copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = log.plate,
                                color = Color(0xFF38BDF8),
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (log.isProcessed) Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFFEF4444).copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (log.isProcessed) "Da Gui Phat" else "Cho Xu Ly",
                                    color = if (log.isProcessed) Color(0xFF10B981) else Color(0xFFEF4444),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Phuong tien: ${log.type} // Vi tri: ${log.location}", color = Color(0xFF94A3B8), fontSize = 11.sp)
                        Text(text = "Dong thai: ${log.offense}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = log.time, color = Color(0xFF64748B), fontSize = 10.sp)
                            
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (!log.isProcessed) {
                                    Button(
                                        onClick = {
                                            // Process ticket
                                            violationLogs = violationLogs.map {
                                                if (it.id == log.id) it.copy(isProcessed = true) else it
                                            }
                                            Toast.makeText(context, "Da xac thuc gui giay phat nguoi den chu phuong tien ${log.plate}!", Toast.LENGTH_LONG).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier.height(26.dp)
                                    ) {
                                        Text("Gui Phat Nguoi", fontSize = 10.sp, color = Color.White)
                                    }
                                }
                                OutlinedButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            Toast.makeText(context, "Da xoa bo ho so lỗi bien so ${log.plate}.", Toast.LENGTH_SHORT).show()
                                        }
                                        violationLogs = violationLogs.filter { it.id != log.id }
                                    },
                                    border = BorderStroke(1.dp, Color(0xFF475569)),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(26.dp)
                                ) {
                                    Text("Huy bo", fontSize = 10.sp, color = Color(0xFF94A3B8))
                                }
                            }
                        }
                    }
                }
            }

            // Quick Chat Assist Template Section
            item {
                Spacer(modifier = Modifier.height(22.dp))
                Text(
                    text = "TRA CUU TRAM CAMERA AI (TIEP GOI TRUONG TRONG)",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    border = BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = "Tro ly", tint = Color(0xFF38BDF8), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Boi canh hoi dap Luat an ninh giao thong Buon Ma Thuot", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        val aiPrompts = listOf(
                            "Quy dinh phan lan duong xe may kieu moi o Buon Ma Thuot?",
                            "Muc phat nguoi khi vao nga sau Buon Ma Thuot qua toc do?",
                            "Cac phuong phap camera AI do tim xe qua tai trong vung tay nguyen?"
                        )
                        
                        aiPrompts.forEach { prompt ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF0F172A))
                                    .clickable {
                                        chatQuery = prompt
                                        coroutineScope.launch {
                                            viewModel.navigateTo(Screen.Alert) // Switch to AI chat assist
                                            Toast.makeText(context, "Da gui cau hoi: \"$prompt\" đen tro ly!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .padding(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(prompt, color = Color(0xFF94A3B8), fontSize = 11.sp)
                                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Chon", tint = Color(0xFF10B981), modifier = Modifier.size(12.dp))
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }
}



