package com.wiom.csp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.data.OnboardingState
import com.wiom.csp.data.Scenario
import com.wiom.csp.ui.components.*
import com.wiom.csp.ui.theme.*
import com.wiom.csp.util.t
import com.wiom.csp.ui.viewmodel.*

// Screen 3: Location
@Composable
fun LocationScreen(viewModel: LocationViewModel, onNext: () -> Unit, onBack: () -> Unit) {
    val scenario = OnboardingState.activeScenario

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        AppHeader(
            title = t("रजिस्ट्रेशन", "Registration"),
            onBack = onBack,
            rightText = t("स्टेप 2/3", "Step 2/3")
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                t("बिज़नेस लोकेशन", "Business Location"),
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                t("आपका पूरा पता चाहिए ताकि हम आपकी लोकैलिटी जान सकें और Wiom के कस्टमर भेज सकें", "We need your complete address details to know your locality and provide Wiom Internet customers"),
                fontSize = 14.sp, color = WiomTextSec,
            )
            Spacer(Modifier.height(16.dp))

            FieldLabel(t("राज्य", "State"))
            var stateExpanded by remember { mutableStateOf(false) }
            val indianStates = listOf(
                "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
                "Chhattisgarh", "Goa", "Gujarat", "Haryana",
                "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala",
                "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya",
                "Mizoram", "Nagaland", "Odisha", "Punjab",
                "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana",
                "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal",
                "Andaman & Nicobar Islands", "Chandigarh", "Dadra & Nagar Haveli and Daman & Diu",
                "Delhi", "Jammu & Kashmir", "Ladakh", "Lakshadweep", "Puducherry",
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = OnboardingState.selectedState,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { stateExpanded = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = WiomBorderInput,
                        focusedBorderColor = WiomBorderFocus,
                        unfocusedContainerColor = Color.White,
                        disabledBorderColor = WiomBorderInput,
                        disabledTextColor = WiomText,
                        disabledContainerColor = Color.White,
                    ),
                    enabled = false,
                    trailingIcon = { Text("▼", fontSize = 12.sp, color = WiomHint) },
                    placeholder = { Text(t("राज्य चुनें", "Select State"), color = WiomHint) },
                )
                DropdownMenu(
                    expanded = stateExpanded,
                    onDismissRequest = { stateExpanded = false },
                    modifier = Modifier.heightIn(max = 300.dp),
                ) {
                    indianStates.forEach { stateName ->
                        DropdownMenuItem(
                            text = { Text(stateName, fontSize = 14.sp) },
                            onClick = {
                                OnboardingState.selectedState = stateName
                                stateExpanded = false
                            },
                        )
                    }
                }
            }

            FieldLabel(t("शहर", "City"))
            WiomTextField(value = OnboardingState.city, onValueChange = { OnboardingState.city = it.filter { c -> !c.isDigit() } }, placeholder = t("उदा: बरेली", "For ex: Bareilly"))

            FieldLabel(t("पिनकोड", "Pincode"))
            WiomTextField(value = OnboardingState.pincode, onValueChange = { OnboardingState.pincode = it.filter { c -> c.isDigit() }.take(6) }, placeholder = t("उदा: 243001", "For ex: 243001"))

            FieldLabel(t("पूरा पता", "Full Address"))
            WiomTextField(value = OnboardingState.address, onValueChange = { OnboardingState.address = it }, placeholder = t("उदा: 123, विजय नगर", "For ex: 123, Vijay Nagar"))

            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TrustBadge("\uD83D\uDCCD", t("GPS कैप्चर हुआ", "GPS Captured"))
                Text("22.71° N, 75.85° E", fontSize = 12.sp, color = WiomHint)
            }

            if (scenario == Scenario.AREA_NOT_SERVICEABLE) {
                // ─── AREA_NOT_SERVICEABLE error ───
                Spacer(Modifier.height(16.dp))
                ErrorCard(
                    icon = "📍",
                    titleHi = "यह एरिया अभी सर्विसेबल नहीं है",
                    titleEn = "This area is not serviceable yet",
                    messageHi = "हम जल्द ही इस एरिया में आ रहे हैं। Waitlist में जुड़ें और पहले मौका पाएं!",
                    messageEn = "We're coming to this area soon. Join the waitlist and get first opportunity!",
                    type = "warning",
                )
                Spacer(Modifier.height(8.dp))
                InfoBox("\uD83D\uDCCB", t("Waitlist में 47 लोग पहले से हैं", "47 people already on waitlist"))
                Spacer(Modifier.height(12.dp))
                WiomButton(t("Waitlist में जुड़ें", "Join Waitlist"), onClick = { OnboardingState.clearScenario() })
                Spacer(Modifier.height(8.dp))
                WiomButton(t("दूसरा पिनकोड डालें", "Enter different pincode"), onClick = { OnboardingState.clearScenario() }, isSecondary = true)
            }
        }
        if (scenario != Scenario.AREA_NOT_SERVICEABLE) {
            BottomBar {
                WiomButton(t("रजिस्ट्रेशन फ़ीस भरें", "Pay Registration Fee"), onClick = onNext)
            }
        }
    }
}
