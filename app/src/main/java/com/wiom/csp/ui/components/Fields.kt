package com.wiom.csp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.ui.theme.*

// Input field label
@Composable
fun FieldLabel(text: String) {
    Text(
        text,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = WiomTextSec,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

// Styled text field
@Composable
fun WiomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    isVerified: Boolean = false,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onFocusChanged: ((Boolean) -> Unit)? = null,
    keyboardOptions: androidx.compose.foundation.text.KeyboardOptions = androidx.compose.foundation.text.KeyboardOptions.Default,
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = if (errorMessage != null) 4.dp else 12.dp)
                .then(
                    if (onFocusChanged != null) Modifier.onFocusChanged { onFocusChanged(it.isFocused) }
                    else Modifier
                ),
            readOnly = readOnly,
            visualTransformation = visualTransformation,
            placeholder = { Text(placeholder, color = WiomHint) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = when {
                    isError -> WiomNegative
                    isVerified -> WiomPositive
                    else -> WiomBorderFocus
                },
                unfocusedBorderColor = when {
                    isError -> WiomNegative
                    isVerified -> WiomPositive
                    else -> WiomBorderInput
                },
                focusedContainerColor = when {
                    isError -> WiomNegative100
                    isVerified -> WiomPositive100
                    else -> Color.White
                },
                unfocusedContainerColor = when {
                    isError -> WiomNegative100
                    isVerified -> WiomPositive100
                    else -> Color.White
                },
            ),
            trailingIcon = if (isVerified) {
                { Icon(Icons.Default.Check, "Verified", tint = WiomPositive) }
            } else null,
            singleLine = true,
            keyboardOptions = keyboardOptions,
        )
        if (errorMessage != null) {
            Text(
                errorMessage,
                fontSize = 12.sp,
                color = WiomNegative,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
            )
        }
    }
}

// ─── Field Validation Error ─────────────────────────────────────
@Composable
fun FieldValidationError(error: String?) {
    if (error != null) {
        Text(
            text = error,
            color = WiomNegative,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(start = 4.dp, top = 2.dp, bottom = 8.dp)
        )
    }
}
