package com.wiom.csp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import com.wiom.csp.R
import com.wiom.csp.ui.components.AppHeader
import com.wiom.csp.ui.components.BottomBar
import com.wiom.csp.ui.components.WiomButton
import com.wiom.csp.ui.theme.*
import com.wiom.csp.util.t
import androidx.compose.foundation.Image

@Composable
fun PitchScreen(onGetStarted: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WiomSurface),
    ) {
        // Header with मदद + Language (matches HTML)
        AppHeader(title = "Wiom Partner+")

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Wiom logo (from wiom-logo.webp)
            Image(
                painter = painterResource(id = R.drawable.wiom_logo),
                contentDescription = "Wiom Logo",
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(18.dp)),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Wiom Partner+",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = WiomPrimary,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = t(
                    "भारत का सबसे भरोसेमंद इंटरनेट पार्टनर नेटवर्क",
                    "India\u2019s most trusted internet partner network"
                ),
                fontSize = 14.sp,
                color = WiomTextSec,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Benefits — exact copy from HTML
            PitchBenefitRow("\uD83C\uDF10", t("अपने एरिया में कनेक्शन सर्विस प्रोवाइडर बनें", "Become a connection service provider in your area"))
            PitchBenefitRow("\uD83D\uDCB0", t("हर कनेक्शन और रीचार्ज पर कमाई करें", "Earn on every connection & recharge"))
            PitchBenefitRow("\uD83C\uDF93", t("पूरी ट्रेनिंग और निरंतर सपोर्ट", "Complete training & ongoing support"))
            PitchBenefitRow("\uD83D\uDE80", t("कम निवेश में अपना खुद का बिजनेस", "Your own business with low investment"))

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Bottom CTA
        BottomBar {
            WiomButton(t("शुरू करें", "Get Started"), onClick = onGetStarted)
        }
    }
}

@Composable
fun PitchBenefitRow(emoji: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(WiomBgSec)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(emoji, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = WiomText)
    }
}
