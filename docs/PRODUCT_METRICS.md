# Wiom CSP Onboarding — Product Metrics & Analytics Framework

**Version:** 1.0 | **Date:** 31 March 2026 | **Status:** Draft
**Source:** PRD_HUMAN.md V3.2, CLEVERTAP_EVENT_SHEET.md V1.1
**Scope:** End-to-end — from app discovery to CSP receiving first customer

---

## How to Read This Document

- **North Star Metric** — The ONE number the entire team optimizes for
- **Leading Metrics** — Move these and the North Star moves. Early signals.
- **Supporting Metrics** — Help explain WHY leading metrics move up or down
- **Tracking frequency:** D = Daily, W = Weekly, M = Monthly
- **Owner:** Who looks at this metric most often

---

## North Star Metric

### CSPs Fully Activated

> **Definition:** Number of CSPs who have completed the entire journey — app onboarding (15 screens) + Partner Plus app download + login + training completion — and are now **receiving Wiom customers**.

> **Formula:** Count of `CSP_ONBOARDED` events fired

> **Why this and not just "onboarding completed"?**
> A CSP who finishes the 15 screens but never downloads the Partner Plus app or completes training is not useful — they can't serve customers yet. The real value is created only when they start receiving and serving Wiom customers.

> **Tracking:** Daily count, weekly trend, monthly cohort

> **Target:** Not defined yet — baseline will be set from first 30 days of data

---

## Leading Metrics (6)

These are the strongest predictors of the North Star. If these move, the North Star moves.

### L1. Onboarding Completion Rate

| Attribute | Detail |
|-----------|--------|
| **Definition** | % of CSPs who start the app (Pitch screen) and reach Screen 14 (Successfully Onboarded) |
| **Formula** | `onboarding_completed` / `pitch_get_started_tapped` x 100 |
| **Why it matters** | Directly measures how many people who show interest actually finish. Low rate = friction in the flow. |
| **Tracking** | W, M |
| **Owner** | Product |
| **Benchmark** | Typical SaaS onboarding: 25-40%. Target: establish baseline first. |

### L2. Full Activation Rate

| Attribute | Detail |
|-----------|--------|
| **Definition** | % of CSPs who complete app onboarding AND complete Partner Plus training |
| **Formula** | `CSP_ONBOARDED` / `onboarding_completed` x 100 |
| **Why it matters** | Measures the gap between "onboarded in app" and "actually ready to serve customers." If this is low, CSPs are dropping off after the app but before training. |
| **Tracking** | W, M |
| **Owner** | Product + Training/Ops |

### L3. Time to Onboard (Median)

| Attribute | Detail |
|-----------|--------|
| **Definition** | Median time from `pitch_get_started_tapped` to `onboarding_completed` (Screen 14) |
| **Formula** | Median of (`onboarding_completed.timestamp` - `pitch_get_started_tapped.timestamp`) |
| **Why it matters** | Shorter = less friction, less drop-off. Long onboarding = CSPs lose interest. Includes wait times for QA review (up to 3 days) and tech assessment (4-5 days). |
| **Tracking** | W, M |
| **Owner** | Product + Ops |
| **Breakdown** | Also track per-phase: Registration time, Verification time, Activation time |

### L4. Phase-wise Drop-off Rate

| Attribute | Detail |
|-----------|--------|
| **Definition** | % of CSPs who enter each phase but do NOT complete it |
| **Formula** | (Entered phase - Completed phase) / Entered phase x 100 |
| **Phases** | Phase 1: Registration (Pitch → Screen 4 payment), Phase 2: Verification (Screen 5 → Screen 9 approved), Phase 3: Activation (Screen 10 → Screen 14) |
| **Why it matters** | Shows WHERE in the funnel CSPs are leaving. Is it payment? Documents? Waiting for review? |
| **Tracking** | W |
| **Owner** | Product |

### L5. QA Approval Rate

| Attribute | Detail |
|-----------|--------|
| **Definition** | % of CSPs submitted for verification (Screen 9) who get approved |
| **Formula** | `verification_approved` / `verification_submitted` x 100 |
| **Why it matters** | Low approval = bad quality applicants or unclear document requirements. High rejection wastes CSP time and triggers refunds. |
| **Tracking** | W |
| **Owner** | QA + Product |

### L6. Tech Assessment Pass Rate

| Attribute | Detail |
|-----------|--------|
| **Definition** | % of CSPs who reach tech assessment (Screen 10) and pass |
| **Formula** | `tech_assessment_passed` / `tech_assessment_pending` x 100 |
| **Why it matters** | Low pass rate = wrong CSPs are getting through earlier stages, or assessment criteria too strict. Each failure here = CSP loses Rs.2,000 with no refund. |
| **Tracking** | W |
| **Owner** | Network Quality + Product |

---

## Supporting Metrics

Organized by what they help explain.

---

### A. Acquisition & Top of Funnel

These tell us: **How are CSPs finding the app?**

| # | Metric | Formula | Frequency | Owner |
|---|--------|---------|-----------|-------|
| A1 | **App Installs** | Play Store installs count | D, W | Growth |
| A2 | **App Opens (first time)** | `app_opened` where `is_fresh_install = true` | D, W | Growth |
| A3 | **Pitch-to-Registration Rate** | `send_otp_tapped` / `pitch_get_started_tapped` x 100 | W | Product |
| A4 | **Install-to-Pitch Rate** | `pitch_screen_viewed` / App Installs x 100 | W | Growth |
| A5 | **Acquisition Channel** | Source attribution (Play Store organic, referral, field sales, ads) | W, M | Growth |
| A6 | **Geographic Distribution of Installs** | `state_selected` and `city_entered` grouped by state/city | W, M | Growth + Sales |
| A7 | **Time on Pitch Screen** | Avg `time_on_pitch_sec` from `pitch_get_started_tapped` | W | Product |

---

### B. Registration Funnel (Screens 0-4)

These tell us: **Are CSPs getting through the registration smoothly?**

| # | Metric | Formula | Frequency | Owner |
|---|--------|---------|-----------|-------|
| B1 | **OTP Success Rate** | `otp_verified_success` / `otp_submitted` x 100 | D, W | Engineering |
| B2 | **OTP Resend Rate** | `otp_resend_tapped` / `otp_sent` x 100 | W | Engineering |
| B3 | **Phone Duplicate Rate** | `phone_duplicate_shown` / `send_otp_tapped` x 100 | W | Product |
| B4 | **Registration Fee Payment Rate** | `payment_completed (reg)` / `location_submitted` x 100 | W | Product + Finance |
| B5 | **Registration Fee Failure Rate** | `payment_failed (reg)` / `payment_initiated (reg)` x 100 | D, W | Engineering + Finance |
| B7 | **Reg Fee Pending (not paid)** | CSPs who reached Screen 4 but haven't paid after 24hrs | D | Sales/Ops |
| B8 | **Avg. Registration Time** | Median time from `send_otp_tapped` to `payment_completed (reg)` | W | Product |
| B9 | **Profile Completion Rate** | `personal_info_submitted` / `otp_verified_success` x 100 | W | Product |
| B10 | **Screen-wise Drop-off (Phase 1)** | For each screen 0→1→2→3→4: % who exit without proceeding | W | Product |

---

### C. Document & Verification Funnel (Screens 5-9)

These tell us: **Are CSPs submitting good documents? Where do they get stuck?**

| # | Metric | Formula | Frequency | Owner |
|---|--------|---------|-----------|-------|
| C1 | **KYC Completion Rate** | `kyc_all_completed` / `kyc_substage_entered (pan)` x 100 | W | Product |
| C2 | **KYC Sub-stage Drop-off** | % who complete PAN but don't start Aadhaar, % who complete Aadhaar but don't start GST | W | Product |
| C3 | **KYC Validation Error Rate** | `kyc_number_error_shown` / `kyc_number_validated` x 100, per doc type | W | Product |
| C4 | **Document Upload Success Rate** | `doc_upload_completed` / `doc_upload_started` x 100, per doc type | W | Engineering |
| C5 | **Document Upload Retake Rate** | `doc_upload_photo_retaken` / `doc_upload_photo_captured` x 100 | W | Product |
| C6 | **Sample Doc View Rate** | `view_sample_doc_tapped` / `doc_upload_started` x 100 | W | Product |
| C7 | **Bank Dedup Hit Rate** | `bank_dedup_found` / `bank_dedup_checked` x 100 | W | Product + Ops |
| C8 | **Bank Doc Upload Rate** | `bank_doc_upload_completed` / `add_bank_document_tapped` x 100 | W | Product |
| C9 | **ISP Agreement Upload Rate** | `isp_submitted` / screen 7 views x 100 | W | Product |
| C10 | **ISP Avg. Pages Uploaded** | Avg `total_pages` from `isp_upload_completed` | W | Product |
| C11 | **Photos Completion Rate** | `photos_submitted` / screen 8 views x 100 | W | Product |
| C12 | **Verification Submission Rate** | `verification_submitted` / `payment_completed (reg)` x 100 | W | Product |
| C13 | **Avg. Document Phase Time** | Median time from `kyc_substage_entered (pan)` to `photos_submitted` | W | Product |
| C14 | **Screen-wise Drop-off (Phase 2)** | For each screen 5→6→7→8→9: % who exit without proceeding | W | Product |
| C15 | **Upload Method Preference** | Distribution of `upload_source` (camera vs gallery) per doc type | M | Product |

---

### D. QA Review & Verification (Screen 9)

These tell us: **How efficient is the QA process? Why are CSPs getting rejected?**

| # | Metric | Formula | Frequency | Owner |
|---|--------|---------|-----------|-------|
| D1 | **QA Review TAT (Median)** | Median time from `verification_submitted` to `verification_approved` or `verification_rejected` | D, W | QA + Ops |
| D2 | **QA Approval Rate** | (Same as L5) | W | QA |
| D3 | **Rejection Reason Distribution** | % breakdown of `rejection_reason` from `qa_decision_made` | W, M | QA + Product |
| D4 | **QA Reversal Rate** | `qa_decision_changed` / `qa_decision_made` x 100 | W | QA |
| D5 | **Applications in Queue** | Count of CSPs with status "pending" on Screen 9 | D | QA + Ops |
| D6 | **QA Reviews per Reviewer** | Count of `qa_decision_made` grouped by `reviewer_id` | W | QA Lead |
| D7 | **Refund Success Rate** | `refund_completed` / `refund_initiated` x 100 | W | Finance |
| D8 | **Refund TAT** | Median `days_to_refund` from `refund_completed` | W | Finance |
| D9 | **CSPs Waiting for Review** | Count of CSPs on Screen 9 with status "pending" for > 3 business days | D | QA + Ops |

---

### E. Activation Funnel (Screens 10-14)

These tell us: **Are approved CSPs finishing the final steps?**

| # | Metric | Formula | Frequency | Owner |
|---|--------|---------|-----------|-------|
| E1 | **Tech Assessment TAT (Median)** | Median time from `tech_assessment_pending` to decision | W | Network Quality |
| E2 | **Tech Assessment Pass Rate** | (Same as L6) | W | Network Quality |
| E3 | **Tech Rejection Reason Distribution** | % breakdown of `rejection_reason` from `tech_assessment_rejected` | W, M | Network Quality + Product |
| E4 | **Policy Acceptance Rate** | `policy_accepted` / `policy_screen_viewed` x 100 | W | Product |
| E5 | **Policy Read Time** | Avg `time_on_screen_sec` from `policy_accepted` | W | Product |
| E6 | **Onboarding Fee Payment Rate** | `payment_completed (onboard)` / `payment_initiated (onboard)` x 100 | W | Finance |
| E7 | **Onboarding Fee Failure Rate** | `payment_failed (onboard)` / `payment_initiated (onboard)` x 100 | W | Engineering + Finance |
| E8 | **Account Setup Success Rate** | `account_setup_completed` / `account_setup_started` x 100 | W | Engineering |
| E9 | **Account Setup Failure Rate** | `account_setup_failed` / `account_setup_started` x 100 | D | Engineering |
| E10 | **Screen-wise Drop-off (Phase 3)** | For each screen 10→11→12→13→14: % who exit without proceeding | W | Product |

---

### F. Post-Onboarding & Full Activation

These tell us: **Are onboarded CSPs actually becoming active and serving customers?**

| # | Metric | Formula | Frequency | Owner |
|---|--------|---------|-----------|-------|
| F1 | **Partner Plus App Download Rate** | `app_download_tapped` / `onboarding_completed` x 100 | W | Product |
| F2 | **Partner Plus Login Rate** | Partner Plus logins / `onboarding_completed` x 100 | W | Product |
| F3 | **Training Completion Rate** | Training completed / Partner Plus logins x 100 | W | Training/Ops |
| F4 | **Time to Full Activation** | Median time from `onboarding_completed` to `CSP_ONBOARDED` | W, M | Product + Ops |
| F5 | **Drop-off: Onboarded but Not Activated** | CSPs with `onboarding_completed` but no `CSP_ONBOARDED` after 7 days | D, W | Ops |
| F6 | **First Customer Received** | Time from `CSP_ONBOARDED` to first customer allocated | W, M | Ops + Product |
| F7 | **CSPs Receiving Customers** | Count of CSPs who have received at least 1 customer in last 30 days | W, M | Business |
| F8 | **Avg. Customers per CSP** | Total Wiom customers served / Total active CSPs | M | Business |
| F9 | **CSP Retention (30-day)** | CSPs active at Day 30 / CSPs activated x 100 | M | Business |
| F10 | **CSP Retention (90-day)** | CSPs active at Day 90 / CSPs activated x 100 | M | Business |

---

### G. Payment & Financial Metrics

These tell us: **How is money flowing?**

| # | Metric | Formula | Frequency | Owner |
|---|--------|---------|-----------|-------|
| G1 | **Total Registration Fees Collected** | Sum of `payment_completed` where `fee_type = "registration"` | D, W, M | Finance |
| G2 | **Total Onboarding Fees Collected** | Sum of `payment_completed` where `fee_type = "onboarding"` | D, W, M | Finance |
| G3 | **Total Revenue from Onboarding** | G1 + G2 | M | Finance + Leadership |
| G4 | **Total Refunds Issued** | Sum of `refund_completed` amounts | W, M | Finance |
| G5 | **Net Revenue** | G3 - G4 | M | Finance + Leadership |
| G6 | **Payment Gateway Success Rate** | `payment_gateway_response (success)` / `payment_gateway_called` x 100 | D | Engineering |
| G7 | **Avg. Payment Retries** | Avg `retry_count` from `payment_completed` + `payment_failed` | W | Engineering |
| G8 | **Pending Registration Fees** | CSPs who reached Screen 4 but payment not completed | D | Sales/Ops |
| G9 | **Refund Failure Rate** | `refund_failed` / `refund_initiated` x 100 | W | Finance |

---

### H. Error & Friction Metrics

These tell us: **Where are CSPs hitting problems?**

| # | Metric | Formula | Frequency | Owner |
|---|--------|---------|-----------|-------|
| H1 | **Error Rate per Screen** | `error_displayed` count per `screen_number` / screen views x 100 | W | Engineering + Product |
| H2 | **Most Common Errors** | Top 5 `error_type` by count | W | Engineering |
| H3 | **Disabled CTA Tap Rate** | `disabled_cta_tapped` count per screen / screen views x 100 | W | Product |
| H4 | **Support Escalation Rate** | `talk_to_us_tapped` / total active CSPs x 100 | W | Support + Product |
| H5 | **Retry Success Rate** | Events succeeding after `error_retry_tapped` / `error_retry_tapped` x 100 | W | Engineering |
| H6 | **OTP Max Attempts Rate** | `otp_max_attempts_reached` / `otp_submitted` x 100 | W | Engineering |
| H7 | **Bank Dedup Block Rate** | `bank_dedup_found` / `add_bank_document_tapped` x 100 | W | Product |
| H8 | **App Crash Rate** | Crashes / sessions x 100 (from Crashlytics) | D | Engineering |
| H9 | **No Internet Error Rate** | `error_displayed (no_internet)` / total sessions x 100 | W | Engineering |
| H10 | **Uninstall Rate (pre-activation)** | `app_uninstalled` (before CSP_ONBOARDED) / total installs x 100 | W, M | Product + Growth |
| H11 | **Uninstall by Phase** | Distribution of `phase` at time of `app_uninstalled` | W, M | Product |
| H12 | **Uninstall by Screen** | Distribution of `last_screen_number` at time of `app_uninstalled` | W, M | Product |
| H13 | **Reinstall Rate** | `app_reinstalled` / `app_uninstalled` x 100 | W, M | Growth |
| H14 | **Reinstall from Notification** | `app_reinstalled` where `reinstall_source = "notification"` / R-series notifications sent x 100 | W | Growth |
| H15 | **Time to Reinstall** | Median `days_since_uninstall` from `app_reinstalled` | M | Growth |
| H16 | **Paid-but-Uninstalled** | CSPs who paid reg fee but uninstalled before completion | D, W | Ops + Product |

---

### I. Engagement & Behavior Metrics

These tell us: **How are CSPs using the app?**

| # | Metric | Formula | Frequency | Owner |
|---|--------|---------|-----------|-------|
| I1 | **Avg. Session Duration** | Avg `time_in_session_sec` from `app_backgrounded` | W | Product |
| I2 | **Avg. Screen Time per Screen** | Avg `time_on_screen_sec` grouped by `screen_number` | W | Product |
| I3 | **Language Preference** | % Hindi vs % English from `language` in events | M | Product |
| I4 | **Language Toggle Rate** | `language_toggled` / total sessions x 100 | M | Product |
| I5 | **Back Button Usage** | `back_button_pressed` count per screen | W | Product |
| I6 | **Sample Doc View Rate** | `view_sample_doc_tapped` / relevant upload screen views x 100 | W | Product |
| I7 | **Camera vs Gallery Preference** | `upload_source` distribution across all uploads | M | Product |
| I8 | **Sessions per CSP** | Total sessions / unique CSPs | W | Product |
| I9 | **Return Visits (incomplete)** | CSPs who left mid-flow and came back | W | Product |

---

### J. Nudge & Re-engagement Metrics

These tell us: **Are our reminders working?**

| # | Metric | Formula | Frequency | Owner |
|---|--------|---------|-----------|-------|
| J1 | **Nudge Delivery Rate** | `nudge_received` / `nudge_sent` x 100 | W | Engineering |
| J2 | **Nudge Open Rate** | `nudge_opened` / `nudge_received` x 100 | W | Growth + Product |
| J3 | **Nudge-to-Action Rate** | CSPs who paid reg fee within 24hrs of nudge / `nudge_opened` x 100 | W | Growth |
| J4 | **Day 4 Auto-Reject Count** | `auto_reject_triggered` count | W | Product + Ops |
| J5 | **Nudge Channel Effectiveness** | Open rate breakdown by `channel` (push / sms / whatsapp) | M | Growth |

---

### K. Geographic & Demographic Metrics

These tell us: **Where are CSPs coming from? What does the market look like?**

| # | Metric | Formula | Frequency | Owner |
|---|--------|---------|-----------|-------|
| K1 | **State-wise Registrations** | Count of `location_submitted` grouped by `state_name` | W, M | Sales + Leadership |
| K2 | **City-wise Registrations** | Count grouped by `city_name` | W, M | Sales |
| K3 | **State-wise Completion Rate** | `onboarding_completed` / `send_otp_tapped` grouped by state | M | Product + Sales |
| K4 | **City-wise Completion Rate** | Same, grouped by city | M | Product + Sales |
| K5 | **Top 10 Cities by Volume** | Cities with most `pitch_get_started_tapped` | M | Sales + Leadership |
| K6 | **Top 10 Cities by Conversion** | Cities with highest completion rate (min 10 starts) | M | Sales + Leadership |
| K7 | **Underperforming Regions** | States/cities with >50% drop-off before reg fee payment | M | Sales + Product |
| K8 | **New State/City Penetration** | Count of unique new states/cities per month | M | Leadership |

---

## Metric Hierarchy (Visual)

```
                    ┌─────────────────────────────┐
                    │      NORTH STAR METRIC       │
                    │    CSPs Fully Activated       │
                    │    (CSP_ONBOARDED count)      │
                    └──────────────┬──────────────┘
                                   │
              ┌────────────────────┼────────────────────┐
              │                    │                     │
     ┌────────▼────────┐  ┌───────▼────────┐  ┌────────▼────────┐
     │ L1. Onboarding  │  │ L2. Full       │  │ L3. Time to     │
     │ Completion Rate  │  │ Activation Rate│  │ Onboard (Median)│
     └────────┬────────┘  └───────┬────────┘  └────────┬────────┘
              │                    │                     │
     ┌────────▼────────┐  ┌───────▼────────┐  ┌────────▼────────┐
     │ L4. Phase-wise  │  │ L5. QA         │  │ L6. Tech        │
     │ Drop-off Rate   │  │ Approval Rate  │  │ Assessment Pass │
     └────────┬────────┘  └───────┬────────┘  └────────┬────────┘
              │                    │                     │
    ┌─────────┼─────────┐         │            ┌────────┼────────┐
    │         │         │         │            │        │        │
┌───▼──┐ ┌───▼──┐ ┌───▼──┐ ┌───▼──┐    ┌───▼──┐ ┌──▼───┐ ┌──▼───┐
│  A   │ │  B   │ │  C   │ │  D   │    │  E   │ │  F   │ │  G   │
│Acqui-│ │Reg.  │ │Doc & │ │QA    │    │Acti- │ │Post- │ │Pay-  │
│sition│ │Funnel│ │Verif.│ │Review│    │vation│ │Onb.  │ │ment  │
└──────┘ └──────┘ └──────┘ └──────┘    └──────┘ └──────┘ └──────┘

Cross-cutting: H (Errors), I (Engagement), J (Nudges), K (Geographic)
```

---

## Dashboard Views (by Function)

### Product Dashboard
- North Star + L1-L6 (leading metrics)
- Phase-wise drop-off funnel (visual)
- Screen-wise drop-off (heatmap)
- Avg. screen time per screen
- Error rate per screen
- Disabled CTA tap rate (friction signals)
- Sample doc view rate (content effectiveness)

### Sales & Growth Dashboard
- App installs (D, W, M trend)
- State/city-wise registrations (map view)
- Top 10 cities by volume and conversion
- Acquisition channel breakdown
- Pitch-to-registration conversion
- Underperforming regions

### QA Dashboard
- Applications in queue (live count)
- QA review TAT (median, P90)
- Approval vs rejection trend
- Rejection reason distribution (pie chart)
- Reviews per reviewer
- CSPs waiting > 3 days (alert)

### Finance Dashboard
- Registration fees collected (daily, cumulative)
- Onboarding fees collected (daily, cumulative)
- Refunds issued (count + amount)
- Net revenue
- Payment gateway success rate
- Pending payments (reg fee not paid)

### Engineering Dashboard
- OTP success rate
- Payment gateway success rate
- App crash rate
- Error rate per screen
- Upload success/failure rate
- Account setup success rate
- API response times

### Ops & Network Quality Dashboard
- Tech assessment queue (live count)
- Tech assessment TAT
- Pass vs rejection trend
- Rejection reason distribution
- CSPs onboarded but not activated (action needed)
- Nudge effectiveness

### Leadership Dashboard
- North Star (CSPs Fully Activated — monthly trend)
- Total CSPs at each stage (funnel view)
- Revenue (total collected, net of refunds)
- Geographic expansion (new states/cities)
- Time to onboard (trend)
- Phase-wise conversion rates

---

## Cohort Analysis

Track the following as weekly/monthly cohorts to see if the onboarding experience is improving over time:

| Cohort Type | Definition | Metrics to Track |
|-------------|------------|-----------------|
| **Registration Cohort** | CSPs who started onboarding in Week X | Completion rate, time to onboard, drop-off screen |
| **Payment Cohort** | CSPs who paid reg fee in Week X | Verification submission rate, approval rate, time to verification |
| **Activation Cohort** | CSPs who got approved in Week X | Time to pay onboarding fee, full activation rate, time to first customer |
| **Geographic Cohort** | CSPs from State/City X | Completion rate, pass rate, drop-off compared to other regions |

---

## Alerts & Thresholds

Set up automated alerts when metrics cross these thresholds:

| Alert | Trigger | Notify |
|-------|---------|--------|
| QA Queue Backlog | Applications pending > 3 business days | QA Lead + Ops |
| Tech Assessment Backlog | Assessments pending > 5 business days | Network Quality Lead |
| Payment Gateway Down | Payment success rate < 80% for 1 hour | Engineering + Finance |
| High Error Rate | Error rate on any screen > 20% for 1 day | Engineering |
| High Drop-off | Any screen drop-off > 50% for 1 week | Product |
| Low OTP Delivery | OTP success rate < 90% for 1 day | Engineering |
| Refund Failures | Refund failure rate > 10% for 1 week | Finance |
| Nudge Ineffectiveness | Nudge open rate < 10% for 1 week | Growth |
| Stale CSPs | CSPs inactive for > 7 days post reg fee payment | Ops + Sales |

---

## Total Metrics Summary

| Category | Count | Section |
|----------|-------|---------|
| North Star | 1 | — |
| Leading Metrics | 6 | L1-L6 |
| Acquisition & TOF | 7 | A1-A7 |
| Registration Funnel | 10 | B1-B10 |
| Document & Verification | 15 | C1-C15 |
| QA Review | 9 | D1-D9 |
| Activation Funnel | 10 | E1-E10 |
| Post-Onboarding | 10 | F1-F10 |
| Payment & Financial | 9 | G1-G9 |
| Error & Friction | 9 | H1-H9 |
| Engagement & Behavior | 9 | I1-I9 |
| Nudge & Re-engagement | 5 | J1-J5 |
| Geographic & Demographic | 8 | K1-K8 |
| **Total** | **~108 metrics** | |

---

*This framework covers the complete product analytics for the Wiom CSP Onboarding App — from app discovery to CSP receiving their first customer. Designed for all functions: Product, Sales, QA, Finance, Engineering, Ops, Network Quality, and Leadership.*
