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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.ui.theme.*

// OTP input row
@Composable
fun OtpRow(
    values: List<String> = listOf("4", "7", "2", "9"),
    isError: Boolean = false,
    isExpired: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
    ) {
        values.forEach { digit ->
            Box(
                modifier = Modifier
                    .size(48.dp, 56.dp)
                    .then(
                        if (isExpired) Modifier.border(2.dp, WiomHint, RoundedCornerShape(12.dp))
                        else if (isError) Modifier.border(2.dp, WiomNegative, RoundedCornerShape(12.dp))
                        else Modifier.border(2.dp, WiomBorderFocus, RoundedCornerShape(12.dp))
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    digit,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isExpired) WiomText.copy(alpha = 0.4f) else WiomText,
                )
            }
        }
    }
}

// Interactive OTP input row with hidden TextField
@Composable
fun OtpInputRow(
    digits: List<String>,
    onDigitsChange: (List<String>) -> Unit,
) {
    // Hidden text field that captures keyboard input
    val combinedValue = digits.joinToString("")
    var focusRequester = remember { androidx.compose.ui.focus.FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
    ) {
        // Hidden input field
        androidx.compose.foundation.text.BasicTextField(
            value = combinedValue,
            onValueChange = { newValue ->
                val filtered = newValue.filter { it.isDigit() }.take(4)
                val newDigits = List(4) { i -> filtered.getOrElse(i) { ' ' }.toString().trim() }
                onDigitsChange(newDigits)
            },
            modifier = Modifier
                .matchParentSize()
                .then(Modifier.alpha(0f))
                .focusRequester(focusRequester),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
            ),
            singleLine = true,
        )

        // Visible OTP boxes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        ) {
            digits.forEachIndexed { index, digit ->
                val isFocused = combinedValue.length == index
                Box(
                    modifier = Modifier
                        .size(48.dp, 56.dp)
                        .border(
                            2.dp,
                            if (digit.isNotEmpty()) WiomPositive
                            else if (isFocused) WiomPrimary
                            else WiomBorderFocus,
                            RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (digit.isNotEmpty()) WiomPositive100 else Color.White)
                        .clickable { focusRequester.requestFocus() },
                    contentAlignment = Alignment.Center,
                ) {
                    if (digit.isNotEmpty()) {
                        Text(digit, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText)
                    } else if (isFocused) {
                        // Blinking cursor
                        Text("|", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomPrimary)
                    }
                }
            }
        }
    }

    // Auto-focus on composition
    LaunchedEffect(Unit) {
        try { focusRequester.requestFocus() } catch (_: Exception) {}
    }
}
