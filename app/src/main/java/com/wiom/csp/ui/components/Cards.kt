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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.ui.theme.*
import com.wiom.csp.util.t

// Card component
@Composable
fun WiomCard(
    modifier: Modifier = Modifier,
    borderColor: Color = WiomBorder,
    backgroundColor: Color = Color.White,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        shadowElevation = 1.dp,
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

// ─── Error Card ─────────────────────────────────────────────────
@Composable
fun ErrorCard(
    icon: String,
    titleHi: String,
    titleEn: String,
    messageHi: String,
    messageEn: String,
    type: String = "error",
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val (borderColor, bgColor, titleColor) = when (type) {
        "warning" -> Triple(WiomWarning, WiomWarning200, WiomWarning700)
        "info" -> Triple(WiomInfo, WiomInfo100, WiomInfo)
        else -> Triple(WiomNegative, WiomNegative100, WiomNegative)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        Text("$icon ${t(titleHi, titleEn)}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = titleColor)
        Spacer(Modifier.height(4.dp))
        Text(t(messageHi, messageEn), fontSize = 14.sp, color = WiomText, lineHeight = 20.sp)
        if (content != null) {
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

// ─── Retry Card ─────────────────────────────────────────────────
@Composable
fun RetryCard(
    titleHi: String, titleEn: String,
    messageHi: String, messageEn: String,
    onRetry: () -> Unit
) {
    WiomCard {
        Column {
            Text(t(titleHi, titleEn), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = WiomWarning)
            Spacer(modifier = Modifier.height(4.dp))
            Text(t(messageHi, messageEn), fontSize = 14.sp, color = WiomText, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(12.dp))
            WiomButton(
                text = t("\u092B\u093F\u0930 \u0938\u0947 \u0915\u094B\u0936\u093F\u0936 \u0915\u0930\u0947\u0902", "Retry"),
                onClick = onRetry
            )
        }
    }
}

// ─── Empty State Card ───────────────────────────────────────────
@Composable
fun EmptyStateCard(
    emoji: String,
    titleHi: String, titleEn: String,
    subtitleHi: String, subtitleEn: String
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 48.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(t(titleHi, titleEn), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = WiomText, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(4.dp))
        Text(t(subtitleHi, subtitleEn), fontSize = 14.sp, color = WiomTextSec, textAlign = TextAlign.Center)
    }
}

// Module card for training
@Composable
fun ModuleCard(
    icon: String,
    title: String,
    subtitle: String,
    isDone: Boolean = false,
    isCurrent: Boolean = false,
    badgeText: String = "",
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                when {
                    isDone -> WiomPositive100
                    isCurrent -> WiomPrimaryLight
                    else -> Color.White
                }
            )
            .border(
                1.dp,
                when {
                    isDone -> WiomPositive300
                    isCurrent -> WiomPrimary
                    else -> WiomBorder
                },
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isDone) WiomPositive100 else WiomWarning200),
            contentAlignment = Alignment.Center,
        ) {
            Text(icon, fontSize = 16.sp)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = WiomText)
            Text(subtitle, fontSize = 12.sp, color = WiomTextSec)
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isDone) WiomPositive100 else WiomWarning200)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                if (isDone) "\u2713" else badgeText,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDone) WiomPositive else WiomWarning700,
            )
        }
    }
}

// Quick action card for Go Live screen
@Composable
fun QuickActionCard(icon: String, title: String, subtitle: String, onClick: () -> Unit = {}) {
    WiomCard(modifier = Modifier.clickable(onClick = onClick)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(icon, fontSize = 20.sp)
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = WiomText)
                Text(subtitle, fontSize = 12.sp, color = WiomTextSec)
            }
        }
    }
}
