package com.wiom.csp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.ui.theme.*
import com.wiom.csp.util.t

// ─── Loading Overlay ────────────────────────────────────────────
@Composable
fun LoadingOverlay(messageHi: String, messageEn: String, isVisible: Boolean) {
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = WiomPrimary, strokeWidth = 4.dp)
                Spacer(Modifier.height(16.dp))
                Text(
                    t(messageHi, messageEn),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

// ─── Loading Overlay (state-driven convenience overload) ────────
@Composable
fun LoadingOverlay(message: String = "") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = WiomPrimary)
            if (message.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(message, color = Color.White, fontSize = 14.sp)
            }
        }
    }
}
