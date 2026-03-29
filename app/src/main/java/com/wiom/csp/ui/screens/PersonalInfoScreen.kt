package com.wiom.csp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.data.OnboardingState
import com.wiom.csp.ui.components.*
import com.wiom.csp.ui.theme.*
import com.wiom.csp.util.t
import com.wiom.csp.ui.viewmodel.*

// Screen 2: Personal & Business Info
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(viewModel: PersonalInfoViewModel, onNext: () -> Unit, onBack: () -> Unit) {
    // Entity type dropdown state
    var dropdownExpanded by remember { mutableStateOf(false) }
    val entityOptions = listOf(t("प्रोप्राइटरशिप (Proprietorship)", "Proprietorship"))

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        AppHeader(
            title = t("रजिस्ट्रेशन", "Registration"),
            onBack = onBack,
            rightText = t("स्टेप 1/3", "Step 1/3")
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                t("पर्सनल और बिज़नेस जानकारी", "Personal & Business Info"),
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                t("आपका Wiom अकाउंट बनाने के लिए हमें इन विवरणों की ज़रूरीता है", "We require these details for creating your Wiom Account"),
                fontSize = 14.sp, color = WiomTextSec,
            )
            Spacer(Modifier.height(16.dp))

            FieldLabel(t("पूरा नाम (आधार अनुसार)", "Full Name (as per Aadhaar)"))
            WiomTextField(
                value = OnboardingState.personalName,
                onValueChange = { OnboardingState.personalName = it },
                placeholder = t("उदाहरण: राजेश कुमार", "Example: Rajesh Kumar"),
            )

            FieldLabel(t("ईमेल आईडी", "Email ID"))
            WiomTextField(
                value = OnboardingState.personalEmail,
                onValueChange = { OnboardingState.personalEmail = it },
                placeholder = t("उदाहरण: rajesh@email.com", "Example: rajesh@email.com"),
            )

            FieldLabel(t("बिज़नेस टाइप", "Business Entity Type"))
            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = it },
            ) {
                OutlinedTextField(
                    value = OnboardingState.entityType,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text(t("बिज़नेस टाइप चुनें", "Select business type"), color = WiomHint) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = WiomBorderInput,
                        focusedBorderColor = WiomBorderFocus,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    ),
                    singleLine = true,
                )
                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false },
                ) {
                    entityOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                OnboardingState.entityType = option
                                dropdownExpanded = false
                            },
                        )
                    }
                }
            }

            FieldLabel(t("व्यापार का नाम", "Business Name"))
            WiomTextField(
                value = OnboardingState.tradeName,
                onValueChange = { OnboardingState.tradeName = it },
                placeholder = t("उदाहरण: राजेश टेलीकॉम", "Example: Rajesh Telecom"),
            )
        }
        BottomBar {
            val allFilled = OnboardingState.personalName.isNotBlank() &&
                    OnboardingState.personalEmail.isNotBlank() &&
                    OnboardingState.entityType.isNotBlank() &&
                    OnboardingState.tradeName.isNotBlank()
            WiomButton(
                t("बिज़नेस लोकेशन जोड़ें", "Add Business Location"),
                onClick = onNext,
                enabled = allFilled,
            )
        }
    }
}
