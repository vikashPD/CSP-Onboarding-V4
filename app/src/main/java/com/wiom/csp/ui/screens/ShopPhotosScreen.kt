package com.wiom.csp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.wiom.csp.ui.components.*
import com.wiom.csp.ui.theme.*
import com.wiom.csp.ui.viewmodel.*
import com.wiom.csp.util.t

// ═══════════════════════════════════════════════════════════════════════════
// Screen 8: Shop & Equipment Photos
// ═══════════════════════════════════════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopPhotosScreen(viewModel: PhotosViewModel, onNext: () -> Unit, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    var uploadTarget by remember { mutableStateOf<String?>(null) } // "shop" or "equip"
    var sampleDocTarget by remember { mutableStateOf<String?>(null) } // "shop" or "equip"
    val uploadSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sampleSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Upload Bottom Sheet (Camera + Gallery + Pro Tips)
    if (uploadTarget != null) {
        val isShop = uploadTarget == "shop"
        val sheetTitle = if (isShop) t("दुकान की फ़ोटो", "Shop Front Photo") else t("राउटर/इक्विपमेंट फ़ोटो", "Router/Equipment Photo")
        ModalBottomSheet(
            onDismissRequest = { uploadTarget = null },
            sheetState = uploadSheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        ) {
            Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 24.dp)) {
                Text(sheetTitle, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = WiomText, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                // Camera option
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).border(1.dp, WiomBorder, RoundedCornerShape(12.dp))
                        .clickable { if (isShop) viewModel.onShopPhotoUploaded() else viewModel.finishEquipmentUpload(); uploadTarget = null }
                        .padding(14.dp, 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("\uD83D\uDCF8", fontSize = 24.sp)
                    Column {
                        Text(t("कैमरा से फ़ोटो लें", "Take Photo from Camera"), fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = WiomText)
                        Text(t("अभी फ़ोटो खींचें", "Capture photo now"), fontSize = 12.sp, color = WiomTextSec)
                    }
                }
                Spacer(Modifier.height(8.dp))
                // Gallery option
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).border(1.dp, WiomBorder, RoundedCornerShape(12.dp))
                        .clickable { if (isShop) viewModel.onShopPhotoUploaded() else viewModel.finishEquipmentUpload(); uploadTarget = null }
                        .padding(14.dp, 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("\uD83D\uDDBC\uFE0F", fontSize = 24.sp)
                    Column {
                        Text(t("गैलरी से अपलोड करें", "Upload from Gallery"), fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = WiomText)
                        Text(t("फ़ोन की गैलरी से चुनें", "Choose from phone gallery"), fontSize = 12.sp, color = WiomTextSec)
                    }
                }
                Spacer(Modifier.height(12.dp))
                // Pro Tips
                Surface(shape = RoundedCornerShape(8.dp), color = WiomBgSec, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(10.dp, 12.dp)) {
                        Text("\uD83D\uDCA1 ${t("प्रो टिप्स", "Pro Tips")}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WiomText)
                        Spacer(Modifier.height(6.dp))
                        val tips = if (isShop) listOf(
                            t("पूरी दुकान के सामने की फ़ोटो ज़रूरी", "Complete Shop Front photo required"),
                            t("बिज़नेस का नाम साफ़ दिखना चाहिए", "Business name should be clearly visible"),
                            t("बिज़नेस LCO के तौर पर मैच होना चाहिए", "Line of business should match as an LCO"),
                        ) else listOf(
                            t("दस्तावेज़ साफ़ दिखना चाहिए", "Document should be clearly visible"),
                            t("सभी बिज़नेस डिटेल्स मैच होनी चाहिए", "All business details should match"),
                            t("दस्तावेज़ की एक्सपायर नहीं होना चाहिए", "Document should not be expired"),
                        )
                        tips.forEach {
                            Row(Modifier.padding(bottom = 4.dp)) {
                                Text("✓ ", fontSize = 11.sp, color = WiomPositive)
                                Text(it, fontSize = 11.sp, color = WiomTextSec, lineHeight = 16.sp)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(t("रद्द करें", "Cancel"), fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = WiomTextSec, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().clickable { uploadTarget = null }.padding(10.dp))
            }
        }
    }

    // Sample Document Bottom Sheet
    if (sampleDocTarget != null) {
        val isShop = sampleDocTarget == "shop"
        val sampleTitle = if (isShop) t("सैंपल दुकान फ़ोटो", "Sample Shop Front Photo") else t("सैंपल इक्विपमेंट फ़ोटो", "Sample Equipment Photo")
        val sampleRes = if (isShop) com.wiom.csp.R.drawable.sample_shop else com.wiom.csp.R.drawable.sample_equip
        ModalBottomSheet(
            onDismissRequest = { sampleDocTarget = null },
            sheetState = sampleSheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        ) {
            Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(sampleTitle, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WiomText, textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = sampleRes),
                    contentDescription = sampleTitle,
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).border(1.dp, WiomBorder, RoundedCornerShape(8.dp)),
                    contentScale = androidx.compose.ui.layout.ContentScale.FillWidth,
                )
                Spacer(Modifier.height(16.dp))
                WiomButton(t("समझ गया", "Got it"), onClick = { sampleDocTarget = null })
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        AppHeader(
            title = t("वेरिफिकेशन", "Verification"),
            onBack = onBack,
            rightText = t("स्टेप 4/5", "Step 4/5"),
        )
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            Text("\uD83C\uDFEA ${t("दुकान वेरिफिकेशन", "Shop Verification")}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = WiomText)
            Spacer(Modifier.height(4.dp))
            Text(
                t("दुकान के सामने की फ़ोटो और इंटरनेट इक्विपमेंट/राउटर की फ़ोटो अपलोड करें", "Upload shop front photo and Internet equipment/Router photos"),
                fontSize = 13.sp, color = WiomTextSec, lineHeight = 18.sp,
            )
            Spacer(Modifier.height(14.dp))

            // Shop Front Photo
            FieldLabel(t("दुकान की फ़ोटो", "Shop Front Photo"))
            Phase2UploadRow(
                icon = "\uD83C\uDFEA",
                label = t("दुकान की फ़ोटो", "Shop Front Photo"),
                isUploaded = state.shopPhotoUploaded,
                onUpload = { if (state.shopPhotoUploaded) viewModel.resetShopPhoto() else uploadTarget = "shop" },
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "\uD83D\uDCCB ${t("सैंपल दस्तावेज़ देखें", "View sample document")}",
                fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = WiomPrimary,
                textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline,
                modifier = Modifier.clickable { sampleDocTarget = "shop" },
            )
            Spacer(Modifier.height(12.dp))

            // Equipment Photos
            FieldLabel(t("राउटर/इक्विपमेंट फ़ोटो", "Router/Equipment Photos"))
            Phase2UploadRow(
                icon = "\uD83D\uDCE1",
                label = t("राउटर/इक्विपमेंट फ़ोटो", "Router/Equipment Photos"),
                isUploaded = state.equipmentPhotoUploaded,
                onUpload = { if (state.equipmentPhotoUploaded) viewModel.resetEquipmentPhotos() else uploadTarget = "equip" },
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "\uD83D\uDCCB ${t("सैंपल दस्तावेज़ देखें", "View sample document")}",
                fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = WiomPrimary,
                textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline,
                modifier = Modifier.clickable { sampleDocTarget = "equip" },
            )
        }
        BottomBar {
            WiomButton(
                if (state.bothUploaded) t("वेरिफिकेशन के लिए सबमिट करें", "Submit for Verification") else t("दोनों फ़ोटो अपलोड करें", "Upload both photos"),
                onClick = if (state.bothUploaded) onNext else { {} },
                enabled = state.bothUploaded,
            )
        }
    }
}
