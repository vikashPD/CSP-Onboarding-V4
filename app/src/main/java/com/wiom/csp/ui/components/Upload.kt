package com.wiom.csp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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

// Upload row
@Composable
fun UploadRow(
    icon: String,
    name: String,
    statusText: String,
    isVerified: Boolean = false,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isVerified) WiomPositive100 else Color.White)
            .border(
                1.dp,
                if (isVerified) WiomPositive300 else WiomBorder,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text("$icon $name", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = WiomText)
            Text(
                statusText,
                fontSize = 12.sp,
                color = if (isVerified) WiomPositive else WiomHint,
                fontWeight = if (isVerified) FontWeight.SemiBold else FontWeight.Normal,
            )
        }
        if (isVerified) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(WiomPositive100)
                    .border(1.dp, WiomPositive, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("\u2713", color = WiomPositive, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

// ─── Upload Row Error ───────────────────────────────────────────
@Composable
fun UploadRowError(
    icon: String,
    name: String,
    statusText: String,
    isError: Boolean = false,
    isWarning: Boolean = false,
) {
    val (borderColor, bgColor, statusColor) = when {
        isError -> Triple(WiomNegative, WiomNegative100, WiomNegative)
        isWarning -> Triple(WiomWarning, WiomWarning200, WiomWarning700)
        else -> Triple(WiomBorder, Color.White, WiomHint)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text("$icon $name", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = WiomText)
            Text(statusText, fontSize = 12.sp, color = statusColor, fontWeight = FontWeight.SemiBold)
        }
        if (isError) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(WiomNegative100)
                    .border(1.dp, WiomNegative, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("\u2717", color = WiomNegative, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
        if (isWarning) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(WiomWarning200)
                    .border(1.dp, WiomWarning, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("\u26A0", color = WiomWarning700, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}
