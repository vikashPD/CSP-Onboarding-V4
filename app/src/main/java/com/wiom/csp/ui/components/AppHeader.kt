package com.wiom.csp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.ui.theme.*

// Status bar mockup
@Composable
fun StatusBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(WiomHeader)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("9:41", color = WiomSurface, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
        Text("100%", color = WiomSurface, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}

// App header bar
@Composable
fun AppHeader(
    title: String,
    onBack: (() -> Unit)? = null,
    rightText: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth().background(WiomHeader)) {
        Spacer(Modifier.windowInsetsPadding(WindowInsets.statusBars))
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(WiomHeader)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack, modifier = Modifier.size(24.dp)) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = WiomSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
        }
        Text(
            title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = WiomSurface,
            modifier = Modifier.weight(1f)
        )
        if (rightText != null) {
            Text(rightText, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = WiomSurface.copy(alpha = 0.5f))
        }
        // मदद (Help) button — opens dialer
        val helpContext = androidx.compose.ui.platform.LocalContext.current
        Spacer(Modifier.width(8.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(888.dp))
                .background(Color.White.copy(alpha = 0.1f))
                .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(888.dp))
                .clickable {
                    val intent = android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse("tel:7836811111"))
                    helpContext.startActivity(intent)
                }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text("\uD83D\uDCDE", fontSize = 10.sp)
            Spacer(Modifier.width(3.dp))
            Text(
                com.wiom.csp.util.t("\u092E\u0926\u0926", "Help"),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = WiomSurface,
            )
        }
        // Language toggle button
        Spacer(Modifier.width(4.dp))
        Text(
            if (com.wiom.csp.util.Lang.isHindi) "English" else "\u0939\u093F\u0902\u0926\u0940",
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = WiomSurface,
            modifier = Modifier
                .clip(RoundedCornerShape(888.dp))
                .background(Color.White.copy(alpha = 0.1f))
                .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(888.dp))
                .clickable { com.wiom.csp.util.Lang.toggle() }
                .padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}
