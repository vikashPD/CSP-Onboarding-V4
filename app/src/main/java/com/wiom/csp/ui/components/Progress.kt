package com.wiom.csp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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

// Progress bar
@Composable
fun WiomProgressBar(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(WiomBorderInput)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .clip(RoundedCornerShape(2.dp))
                .background(WiomPositive)
        )
    }
}

// Checklist item
@Composable
fun ChecklistItem(
    text: String,
    subtitle: String? = null,
    isDone: Boolean = true,
    isWaiting: Boolean = false,
    isLast: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isDone -> WiomPositive
                        isWaiting -> WiomWarning200
                        else -> WiomBgSec
                    }
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                if (isDone) "\u2713" else "\u22EF",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDone) Color.White else WiomWarning700,
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = WiomText)
            if (subtitle != null) {
                Text(subtitle, fontSize = 12.sp, color = if (isWaiting) WiomWarning700 else WiomTextSec)
            }
        }
    }
}

// Stepper dots
@Composable
fun StepperDots(total: Int, current: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(WiomBgSec)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
    ) {
        repeat(total) { i ->
            Box(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .widthIn(max = 32.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        when {
                            i < current -> WiomPositive
                            i == current -> WiomPrimary
                            else -> WiomBorderInput
                        }
                    )
            )
        }
    }
}

// Verification row
@Composable
fun VerificationItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("\u2713", fontSize = 14.sp, color = WiomPositive, fontWeight = FontWeight.Bold)
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = WiomPositive)
    }
}

// Amount display box
@Composable
fun AmountBox(amount: String, label: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(WiomBgSec)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(amount, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = WiomText)
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 14.sp, color = WiomTextSec)
    }
}
