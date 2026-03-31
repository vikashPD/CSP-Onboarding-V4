# Wiom CSP Onboarding — Notification Campaigns

**Version:** 2.0 | **Date:** 31 March 2026 | **Status:** Draft
**Source:** PRD_HUMAN.md V3.2, CLEVERTAP_EVENT_SHEET.md V1.1
**Scope:** All automated communications from app install to CSP activation

---

## How to Read This Document

- **Channels:** Push + WhatsApp sent together | SMS only as fallback when WhatsApp delivery fails
- **Language:** Hindi first, English below
- **Tone:** Respectful "aap" form (करें/भरें/बनें), warm, encouraging — Wiom's culture
- **Timing:** Wake hours = 9:00 AM to 9:00 PM IST (unless marked "Immediate")
- **Trigger:** All campaigns are system-triggered (automated)
- **Philosophy:** Inform and engage — never spam. Every notification must have a clear reason and clear next step.

---

## Channel Logic

```
Every notification:
  ├── Push Notification ──── always sent
  └── WhatsApp message ──── always sent together with Push
          │
          ├── Delivered? → Done
          │
          └── Not delivered in 5 minutes?
                    │
                    └── SMS (fallback)
```

Push and WhatsApp go out **simultaneously**. SMS fires **only** if WhatsApp fails.

---

## Communication Categories

| # | Category | Purpose | Campaigns |
|---|----------|---------|-----------|
| 1 | Drop-off Recovery | Bring back CSPs who left mid-flow (includes progress context) | 10 |
| 2 | Celebratory / Milestone | Positive reinforcement at big moments | 7 |
| 3 | Status Updates | Keep CSP informed during waiting + rejection/refund | 7 |
| 4 | Engagement (Post-onboarding) | Drive Partner Plus download + training completion | 3 |
| 5 | Support / Helpline | Proactive help when stuck | 4 |
| 6 | Uninstall Re-engagement | Bring back CSPs who uninstalled before activation | 3 |
| **Total** | | | **34 campaigns** |

---

## 1. Drop-off Recovery Communications (10)

Each message tells the CSP what they've already completed and what's the one next step. No generic "come back" messages.

### Registration Phase

| ID | Trigger | Delay | Hindi Message | English Message |
|----|---------|-------|---------------|-----------------|
| D01 | App opened but phone not entered | 2 hours | **Wiom:** आपने ऐप खोला था! बस अपना मोबाइल नंबर डालें और Wiom CSP बनने की शुरुआत करें। | **Wiom:** You opened the app! Just enter your mobile number and start your journey to become a Wiom CSP. |
| D02 | OTP sent but not verified | 5 min (immediate) | **Wiom:** OTP भेजा गया है। कृपया वेरिफ़ाई करें — बस 4 अंक डालने हैं। | **Wiom:** OTP has been sent. Please verify — just enter 4 digits. |
| D03 | OTP verified but personal info not filled | 4 hours | **Wiom:** नंबर वेरिफ़ाई हो गया! अब बस नाम, ईमेल और बिज़नेस की जानकारी भरें — 2 मिनट का काम। | **Wiom:** Number verified! Now just fill name, email, and business details — 2 minutes of work. |
| D04 | Info + location done but reg fee not paid — Day 1 | 24 hours | **Wiom:** आपकी जानकारी और लोकेशन सेव है! बस ₹2,000 रजिस्ट्रेशन फ़ीस भरें और वेरिफ़िकेशन शुरू हो जाएगा। रिजेक्ट होने पर पूरा पैसा वापस। | **Wiom:** Your details and location are saved! Just pay ₹2,000 registration fee and verification will begin. Full refund if rejected. |
| D05 | Info + location done but reg fee not paid — Day 3 | 72 hours | **Wiom:** आपकी CSP यात्रा अभी रुकी है! ₹2,000 फ़ीस भरें — पूरा रिफ़ंडेबल है। कल आख़िरी दिन है। सवाल हैं? बात करें: 7836811111 | **Wiom:** Your CSP journey is on hold! Pay ₹2,000 fee — fully refundable. Tomorrow is the last day. Questions? Call: 7836811111 |

### Verification Phase

| ID | Trigger | Delay | Hindi Message | English Message |
|----|---------|-------|---------------|-----------------|
| D06 | Reg fee paid but KYC not started | 24 hours | **Wiom:** ₹2,000 फ़ीस मिल गई! अब PAN, आधार और GST अपलोड करें। ऐप में "सैंपल दस्तावेज़ देखें" पर टैप करके पता चलेगा क्या अपलोड करना है। | **Wiom:** ₹2,000 fee received! Now upload PAN, Aadhaar, and GST. Tap "View sample document" in the app to see what to upload. |
| D07 | KYC partial (started but not all 3 done) | 24 hours | **Wiom:** KYC शुरू किया था — बस थोड़ा और बाक़ी है! {completed_count}/3 दस्तावेज़ हो चुके हैं। बाक़ी पूरा करें। | **Wiom:** You started KYC — just a little more left! {completed_count}/3 documents done. Complete the rest. |
| D08 | KYC done but bank/ISP/photos not complete | 24 hours | **Wiom:** KYC पूरा हो गया! अब बस {remaining_steps} और बाक़ी हैं — {next_step_name}। पूरा करें और वेरिफ़िकेशन के लिए जमा करें। | **Wiom:** KYC is done! Just {remaining_steps} more left — {next_step_name}. Complete and submit for verification. |

### Activation Phase

| ID | Trigger | Delay | Hindi Message | English Message |
|----|---------|-------|---------------|-----------------|
| D09 | Tech assessment passed but policy not accepted | 24 hours | **Wiom:** टेक्निकल असेसमेंट पास हो चुका है! अब बस पॉलिसी देखें, समझें और आगे बढ़ें। | **Wiom:** Technical assessment is already passed! Now just review the policy and proceed. |
| D10 | Policy accepted but onboarding fee not paid | 24 hours | **Wiom:** बस एक आख़िरी कदम — ₹20,000 ऑनबोर्डिंग फ़ीस भरें और Wiom CSP बनें! आप बहुत क़रीब हैं। | **Wiom:** Just one last step — pay ₹20,000 onboarding fee and become a Wiom CSP! You're so close. |

---

## 2. Celebratory / Milestone Communications (7)

Positive reinforcement at meaningful moments. Makes the CSP feel valued and motivated.

| ID | Trigger | Delay | Hindi Message | English Message |
|----|---------|-------|---------------|-----------------|
| C01 | Registration fee paid | Immediate | **Wiom:** शानदार! रजिस्ट्रेशन पूरा! आप Wiom CSP बनने के रास्ते पर हैं। अब दस्तावेज़ अपलोड करें। | **Wiom:** Fantastic! Registration complete! You're on your way to becoming a Wiom CSP. Now upload your documents. |
| C02 | All documents submitted for verification | Immediate | **Wiom:** बहुत बढ़िया! सभी दस्तावेज़ जमा हो गए! हमारी टीम चेक करेगी — आप बस आराम करें। | **Wiom:** Excellent! All documents submitted! Our team will review — you just relax. |
| C03 | Verification approved | Immediate | **Wiom:** बधाई हो! वेरिफ़िकेशन पास! आपकी मेहनत रंग लाई। अब टेक्निकल असेसमेंट होगा — हमारी टीम 4-5 दिन में संपर्क करेगी। | **Wiom:** Congratulations! Verification passed! Your hard work paid off. Technical assessment is next — our team will contact you in 4-5 days. |
| C04 | Tech assessment passed | Immediate | **Wiom:** बधाई हो! टेक्निकल असेसमेंट पास! आप CSP बनने के बहुत क़रीब हैं। | **Wiom:** Congratulations! Technical assessment passed! You're very close to becoming a CSP. |
| C05 | Onboarding fee paid | Immediate | **Wiom:** ₹20,000 फ़ीस मिल गई! आपका अकाउंट सेटअप हो रहा है — बस कुछ ही देर! | **Wiom:** ₹20,000 fee received! Your account is being set up — just a moment! |
| C06 | Successfully onboarded (Screen 14) | Immediate | **Wiom:** बधाई हो! आप अब Wiom CSP हैं! Wiom Partner Plus ऐप डाउनलोड करें, ट्रेनिंग पूरी करें — उसके बाद कस्टमर मिलना शुरू! | **Wiom:** Congratulations! You're now a Wiom CSP! Download Wiom Partner Plus app, complete training — customers will start coming after that! |
| C07 | Training completed — CSP_ONBOARDED | Immediate | **Wiom:** आप पूरी तरह तैयार हैं! ट्रेनिंग पूरी हो गई — अब Wiom आपके इलाक़े में कस्टमर भेजना शुरू करेगा। शुभकामनाएँ! | **Wiom:** You're fully ready! Training complete — Wiom will now start sending customers to your area. Best wishes! |

---

## 3. Status Update Communications (7)

Keep CSPs informed during waiting periods and on decisions. No CSP should ever feel "what's happening?"

### Waiting Period Updates

| ID | Trigger | Delay | Hindi Message | English Message |
|----|---------|-------|---------------|-----------------|
| U01 | QA review in progress — mid-wait | 48 hours after docs submission | **Wiom:** आपके दस्तावेज़ की समीक्षा जारी है। जल्दी ही अपडेट मिलेगा — धन्यवाद! | **Wiom:** Your document review is in progress. You'll get an update soon — thank you! |
| U02 | Tech assessment in progress — mid-wait | 72 hours after Screen 10 | **Wiom:** टेक्निकल टीम आपकी जानकारी चेक कर रही है। आपको कॉल भी आ सकती है — कृपया उठाएँ! | **Wiom:** Our technical team is reviewing your details. You may also receive a call — please pick up! |

### Decision Notifications

| ID | Trigger | Delay | Hindi Message | English Message |
|----|---------|-------|---------------|-----------------|
| U03 | Verification rejected | Immediate | **Wiom:** आपकी प्रोफ़ाइल अभी अप्रूव नहीं हो पाई। चिंता न करें — ₹2,000 रिफ़ंड शुरू हो गया है। 5-6 कार्य दिवसों में पैसा वापस आ जाएगा। | **Wiom:** Your profile could not be approved at this time. Don't worry — ₹2,000 refund has been initiated. Money will be back in 5-6 business days. |
| U04 | Tech assessment rejected | Immediate | **Wiom:** टेक्निकल असेसमेंट अभी पास नहीं हो पाया। हमसे बात करें — मिलकर समाधान निकालेंगे। कॉल करें: 7836811111 | **Wiom:** Technical assessment could not be passed at this time. Talk to us — we'll find a solution together. Call: 7836811111 |

### Refund Notifications

| ID | Trigger | Delay | Hindi Message | English Message |
|----|---------|-------|---------------|-----------------|
| U05 | Refund completed | Immediate | **Wiom:** ₹2,000 रिफ़ंड आपके बैंक अकाउंट में आ गया है। सवाल हैं? बात करें: 7836811111 | **Wiom:** ₹2,000 refund has been credited to your bank account. Questions? Talk to us: 7836811111 |
| U06 | Refund in progress (3+ days) | 72 hours after refund initiated | **Wiom:** आपका ₹2,000 रिफ़ंड प्रोसेस हो रहा है। 5-6 कार्य दिवस लग सकते हैं। चिंता न करें — पैसा सुरक्षित है। | **Wiom:** Your ₹2,000 refund is being processed. May take 5-6 business days. Don't worry — your money is safe. |
| U07 | Refund failed | Immediate | **Wiom:** रिफ़ंड में दिक़्क़त आई है। चिंता न करें — आपका पैसा सुरक्षित है। कृपया बात करें: 7836811111 | **Wiom:** There was an issue with the refund. Don't worry — your money is safe. Please call: 7836811111 |

---

## 4. Engagement — Post-Onboarding (3)

Drive CSPs from "onboarded in app" to "fully activated and receiving customers."

| ID | Trigger | Delay | Hindi Message | English Message |
|----|---------|-------|---------------|-----------------|
| P01 | Onboarded but Partner Plus not downloaded | 24 hours after Screen 14 | **Wiom:** आप Wiom CSP बन चुके हैं! अब Wiom Partner Plus ऐप डाउनलोड करें और ट्रेनिंग शुरू करें — उसके बाद कस्टमर मिलना शुरू होगा। | **Wiom:** You're now a Wiom CSP! Download the Wiom Partner Plus app and start training — customers will start coming after that. |
| P02 | Partner Plus downloaded but training not started | 48 hours after login | **Wiom:** ऐप डाउनलोड हो गया! अब ट्रेनिंग पूरी करें — जितनी जल्दी ट्रेनिंग, उतनी जल्दी कस्टमर। बस कुछ मॉड्यूल हैं। | **Wiom:** App downloaded! Now complete the training — faster training means faster customers. Just a few modules. |
| P03 | Training started but not completed | 72 hours after training start | **Wiom:** ट्रेनिंग लगभग पूरी है! बस {remaining_modules} मॉड्यूल बाक़ी हैं। पूरा करें और कस्टमर पाना शुरू करें। | **Wiom:** Training is almost done! Just {remaining_modules} modules left. Complete and start getting customers. |

---

## 5. Support / Helpline Communications (4)

Proactive help — reach out before the CSP gives up.

| ID | Trigger | Delay | Hindi Message | English Message |
|----|---------|-------|---------------|-----------------|
| H01 | Any error screen hit (payment failed, setup failed) | 15 min after error | **Wiom:** कुछ दिक़्क़त आई? चिंता न करें — दोबारा कोशिश करें। मदद चाहिए? बात करें: 7836811111 | **Wiom:** Facing an issue? Don't worry — try again. Need help? Talk to us: 7836811111 |
| H02 | CSP inactive for 7+ days mid-onboarding | 7 days of no activity | **Wiom:** कुछ दिनों से आपकी CSP यात्रा रुकी है। कहीं अटक गए? हम मदद के लिए तैयार हैं। कॉल करें: 7836811111 | **Wiom:** Your CSP journey has been paused for a few days. Stuck somewhere? We're ready to help. Call: 7836811111 |
| H03 | Bank dedup error | 30 min after error | **Wiom:** बैंक जानकारी में दिक़्क़त दिख रही है। कृपया अलग बैंक अकाउंट डालें। मदद चाहिए? बात करें: 7836811111 | **Wiom:** There seems to be an issue with bank details. Please enter a different bank account. Need help? Talk to us: 7836811111 |
| H04 | Payment failed twice (any payment screen) | Immediate after 2nd failure | **Wiom:** भुगतान में दिक़्क़त आ रही है? कोई बात नहीं — दूसरा पेमेंट मेथड आज़माएँ। या बात करें: 7836811111 | **Wiom:** Trouble with payment? No problem — try a different payment method. Or call: 7836811111 |

---

## 6. Uninstall Re-engagement Communications (3)

When a CSP uninstalls the app before full activation, we re-engage via WhatsApp/SMS (push won't work after uninstall).

**Detection:** CleverTap tracks app uninstall events automatically. These campaigns use WhatsApp (primary) + SMS (fallback) only — no push.

| ID | Trigger | Delay | Channel | Hindi Message | English Message |
|----|---------|-------|---------|---------------|-----------------|
| R01 | Uninstalled before reg fee payment | 24 hours after uninstall | WhatsApp + SMS | **Wiom:** आपने Wiom for Business ऐप हटा दिया। कोई बात नहीं! जब चाहें वापस आएँ — आपकी जानकारी सुरक्षित है। ऐप डाउनलोड करें: {app_link} | **Wiom:** You removed the Wiom for Business app. No worries! Come back anytime — your details are safe. Download app: {app_link} |
| R02 | Uninstalled after reg fee paid but before verification approved | 24 hours after uninstall | WhatsApp + SMS | **Wiom:** आपने ₹2,000 फ़ीस भर दी है और दस्तावेज़ भी जमा हो रहे थे। ऐप वापस डाउनलोड करें — सब कुछ वहीं से शुरू होगा जहाँ छोड़ा था। {app_link} | **Wiom:** You've already paid ₹2,000 and were submitting documents. Download the app again — everything will resume from where you left off. {app_link} |
| R03 | Uninstalled after verification approved but before onboarding complete | 12 hours after uninstall | WhatsApp + SMS | **Wiom:** आपकी प्रोफ़ाइल अप्रूव हो चुकी है! बस कुछ कदम बाक़ी हैं — ऐप वापस डाउनलोड करें और CSP बनें। आप बहुत क़रीब हैं! {app_link} | **Wiom:** Your profile is already approved! Just a few steps left — download the app again and become a CSP. You're so close! {app_link} |

---

## Timing & Frequency Rules

| Rule | Detail |
|------|--------|
| **Wake hours** | 9:00 AM to 9:00 PM IST |
| **Exceptions (immediate)** | D02 (OTP not verified), all U-series (status/refund), all C-series (celebrations), H01/H04 (errors) |
| **Max notifications per day** | 2 per CSP (across all categories) |
| **Cool-off between notifications** | Minimum 4 hours gap (except immediate triggers) |
| **Priority when multiple trigger** | Celebratory > Status Update > Drop-off > Engagement > Support |
| **Stop on completion** | Once `CSP_ONBOARDED` fires, stop all onboarding campaigns |
| **Stop on rejection** | After rejection, stop drop-off and engagement. Only send status updates (U-series) |
| **Stop on uninstall** | After uninstall, stop push notifications. Only WhatsApp/SMS for R-series |
| **De-duplication** | If a celebration (C) already covers the same info as a drop-off (D), skip the D |

---

## Campaign-to-CleverTap Event Mapping

| Event | When | Properties |
|-------|------|------------|
| `notification_sent` | System sends notification | `campaign_id`, `channel` (whatsapp/sms/push), `csp_id` |
| `notification_delivered` | Reaches device/WhatsApp | `campaign_id`, `channel`, `delivery_time_ms` |
| `notification_opened` | CSP taps/opens | `campaign_id`, `channel`, `time_since_sent_min` |
| `notification_action_taken` | CSP does the intended action within 24hrs | `campaign_id`, `channel`, `action` (e.g., "resumed_onboarding", "called_support", "reinstalled_app") |
| `notification_dismissed` | CSP dismisses | `campaign_id`, `channel` |
| `whatsapp_fallback_to_sms` | WhatsApp failed, SMS sent | `campaign_id`, `csp_id`, `whatsapp_failure_reason` |
| `app_uninstalled` | CSP uninstalls the app | `csp_id`, `last_screen_number`, `last_screen_name`, `phase`, `days_since_install`, `reg_fee_paid` (bool) |
| `app_reinstalled` | CSP reinstalls after uninstall | `csp_id`, `days_since_uninstall`, `reinstall_source` (organic/notification) |

---

## Campaign Performance Metrics

| Metric | Formula | Target |
|--------|---------|--------|
| **Delivery Rate** | `notification_delivered` / `notification_sent` x 100 | > 95% (push), > 85% (WhatsApp), > 98% (SMS) |
| **Open Rate** | `notification_opened` / `notification_delivered` x 100 | > 30% (push), > 60% (WhatsApp), > 15% (SMS) |
| **Action Rate** | `notification_action_taken` / `notification_opened` x 100 | > 20% |
| **Drop-off Recovery Rate** | CSPs who resumed within 48hrs of D-series / D-series sent x 100 | > 15% |
| **Reinstall Rate** | `app_reinstalled` within 7 days of R-series / R-series sent x 100 | > 10% |
| **WhatsApp-to-SMS Fallback Rate** | `whatsapp_fallback_to_sms` / WhatsApp attempts x 100 | < 15% |
| **Opt-out Rate** | CSPs who disabled notifications / total CSPs x 100 | < 5% |
| **Uninstall Rate (pre-activation)** | `app_uninstalled` (before CSP_ONBOARDED) / total installs x 100 | Track baseline, reduce over time |

---

## Full Campaign Journey Map

```
APP INSTALL
    │
    ├─ D01: Didn't enter phone (2hr)
    │
PHONE + OTP
    │
    ├─ D02: OTP not verified (5min, immediate)
    │
PERSONAL INFO + LOCATION
    │
    ├─ D03: Info not filled (4hr)
    │
REG FEE (₹2,000)
    │
    ├─ C01: Registration complete!
    ├─ D04: Not paid Day 1
    ├─ D05: Not paid Day 3 (last day)
    ├─ H04: Payment failed twice
    │
KYC + BANK + ISP + PHOTOS
    │
    ├─ D06: KYC not started (24hr after fee)
    ├─ D07: KYC partial (24hr)
    ├─ D08: KYC done but rest pending (24hr)
    ├─ H03: Bank dedup error (30min)
    ├─ C02: All docs submitted!
    │
VERIFICATION (waiting 3 days)
    │
    ├─ U01: Mid-wait update (48hr)
    ├─ C03: Approved!  /  U03: Rejected + refund
    ├─ U05: Refund completed  /  U06: Refund in progress  /  U07: Refund failed
    │
TECH ASSESSMENT (waiting 4-5 days)
    │
    ├─ U02: Mid-wait update (72hr)
    ├─ C04: Passed!  /  U04: Rejected
    │
POLICY & SLA
    │
    ├─ D09: Policy not accepted (24hr)
    │
ONBOARDING FEE (₹20,000)
    │
    ├─ C05: Fee received!
    ├─ D10: Fee not paid (24hr)
    ├─ H04: Payment failed twice
    │
SUCCESSFULLY ONBOARDED
    │
    ├─ C06: You're a Wiom CSP!
    │
POST-ONBOARDING
    │
    ├─ P01: Partner Plus not downloaded (24hr)
    ├─ P02: Training not started (48hr)
    ├─ P03: Training not completed (72hr)
    ├─ C07: Fully activated! Customers coming!
    │
ANYTIME — SUPPORT
    │
    ├─ H01: Error screen hit (15min)
    ├─ H02: Inactive 7+ days
    │
UNINSTALL (before activation)
    │
    ├─ R01: Before reg fee (24hr, WhatsApp/SMS only)
    ├─ R02: After reg fee, before approval (24hr, WhatsApp/SMS only)
    ├─ R03: After approval, before completion (12hr, WhatsApp/SMS only)
```

---

*34 automated campaigns. Inform and engage, never spam. Push + WhatsApp together, SMS fallback. Hindi-first, respectful tone. Includes uninstall re-engagement via WhatsApp/SMS.*
