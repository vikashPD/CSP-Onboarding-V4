# Wiom CSP Onboarding — Notification Campaigns

**Version:** 1.0 | **Date:** 31 March 2026 | **Status:** Draft
**Source:** PRD_HUMAN.md V3.2, CLEVERTAP_EVENT_SHEET.md V1.1
**Scope:** All automated communications from app install to CSP activation

---

## How to Read This Document

- **Channels:** WhatsApp (primary) > SMS (fallback if WhatsApp unavailable) > Push Notification
- **Language:** Hindi first, English below
- **Tone:** Respectful "aap" form (करें/भरें/बनें), warm, encouraging — in sync with Wiom's culture
- **Timing:** Wake hours = 9:00 AM to 9:00 PM IST (unless marked "Immediate")
- **Trigger:** All campaigns are system-triggered (automated), no manual intervention
- **Fallback logic:** Try WhatsApp first → if not delivered in 5 min → send SMS → push notification always sent in parallel

---

## Communication Categories

| # | Category | Purpose | Total Campaigns |
|---|----------|---------|-----------------|
| 1 | Stage-wise Progress | Transparency — tell CSP what happened and what's next | 14 |
| 2 | Drop-off Recovery | Bring back CSPs who left mid-flow | 12 |
| 3 | Engagement & Stickiness | Keep CSPs motivated to complete the journey | 6 |
| 4 | Celebratory / Milestone | Celebrate progress — positive reinforcement | 7 |
| 5 | Status Updates | Keep CSP informed during waiting periods | 5 |
| 6 | Support / Helpline | Offer help when CSP might be stuck | 4 |
| **Total** | | | **48 campaigns** |

---

## 1. Stage-wise Progress Communications

These fire at every meaningful step to keep the CSP informed about what just happened and what comes next.

### Phase 1 — Registration

| ID | Trigger | Delay | Channel | Hindi Message | English Message |
|----|---------|-------|---------|---------------|-----------------|
| S01 | OTP verified (Screen 1 → 2) | Immediate | Push | **Wiom:** नंबर वेरिफ़ाई हो गया! अब अपनी जानकारी भरें और Wiom CSP बनने की ओर बढ़ें। | **Wiom:** Number verified! Now fill your details and move towards becoming a Wiom CSP. |
| S02 | Personal info submitted (Screen 2 → 3) | Immediate | Push | **Wiom:** जानकारी सेव हो गई! अब अपनी दुकान/ऑफ़िस की लोकेशन बताएँ। | **Wiom:** Details saved! Now share your shop/office location. |
| S03 | Location submitted (Screen 3 → 4) | Immediate | Push | **Wiom:** लोकेशन सेव हो गई! अगला कदम — रजिस्ट्रेशन फ़ीस भरें और वेरिफ़िकेशन शुरू करें। | **Wiom:** Location saved! Next step — pay the registration fee to start verification. |
| S04 | Registration fee paid (Screen 4 → 5) | Immediate | WhatsApp + Push | **Wiom:** ₹2,000 रजिस्ट्रेशन फ़ीस मिल गई! अब अपने KYC दस्तावेज़ अपलोड करें — PAN, आधार, GST। | **Wiom:** ₹2,000 registration fee received! Now upload your KYC documents — PAN, Aadhaar, GST. |

### Phase 2 — Verification

| ID | Trigger | Delay | Channel | Hindi Message | English Message |
|----|---------|-------|---------|---------------|-----------------|
| S05 | KYC completed (Screen 5 → 6) | Immediate | Push | **Wiom:** KYC दस्तावेज़ अपलोड हो गए! अब बैंक की जानकारी भरें। | **Wiom:** KYC documents uploaded! Now fill your bank details. |
| S06 | Bank doc uploaded (Screen 6 → 7) | Immediate | Push | **Wiom:** बैंक जानकारी सेव हो गई! अब ISP अनुबंध अपलोड करें। | **Wiom:** Bank details saved! Now upload your ISP agreement. |
| S07 | ISP agreement uploaded (Screen 7 → 8) | Immediate | Push | **Wiom:** ISP अनुबंध अपलोड हो गया! अब दुकान और उपकरण की फ़ोटो लें। | **Wiom:** ISP agreement uploaded! Now take photos of your shop and equipment. |
| S08 | Photos submitted (Screen 8 → 9) | Immediate | WhatsApp + Push | **Wiom:** सभी दस्तावेज़ जमा हो गए! हमारी टीम अब आपकी जानकारी चेक करेगी। इसमें 3 कार्य दिवस लग सकते हैं। | **Wiom:** All documents submitted! Our team will now review your details. This may take up to 3 business days. |

### Phase 3 — Activation

| ID | Trigger | Delay | Channel | Hindi Message | English Message |
|----|---------|-------|---------|---------------|-----------------|
| S09 | Verification approved (Screen 9 → 10) | Immediate | WhatsApp + Push | **Wiom:** बधाई हो! आपकी जानकारी अप्रूव हो गई! अगला कदम — टेक्निकल असेसमेंट। हमारी टीम 4-5 कार्य दिवस में आपसे संपर्क करेगी। | **Wiom:** Congratulations! Your details have been approved! Next step — technical assessment. Our team will contact you within 4-5 business days. |
| S10 | Tech assessment passed (Screen 10 → 11) | Immediate | WhatsApp + Push | **Wiom:** टेक्निकल असेसमेंट पास! अब Wiom की पॉलिसी और रेट कार्ड देखें और आगे बढ़ें। | **Wiom:** Technical assessment passed! Now review Wiom's policy and rate card and proceed. |
| S11 | Policy accepted (Screen 11 → 12) | Immediate | Push | **Wiom:** पॉलिसी स्वीकार हो गई! अब ऑनबोर्डिंग फ़ीस भरें और CSP बनें। | **Wiom:** Policy accepted! Now pay the onboarding fee and become a CSP. |
| S12 | Onboarding fee paid (Screen 12 → 13) | Immediate | WhatsApp + Push | **Wiom:** ₹20,000 ऑनबोर्डिंग फ़ीस मिल गई! आपका अकाउंट अभी सेटअप हो रहा है। | **Wiom:** ₹20,000 onboarding fee received! Your account is being set up now. |
| S13 | Successfully onboarded (Screen 14) | Immediate | WhatsApp + Push | **Wiom:** बधाई हो! आप अब Wiom CSP हैं! Wiom Partner Plus ऐप डाउनलोड करें, लॉगिन करें और ट्रेनिंग पूरी करें — उसके बाद कस्टमर मिलना शुरू! | **Wiom:** Congratulations! You are now a Wiom CSP! Download the Wiom Partner Plus app, login, and complete training — customers will start coming after that! |

---

## 2. Drop-off Recovery Communications

These fire when a CSP leaves the app without completing the current step. Goal: bring them back.

### Registration Phase Drop-offs

| ID | Trigger | Delay | Channel | Hindi Message | English Message |
|----|---------|-------|---------|---------------|-----------------|
| D01 | Started app but didn't enter phone number | 1 hour | Push | **Wiom:** आपने Wiom CSP बनने की शुरुआत की थी! बस अपना नंबर डालें और आगे बढ़ें। | **Wiom:** You started becoming a Wiom CSP! Just enter your number and move ahead. |
| D02 | OTP sent but not verified | Immediate (after 5 min) | Push | **Wiom:** आपका OTP भेजा गया है। कृपया वेरिफ़ाई करें और अपनी CSP यात्रा जारी रखें। | **Wiom:** Your OTP has been sent. Please verify and continue your CSP journey. |
| D03 | OTP verified but personal info not filled | 2 hours | Push | **Wiom:** आपका नंबर वेरिफ़ाई हो चुका है! बस कुछ जानकारी और भरनी है — 2 मिनट का काम। | **Wiom:** Your number is verified! Just a few more details to fill — 2 minutes of work. |
| D04 | Personal info done but location not filled | 2 hours | Push | **Wiom:** बस एक कदम और! अपनी दुकान की लोकेशन बताएँ और रजिस्ट्रेशन पूरा करें। | **Wiom:** Just one more step! Share your shop location and complete registration. |
| D05 | Location done but reg fee not paid — Day 1 | 24 hours | WhatsApp + Push | **Wiom:** आपका रजिस्ट्रेशन लगभग पूरा है! ₹2,000 फ़ीस भरें और वेरिफ़िकेशन शुरू करें। यह पूरी तरह रिफ़ंडेबल है। | **Wiom:** Your registration is almost complete! Pay the ₹2,000 fee and start verification. It's fully refundable. |
| D06 | Location done but reg fee not paid — Day 2 | 48 hours | WhatsApp + Push | **Wiom:** रजिस्ट्रेशन फ़ीस अभी बाक़ी है। ₹2,000 भरें — रिजेक्ट होने पर पूरा पैसा वापस। चिंता की कोई बात नहीं! | **Wiom:** Registration fee is still pending. Pay ₹2,000 — full refund if rejected. Nothing to worry about! |
| D07 | Location done but reg fee not paid — Day 3 | 72 hours | WhatsApp + SMS + Push | **Wiom:** आपकी CSP यात्रा रुकी हुई है! कल आख़िरी दिन है। ₹2,000 भरें और वेरिफ़िकेशन शुरू करें। सवाल हैं? बात करें: 7836811111 | **Wiom:** Your CSP journey is on hold! Tomorrow is the last day. Pay ₹2,000 and start verification. Questions? Call: 7836811111 |

### Verification Phase Drop-offs

| ID | Trigger | Delay | Channel | Hindi Message | English Message |
|----|---------|-------|---------|---------------|-----------------|
| D08 | Reg fee paid but KYC not started | 24 hours | WhatsApp + Push | **Wiom:** फ़ीस मिल गई — अब अगला कदम! PAN, आधार और GST अपलोड करें। "सैंपल दस्तावेज़ देखें" से पता चलेगा कि क्या अपलोड करना है। | **Wiom:** Fee received — now the next step! Upload PAN, Aadhaar, and GST. Tap "View sample document" to see what to upload. |
| D09 | KYC started but not completed (partial sub-stages) | 24 hours | WhatsApp + Push | **Wiom:** KYC अधूरा है! आपने शुरू किया था — बस थोड़ा और बाक़ी है। अभी पूरा करें। | **Wiom:** KYC is incomplete! You had started — just a little more left. Complete it now. |
| D10 | KYC done but bank details not filled | 24 hours | Push | **Wiom:** KYC पूरा हो गया! अब बैंक जानकारी भरें और बैंक दस्तावेज़ अपलोड करें — बस 5 मिनट का काम। | **Wiom:** KYC is done! Now fill bank details and upload a bank document — just 5 minutes of work. |
| D11 | Bank done but ISP agreement not uploaded | 24 hours | Push | **Wiom:** बैंक जानकारी सेव हो गई! अब ISP अनुबंध अपलोड करें। PDF या फ़ोटो — दोनों चलेगा। | **Wiom:** Bank details saved! Now upload your ISP agreement. PDF or photos — both work. |
| D12 | ISP done but photos not uploaded | 24 hours | Push | **Wiom:** लगभग पहुँच गए! बस दुकान और उपकरण की फ़ोटो लें और वेरिफ़िकेशन के लिए जमा करें। | **Wiom:** Almost there! Just take shop and equipment photos and submit for verification. |

---

## 3. Engagement & Stickiness Communications

These keep CSPs motivated and connected during the onboarding journey, especially during long waiting periods.

| ID | Trigger | Delay | Channel | Hindi Message | English Message |
|----|---------|-------|---------|---------------|-----------------|
| E01 | Waiting for QA review (Day 1 of wait) | 24 hours after submission | WhatsApp + Push | **Wiom:** आपके दस्तावेज़ हमारी टीम के पास हैं। समीक्षा जारी है — जल्दी ही अपडेट मिलेगा। धन्यवाद! | **Wiom:** Your documents are with our team. Review is in progress — you'll get an update soon. Thank you! |
| E02 | Waiting for QA review (Day 2 of wait) | 48 hours after submission | Push | **Wiom:** समीक्षा चल रही है। हम जल्द से जल्द आपको बताएँगे। थोड़ा इंतज़ार करें! | **Wiom:** Review is ongoing. We'll let you know as soon as possible. Just a little more wait! |
| E03 | Waiting for tech assessment (Day 2 of wait) | 48 hours after Screen 10 | WhatsApp + Push | **Wiom:** टेक्निकल टीम आपकी जानकारी चेक कर रही है। 4-5 कार्य दिवस में अपडेट मिलेगा। आपको कॉल भी आ सकती है — कृपया उठाएँ! | **Wiom:** Our technical team is reviewing your details. You'll get an update in 4-5 business days. You may also receive a call — please pick up! |
| E04 | Waiting for tech assessment (Day 4 of wait) | 96 hours after Screen 10 | Push | **Wiom:** टेक्निकल असेसमेंट लगभग पूरा होने वाला है। जल्दी ही आपको नतीजा बताएँगे! | **Wiom:** Technical assessment is almost done. We'll share the result with you soon! |
| E05 | Onboarded but Partner Plus not downloaded (Day 1) | 24 hours after Screen 14 | WhatsApp + Push | **Wiom:** आप Wiom CSP बन चुके हैं! अब Wiom Partner Plus ऐप डाउनलोड करें और ट्रेनिंग शुरू करें — उसके बाद कस्टमर मिलना शुरू होगा। | **Wiom:** You're now a Wiom CSP! Download the Wiom Partner Plus app and start training — customers will start coming after that. |
| E06 | Onboarded but training not completed (Day 3) | 72 hours after Partner Plus login | WhatsApp + Push | **Wiom:** ट्रेनिंग पूरी करें और कस्टमर पाना शुरू करें! बस कुछ मॉड्यूल बाक़ी हैं। जितनी जल्दी ट्रेनिंग, उतनी जल्दी कमाई! | **Wiom:** Complete your training and start getting customers! Just a few modules left. Faster training = faster earnings! |

---

## 4. Celebratory / Milestone Communications

Positive reinforcement at every meaningful milestone. Makes the CSP feel valued.

| ID | Trigger | Delay | Channel | Hindi Message | English Message |
|----|---------|-------|---------|---------------|-----------------|
| C01 | Registration fee paid | Immediate | WhatsApp + Push | **Wiom:** शानदार! रजिस्ट्रेशन पूरा हो गया! आप Wiom CSP बनने के रास्ते पर हैं। अब दस्तावेज़ अपलोड करें। | **Wiom:** Fantastic! Registration is complete! You're on your way to becoming a Wiom CSP. Now upload your documents. |
| C02 | All documents submitted | Immediate | WhatsApp + Push | **Wiom:** बहुत बढ़िया! सभी दस्तावेज़ जमा हो गए। अब हमारी टीम चेक करेगी — आप बस आराम करें! | **Wiom:** Excellent! All documents submitted. Our team will review — you just relax! |
| C03 | Verification approved | Immediate | WhatsApp + Push | **Wiom:** बधाई हो! वेरिफ़िकेशन पास! आपकी मेहनत रंग लाई। अब बस कुछ और कदम बाक़ी हैं! | **Wiom:** Congratulations! Verification passed! Your hard work paid off. Just a few more steps to go! |
| C04 | Tech assessment passed | Immediate | WhatsApp + Push | **Wiom:** बधाई हो! टेक्निकल असेसमेंट पास! आप CSP बनने के बहुत क़रीब हैं। | **Wiom:** Congratulations! Technical assessment passed! You're very close to becoming a CSP. |
| C05 | Onboarding fee paid | Immediate | WhatsApp + Push | **Wiom:** ₹20,000 फ़ीस मिल गई! आपका अकाउंट सेटअप हो रहा है — बस कुछ ही देर में आप Wiom CSP होंगे! | **Wiom:** ₹20,000 fee received! Your account is being set up — you'll be a Wiom CSP in just a moment! |
| C06 | Successfully onboarded (Screen 14) | Immediate | WhatsApp + Push | **Wiom:** बधाई हो! आप अब आधिकारिक Wiom CSP हैं! Wiom Partner Plus ऐप डाउनलोड करें, ट्रेनिंग पूरी करें — और कस्टमर पाना शुरू करें! | **Wiom:** Congratulations! You are now an official Wiom CSP! Download the Wiom Partner Plus app, complete training — and start getting customers! |
| C07 | Training completed + CSP_ONBOARDED | Immediate | WhatsApp + Push | **Wiom:** आप पूरी तरह तैयार हैं! ट्रेनिंग पूरी हो गई — अब Wiom आपके इलाक़े में कस्टमर भेजना शुरू करेगा। शुभकामनाएँ! | **Wiom:** You're fully ready! Training is complete — Wiom will now start sending customers to your area. Best wishes! |

---

## 5. Status Update Communications

Keep CSPs informed during waiting periods so they don't feel ignored.

| ID | Trigger | Delay | Channel | Hindi Message | English Message |
|----|---------|-------|---------|---------------|-----------------|
| U01 | Verification under review (Day 3 — max TAT) | 72 hours after submission | WhatsApp + Push | **Wiom:** आपके दस्तावेज़ की समीक्षा अभी जारी है। हम जल्द ही आपको बताएँगे। धन्यवाद! | **Wiom:** Your document review is still in progress. We'll inform you shortly. Thank you! |
| U02 | Verification rejected | Immediate | WhatsApp + SMS + Push | **Wiom:** आपकी प्रोफ़ाइल अभी अप्रूव नहीं हो पाई। चिंता न करें — ₹2,000 रिफ़ंड शुरू हो गया है। 5-6 कार्य दिवसों में पैसा वापस आ जाएगा। | **Wiom:** Your profile could not be approved at this time. Don't worry — ₹2,000 refund has been initiated. Money will be back in 5-6 business days. |
| U03 | Refund completed | Immediate | WhatsApp + Push | **Wiom:** ₹2,000 रिफ़ंड आपके बैंक अकाउंट में आ गया है। अगर कोई सवाल है तो बात करें: 7836811111 | **Wiom:** ₹2,000 refund has been credited to your bank account. If you have questions, talk to us: 7836811111 |
| U04 | Tech assessment rejected | Immediate | WhatsApp + SMS + Push | **Wiom:** टेक्निकल असेसमेंट अभी पास नहीं हो पाया। हमसे बात करें — हम मिलकर समाधान निकालेंगे। कॉल करें: 7836811111 | **Wiom:** Technical assessment could not be passed at this time. Talk to us — we'll find a solution together. Call: 7836811111 |
| U05 | Refund failed | Immediate | WhatsApp + SMS + Push | **Wiom:** रिफ़ंड में कुछ दिक़्क़त आई है। चिंता न करें — आपका पैसा सुरक्षित है। कृपया हमसे बात करें: 7836811111 | **Wiom:** There was an issue with the refund. Don't worry — your money is safe. Please talk to us: 7836811111 |

---

## 6. Support / Helpline Communications

Proactive help when the CSP might be stuck.

| ID | Trigger | Delay | Channel | Hindi Message | English Message |
|----|---------|-------|---------|---------------|-----------------|
| H01 | CSP hit any error screen (payment failed, setup failed, etc.) | 10 minutes after error | Push | **Wiom:** कुछ दिक़्क़त आई? चिंता न करें — दोबारा कोशिश करें। मदद चाहिए? बात करें: 7836811111 | **Wiom:** Facing an issue? Don't worry — try again. Need help? Talk to us: 7836811111 |
| H02 | CSP inactive for 7+ days mid-onboarding | 7 days of no activity | WhatsApp + Push | **Wiom:** कुछ दिनों से आपकी CSP यात्रा रुकी हुई है। कहीं अटक गए हैं? हम मदद करने के लिए तैयार हैं। कॉल करें: 7836811111 | **Wiom:** Your CSP journey has been paused for a few days. Stuck somewhere? We're ready to help. Call: 7836811111 |
| H03 | Bank dedup error encountered | 30 minutes after error | WhatsApp + Push | **Wiom:** बैंक जानकारी में दिक़्क़त दिख रही है। कृपया अलग बैंक अकाउंट डालें। मदद चाहिए? बात करें: 7836811111 | **Wiom:** There seems to be an issue with bank details. Please enter a different bank account. Need help? Talk to us: 7836811111 |
| H04 | Payment failed twice | Immediate after 2nd failure | WhatsApp + Push | **Wiom:** भुगतान में दिक़्क़त आ रही है? कोई बात नहीं — दूसरा पेमेंट मेथड आज़माएँ। या हमसे बात करें: 7836811111 | **Wiom:** Having trouble with payment? No problem — try a different payment method. Or talk to us: 7836811111 |

---

## Timing & Frequency Rules

| Rule | Detail |
|------|--------|
| **Wake hours** | 9:00 AM to 9:00 PM IST |
| **Exception** | Drop-off recovery D02 (OTP not verified) fires immediately regardless of time |
| **Max notifications per day** | 3 per CSP (across all categories) |
| **Max WhatsApp per day** | 2 per CSP |
| **Cool-off between notifications** | Minimum 2 hours gap between any two notifications to same CSP |
| **No duplicate messages** | If a stage-wise (S) notification already covered the same content, skip the corresponding drop-off (D) notification |
| **Stop all on completion** | Once `CSP_ONBOARDED` fires, stop all onboarding notifications. Switch to Partner Plus app campaigns. |
| **Stop all on rejection** | After verification or tech assessment rejection, stop all engagement/drop-off notifications. Only send status updates (U series). |

---

## Channel Priority & Fallback Logic

```
Step 1: Send Push Notification (always, immediate)
          │
Step 2: Send WhatsApp message (if campaign is marked WhatsApp)
          │
          ├── Delivered? → Done
          │
          └── Not delivered in 5 minutes?
                    │
                    └── Send SMS (fallback)
```

**WhatsApp requirements:**
- CSP must have opted in (T&C acceptance on Screen 0 covers this)
- Messages must use WhatsApp Business API templates (pre-approved by Meta)
- Template variables: `{{csp_name}}`, `{{business_name}}`, `{{amount}}`, `{{phone_number}}`

**SMS requirements:**
- Transactional SMS route (not promotional) for status updates and refund notifications
- Promotional SMS route for engagement and drop-off recovery
- DND compliance: transactional SMS bypasses DND, promotional does not

---

## Campaign-to-CleverTap Event Mapping

Each notification should fire these CleverTap events:

| Event | When | Properties |
|-------|------|------------|
| `notification_sent` | System sends the notification | `campaign_id`, `channel` (whatsapp/sms/push), `csp_id` |
| `notification_delivered` | Notification reaches device | `campaign_id`, `channel`, `delivery_time_ms` |
| `notification_opened` | CSP taps/opens the notification | `campaign_id`, `channel`, `time_since_sent_min` |
| `notification_action_taken` | CSP performs the intended action within 24hrs of open | `campaign_id`, `channel`, `action` (e.g., "resumed_onboarding", "called_support") |
| `notification_dismissed` | CSP dismisses without opening | `campaign_id`, `channel` |

---

## Campaign Performance Metrics

| Metric | Formula | Target |
|--------|---------|--------|
| **Delivery Rate** | `notification_delivered` / `notification_sent` x 100 | > 95% (push), > 85% (WhatsApp), > 98% (SMS) |
| **Open Rate** | `notification_opened` / `notification_delivered` x 100 | > 30% (push), > 60% (WhatsApp), > 15% (SMS) |
| **Action Rate** | `notification_action_taken` / `notification_opened` x 100 | > 20% |
| **Drop-off Recovery Rate** | CSPs who resumed onboarding within 48hrs of drop-off notification / drop-off notifications sent x 100 | > 15% |
| **Opt-out Rate** | CSPs who disabled notifications / total CSPs x 100 | < 5% |

---

## Full Campaign Summary by Journey Stage

```
APP INSTALL
    │
    ├─ D01: Didn't enter phone (1hr)
    │
PHONE + OTP
    │
    ├─ S01: OTP verified ✓
    ├─ D02: OTP not verified (5min, immediate)
    │
PERSONAL INFO
    │
    ├─ S02: Info submitted ✓
    ├─ D03: Info not filled (2hr)
    │
LOCATION
    │
    ├─ S03: Location submitted ✓
    ├─ D04: Location not filled (2hr)
    │
REG FEE (₹2,000)
    │
    ├─ S04: Fee paid ✓
    ├─ C01: Celebration — Registration complete!
    ├─ D05: Not paid Day 1
    ├─ D06: Not paid Day 2
    ├─ D07: Not paid Day 3 (last day warning)
    ├─ H04: Payment failed twice
    │
KYC DOCUMENTS
    │
    ├─ S05: KYC completed ✓
    ├─ D08: KYC not started (24hr)
    ├─ D09: KYC partial (24hr)
    │
BANK DETAILS
    │
    ├─ S06: Bank doc uploaded ✓
    ├─ D10: Bank not filled (24hr)
    ├─ H03: Bank dedup error (30min)
    │
ISP AGREEMENT
    │
    ├─ S07: ISP uploaded ✓
    ├─ D11: ISP not uploaded (24hr)
    │
SHOP & EQUIPMENT PHOTOS
    │
    ├─ S08: Photos submitted ✓
    ├─ C02: Celebration — All docs submitted!
    ├─ D12: Photos not uploaded (24hr)
    │
VERIFICATION (waiting)
    │
    ├─ E01: Day 1 of wait
    ├─ E02: Day 2 of wait
    ├─ U01: Day 3 — max TAT reminder
    ├─ S09: Approved ✓ / U02: Rejected
    ├─ C03: Celebration — Verified!
    ├─ U03: Refund completed (if rejected)
    ├─ U05: Refund failed (if applicable)
    │
TECH ASSESSMENT (waiting)
    │
    ├─ E03: Day 2 of wait
    ├─ E04: Day 4 of wait
    ├─ S10: Passed ✓ / U04: Rejected
    ├─ C04: Celebration — Assessment passed!
    │
POLICY & SLA
    │
    ├─ S11: Policy accepted ✓
    │
ONBOARDING FEE (₹20,000)
    │
    ├─ S12: Fee paid ✓
    ├─ C05: Celebration — Fee received!
    ├─ H04: Payment failed twice
    │
ACCOUNT SETUP + SUCCESS
    │
    ├─ S13: Successfully onboarded ✓
    ├─ C06: Celebration — You're a Wiom CSP!
    │
POST-ONBOARDING
    │
    ├─ E05: Partner Plus not downloaded (Day 1)
    ├─ E06: Training not completed (Day 3)
    ├─ C07: Celebration — Fully activated! Customers coming!
    │
THROUGHOUT JOURNEY
    │
    ├─ H01: Error screen hit (10min)
    ├─ H02: Inactive 7+ days (proactive help)
```

---

*This document covers 48 automated notification campaigns across 6 categories for the complete Wiom CSP onboarding journey. All campaigns are Hindi-first, respectful tone, triggered automatically, with WhatsApp as primary channel and SMS fallback. Designed for CleverTap campaign orchestration.*
