package skyfiles.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Splash screen with loading animation and app branding.
 */
@Composable
fun SplashScreen(
    onComplete: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(0f) }
    
    // Animate progress from 0 to 1
    val progressAnimation by animateFloatAsState(
        targetValue = if (isLoading) 1f else 0f,
        animationSpec = tween(2000, easing = LinearEasing),
        finishedListener = { isLoading = false }
    )
    
    // Fade out animation
    val alpha by animateFloatAsState(
        targetValue = if (isLoading) 1f else 0f,
        animationSpec = tween(500)
    )
    
    LaunchedEffect(Unit) {
        // Simulate loading time
        delay(2500)
        onComplete()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .alpha(alpha),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App logo placeholder (cloud/folder motif)
            Text(
                text = "☁️📁",
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // App name
            Text(
                text = "SkyFiles",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Tagline
            Text(
                text = "Advanced File Explorer",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            // Loading progress bar
            LinearProgressIndicator(
                progress = progressAnimation,
                modifier = Modifier
                    .width(200.dp)
                    .height(4.dp),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Loading text
            Text(
                text = "Loading...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Version info
            Text(
                text = "Made with ❤️ in India 🇮🇳",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

