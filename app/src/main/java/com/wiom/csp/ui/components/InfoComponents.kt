package com.wiom.csp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

// Info box (ibox-p, ibox-g, ibox-o)
@Composable
fun InfoBox(
    icon: String,
    text: String,
    type: InfoBoxType = InfoBoxType.INFO,
    modifier: Modifier = Modifier,
) {
    val (bg, textColor) = when (type) {
        InfoBoxType.INFO -> WiomInfo100 to WiomTextSec
        InfoBoxType.SUCCESS -> WiomPositive100 to Color(0xFF005C30)
        InfoBoxType.WARNING -> WiomWarning200 to WiomWarning700
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(icon, fontSize = 16.sp)
        Text(text, fontSize = 14.sp, color = textColor, lineHeight = 20.sp)
    }
}

enum class InfoBoxType { INFO, SUCCESS, WARNING }

// Trust badge
@Composable
fun TrustBadge(icon: String, text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(888.dp))
            .background(WiomPositive100)
            .border(1.dp, WiomPositive300, RoundedCornerShape(888.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(icon, fontSize = 14.sp)
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = WiomPositive)
    }
}

// Chip
@Composable
fun WiomChip(
    text: String,
    backgroundColor: Color = WiomPositive100,
    textColor: Color = WiomPositive,
) {
    Text(
        text,
        modifier = Modifier
            .clip(RoundedCornerShape(888.dp))
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = textColor,
    )
}

// Section header
@Composable
fun SectionHeader(text: String) {
    Text(
        text,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = WiomTextSec,
        letterSpacing = 0.5.sp,
    )
}
