package com.wiom.csp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.ui.theme.*
import com.wiom.csp.util.t

// ═══════════════════════════════════════════════════════════════════════════
// Helper: Fee Detail Row (used in Bank Details and other Phase 2 screens)
// ═══════════════════════════════════════════════════════════════════════════

@Composable
internal fun FeeDetailRow(label: String, value: String, valueColor: Color = WiomText) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 12.sp, color = WiomTextSec)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Helper: Upload Row (local to Phase 2 screens — ISP + Shop Photos)
// ═══════════════════════════════════════════════════════════════════════════

@Composable
internal fun Phase2UploadRow(icon: String, label: String, isUploaded: Boolean, onUpload: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onUpload),
        shape = RoundedCornerShape(12.dp),
        color = if (isUploaded) com.wiom.csp.ui.theme.WiomPositive100 else Color.White,
        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("$icon $label", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = WiomText)
                Text(
                    if (isUploaded) "${t("अपलोड हो गया", "Uploaded")} ✓" else t("टैप करें", "Tap to Upload"),
                    fontSize = 12.sp, color = if (isUploaded) WiomPositive else WiomHint,
                )
            }
            OutlinedButton(
                onClick = onUpload,
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = if (isUploaded) WiomPositive else WiomPrimary),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            ) {
                Text(
                    if (isUploaded) t("बदलें", "Update") else t("अपलोड", "Upload"),
                    fontSize = 12.sp, fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
