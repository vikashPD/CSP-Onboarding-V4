# M1 — Authentication

**Version:** 1.0 | **Date:** 05 April 2026 | **Status:** Production Specification
**Parent:** [ONBOARDING_SERVICE_MODULES.md](ONBOARDING_SERVICE_MODULES.md)
**Parent PRD:** [PRD_HUMAN.md v3.2](../PRD_HUMAN.md)

> **Sections 1–7** describe this module's role in principle — they remain stable across PRD versions.
> **Sections 8–12** reference the current PRD version (v3.2) and are marked accordingly.

---

## Table of Contents

1. [Module Overview](#1-module-overview)
2. [Objective](#2-objective)
3. [Explain Like I'm 10](#3-explain-like-im-10)
4. [This Module IS Responsible For](#4-this-module-is-responsible-for)
5. [This Module is NOT Responsible For](#5-this-module-is-not-responsible-for)
6. [Boundary Prohibitions](#6-boundary-prohibitions)
7. [Dependency Declaration](#7-dependency-declaration)
8. [Approved Inputs](#8-approved-inputs)
9. [Screen-by-Screen Specification (per PRD v3.2)](#9-screen-by-screen-specification-per-prd-v32)
10. [Error Scenarios (per PRD v3.2)](#10-error-scenarios-per-prd-v32)
11. [Validation Rules & Checks (per PRD v3.2)](#11-validation-rules--checks-per-prd-v32)
12. [Test Cases (per PRD v3.2)](#12-test-cases-per-prd-v32)

---

## 1. Module Overview

| Field | Value |
|-------|-------|
| **Module ID** | M1 |
| **Formal Name** | Authentication |
| **Phase** | Phase 1 — Registration |
| **Purpose** | Verify the partner owns a unique identity and establish an authenticated session |
| **Screens Owned (v3.2)** | Screen 0: Phone Entry (auth portion only — T&C checkbox is owned by M2) |
|  | Screen 1: OTP Verification |
| **Onboarding Flow Position** | First module — nothing happens before Auth completes |
| **Downstream Consumers** | M2 (Terms & Conditions), M3 (Registration), and transitively all other modules |

---

## 2. Objective

Verify that the partner owns a unique identity and establish an authenticated session before any other module can proceed.

This module is the **front door** of the onboarding journey. It answers two questions:
1. **Is this identity real?** — Verified through a configured verification method (currently: OTP sent to mobile number).
2. **Is this identity unique?** — Checked against the existing partner database to prevent duplicates.

Once both conditions are met, an authenticated session is created, and the verified identity is passed downstream as the partner's identity anchor for the rest of the journey.

---

## 3. Explain Like I'm 10

Before joining the Wiom shopkeeper club, you prove who you are. The app checks your identity using a verification method — if it matches, you're in. If someone else already joined with that identity, the app says "already taken" — because one person, one identity, one partnership. That's all this module does: check who you are, verify it's really you, and open the door.

---

## 4. This Module IS Responsible For

### 4.1 Identity Capture
- Present a screen to capture the partner's identity credential (currently: mobile number)
- Validate the credential format as per configured rules
- Ensure only valid-format credentials can proceed to the verification step

### 4.2 Duplicate Check
- Check the submitted identity against the existing partner database
- If the identity is already registered:
  - Block the partner from proceeding
  - Show an appropriate error with alternative actions
- In future: re-entry may be allowed based on the [Exclusion Policy](../PRD_HUMAN.md) which defines CSP re-entry rules into the Wiom ecosystem

### 4.3 Identity Verification
- Initiate a verification challenge to the submitted identity (currently: send OTP via SMS provider)
- Present a screen for the partner to respond to the challenge (currently: enter OTP)
- Verify the partner's response against the expected value
- Manage the verification lifecycle:
  - **Expiry:** Verification challenge expires after a configurable duration
  - **Resend:** Partner can request a new challenge; must wait for the current timer to exhaust before resending
  - **Attempt limits:** Maximum wrong attempts before blocking (configurable)
  - **Resend limits:** May be configured in future

### 4.4 Session Establishment
- On successful verification, create an authenticated session
- The session is used by all downstream modules throughout the onboarding journey
- Pass the verified identity to M2 and M3 as the identity anchor

### 4.5 Channel Support
- **Current:** Mobile number + SMS OTP
- **Future possible:** Email, WhatsApp OTP, or other verification methods
- The module must be designed to support swapping the verification method without changing its responsibilities or boundaries

---

## 5. This Module is NOT Responsible For

| What | Owned By |
|------|----------|
| Presenting or recording T&C acceptance | M2 — Terms & Conditions |
| T&C checkbox logic (even if it appears on the same screen as identity capture) | M2 — Terms & Conditions |
| Collecting personal details (name, email, etc.) | M3 — Registration |
| Collecting business details or location | M3 — Registration |
| Any payment processing or fee configuration | M4 — Fee Collection |
| KYC document collection or verification | M5 — Verification & Assessment |
| Policy or SLA presentation | M6 — CSP Policy |
| Account creation in backend systems | M7 — CSP Account Setup |
| Defining or enforcing the Exclusion Policy | Compliance / Operational Systems |

---

## 6. Boundary Prohibitions

This module must **NEVER:**

| # | Prohibition | Reason |
|---|-------------|--------|
| 1 | Store verification secrets (e.g., OTP) in plain text or client-side storage | Security — verification secrets must be server-side and encrypted |
| 2 | Allow unlimited wrong verification attempts | Prevents brute-force attacks on the verification challenge |
| 3 | Allow anyone to bypass identity verification and proceed to M2/M3 | Auth is the mandatory first gate — no exceptions |
| 4 | Allow a duplicate identity to proceed (unless Exclusion Policy re-entry rules apply) | One identity = one active partnership |
| 5 | Collect, store, or process any data beyond the identity credential and session | Scope creep — personal/business data belongs to M3 |
| 6 | Share the identity credential or verification secrets with any service outside its declared dependencies | Data privacy — only declared dependencies receive data |
| 7 | Modify the identity credential after verification succeeds | The verified identity is immutable — downstream modules receive it as-is |
| 8 | Make assumptions about which verification channel is used | Module must work with any configured verification method |

---

## 7. Dependency Declaration

### 7.1 This Module Depends On

| Dependency | Type | Purpose | Notes |
|------------|------|---------|-------|
| SMS / Messaging Provider | 3P | Send verification challenges (OTP) to partner's identity | Currently: Gupshup. Must be generic — provider can be swapped. |
| Wiom Partner Database | Internal | Check if identity is already registered (duplicate check) | Exact table/schema to be confirmed with tech team |
| Session Management Service | Internal | Create and manage authenticated session after verification | Implementation (JWT, Firebase Auth, etc.) to be confirmed with tech team |

### 7.2 This Module is Depended On By

| Module | Why |
|--------|-----|
| M2 — Terms & Conditions | Cannot present T&C until identity is verified |
| M3 — Registration | Receives verified identity as the partner's anchor for the entire journey |
| All downstream modules (M4–M7) | Transitively depend on the authenticated session created here |

### 7.3 This Module Must NOT Depend On

| Service / Module | Reason |
|-----------------|--------|
| M2, M3, M4, M5, M6, M7 | Auth is upstream — must be completely independent of downstream modules |
| Payment Gateway | Auth has no business with payments |
| KYC / Verification APIs | Auth verifies identity ownership, not identity documents |
| Wiom CMS | Auth does not serve content |
| Wiom Notification Service | Auth does not send nudges or reminders (notifications for drop-off recovery are handled by the shared Notification Service, not triggered by this module) |

---

## 8. Approved Inputs

> *Per PRD v3.2. May change in future versions.*

### 8.1 Screen 0 — Phone Entry (Auth portion only)

| Input | Type | Rules |
|-------|------|-------|
| Mobile Number | Numeric | Exactly 10 digits. Prefixed with +91 (country code, non-editable). Numeric only — no letters, spaces, or special characters. |

**Note:** The T&C checkbox on this screen is owned by M2. M1 only owns the phone number input and "Send OTP" CTA. However, the CTA is enabled only when **both** M1's condition (10 digits entered) **and** M2's condition (T&C checked) are met — this is a **cross-module CTA dependency**.

### 8.2 Screen 1 — OTP Verification

| Input | Type | Rules |
|-------|------|-------|
| OTP | Numeric | Exactly 4 digits. Auto-focus on first digit. Numeric only. |

---

## 9. Screen-by-Screen Specification (per PRD v3.2)

### Screen 0: Phone Entry

**Purpose:** Capture the partner's mobile number.

**What the partner sees:**
- Header: "Wiom Partner+"
- Phone input field with +91 country code prefix (non-editable)
- 10-digit phone number input (numeric keyboard)
- T&C checkbox with link to terms *(owned by M2 — displayed on this screen but not this module's logic)*
- CTA button: "Send OTP"

**CTA Enable Logic (cross-module):**
- CTA is disabled by default
- CTA enables only when **both** conditions are true:
  - M1 condition: Exactly 10 digits entered
  - M2 condition: T&C checkbox is checked
- If either condition becomes false (digits deleted, T&C unchecked), CTA disables again

**On CTA Tap:**
1. System checks if the mobile number is already registered in the partner database (duplicate check)
2. If **duplicate found** → show duplicate error (see [Error Scenarios §10](#10-error-scenarios-per-prd-v32))
3. If **clear** → send OTP to the number via SMS provider → navigate to Screen 1

**Behaviour:**
- Only numeric input allowed — non-numeric characters are blocked at keyboard level
- Phone number field does not accept more than 10 digits
- No validation error shown while typing — validation happens on CTA tap

---

### Screen 1: OTP Verification

**Purpose:** Verify the partner owns the mobile number they entered.

**What the partner sees:**
- Message showing which number the OTP was sent to (e.g., "OTP sent to +91 98XXXXX890")
- 4-digit OTP input boxes (auto-focus on first box)
- Countdown timer starting at configured duration (currently: 28 seconds)
- CTA button: "सत्यापित करें" / "Verify"
- After timer expires: "Resend OTP" link + "Change Number" link

**CTA Enable Logic:**
- CTA disabled until all 4 digits are filled

**On CTA Tap:**
1. System verifies entered OTP against the sent OTP
2. If **correct** → create authenticated session → navigate to M2/M3 (next in flow)
3. If **wrong** → show wrong OTP error (see [Error Scenarios §10](#10-error-scenarios-per-prd-v32))

**Timer Behaviour:**
- Counts down 1 second at a time from the configured start value
- While timer is active: "Resend OTP" and "Change Number" are hidden
- When timer reaches 0: "Resend OTP" and "Change Number" links appear
- On "Resend OTP" tap: new OTP is sent, timer restarts from the beginning
- On "Change Number" tap: navigate back to Screen 0, phone field is editable again

**Auto-advance Behaviour:**
- OTP input auto-focuses on next box after each digit is entered
- When 4th digit is entered, CTA becomes enabled (partner still needs to tap it)

---

## 10. Error Scenarios (per PRD v3.2)

### 10.1 Phone Duplicate

| Field | Value |
|-------|-------|
| **Trigger** | Partner enters a mobile number that is already registered in the partner database |
| **When** | On "Send OTP" tap, after duplicate check |
| **What the partner sees** | Error card explaining the number is already registered. Alternative CTAs provided. |
| **Outcome** | **Blocking** — partner cannot proceed with this number |
| **Recovery** | Enter a different number, or contact support. In future: re-entry may be possible per Exclusion Policy. |

### 10.2 Wrong OTP

| Field | Value |
|-------|-------|
| **Trigger** | Partner enters an incorrect OTP |
| **When** | On "Verify" tap |
| **What the partner sees** | OTP input boxes turn red. Attempt counter shown (e.g., "2 of 3 attempts remaining"). |
| **Outcome** | **Retryable** — partner can try again up to the configured max attempts (currently: 3) |
| **After max attempts** | Blocked. Partner must request a new OTP via "Resend OTP" (after timer expires) to reset the attempt counter. |

### 10.3 OTP Expired

| Field | Value |
|-------|-------|
| **Trigger** | Partner does not enter OTP before the timer expires |
| **When** | Timer reaches 0 |
| **What the partner sees** | OTP input boxes become faded/disabled. "Resend OTP" and "Change Number" links appear. |
| **Outcome** | **Retryable** — partner can request a new OTP |
| **Recovery** | Tap "Resend OTP" to get a fresh OTP and restart the timer. Or tap "Change Number" to go back. |

### 10.4 Network / Server Errors

| Field | Value |
|-------|-------|
| **Trigger** | No internet connectivity or server is unreachable |
| **When** | Any action on Screen 0 or Screen 1 |
| **What the partner sees** | Error overlay (No Internet or Server Error) — as per shared concern in master doc |
| **Outcome** | **Blocking** until connectivity is restored |
| **Recovery** | Automatic — overlay disappears when connection is back. No data loss. |

---

## 11. Validation Rules & Checks (per PRD v3.2)

### 11.1 Client-Side Validations

| Field | Rule | When Validated | Error Behaviour |
|-------|------|----------------|-----------------|
| Mobile Number | Exactly 10 digits | CTA remains disabled until 10 digits are entered | No explicit error message — CTA simply stays disabled |
| Mobile Number | Numeric only | On each keystroke | Non-numeric input is blocked at keyboard level |
| Mobile Number | Max 10 digits | On each keystroke | Extra digits beyond 10 are blocked |
| OTP | Exactly 4 digits | CTA remains disabled until 4 digits are entered | No explicit error message — CTA simply stays disabled |
| OTP | Numeric only | On each keystroke | Non-numeric input is blocked at keyboard level |

### 11.2 Server-Side Validations

| Check | When | Success Path | Failure Path |
|-------|------|--------------|-------------|
| Phone duplicate check | On "Send OTP" tap, before sending OTP | OTP is sent, navigate to Screen 1 | Duplicate error shown (§10.1) |
| OTP verification | On "Verify" tap | Session created, proceed to next module | Wrong OTP error (§10.2) |
| OTP expiry check | On "Verify" tap | OTP is still valid, verification proceeds | OTP expired error (§10.3) |

### 11.3 Configurable Parameters

| Parameter | Current Value (v3.2) | Configurable? |
|-----------|---------------------|---------------|
| Identity method | Mobile number + SMS OTP | Yes — future: email, WhatsApp OTP, etc. |
| Country code | +91 (India) | Yes |
| Phone number length | 10 digits | Yes |
| OTP length | 4 digits | Yes |
| OTP expiry duration | 28 seconds | Yes |
| Max wrong OTP attempts | 3 | Yes |
| Max OTP resends per session | No limit (currently) | Configurable for future |
| Cooldown between resends | Must wait for timer to exhaust | Yes |

---

## 12. Test Cases (per PRD v3.2)

### 12.1 Happy Path

| ID | Test | Steps | Expected Result |
|----|------|-------|-----------------|
| HP-01 | Complete phone + OTP flow | Enter 10-digit number → check T&C → tap Send OTP → enter correct 4-digit OTP → tap Verify | Session created, partner proceeds to next module |
| HP-02 | CTA disabled without 10 digits | Enter 9 digits with T&C checked | "Send OTP" CTA remains disabled |
| HP-03 | CTA disabled without T&C | Enter 10 digits with T&C unchecked | "Send OTP" CTA remains disabled |
| HP-04 | CTA enables on both conditions met | Enter 10 digits + check T&C | "Send OTP" CTA becomes enabled |
| HP-05 | CTA disables when T&C unchecked | Enter 10 digits + check T&C + uncheck T&C | "Send OTP" CTA becomes disabled again |
| HP-06 | OTP timer countdown | Navigate to Screen 1 | Timer starts at 28s, counts down 1/sec |
| HP-07 | Resend OTP after timer expires | Wait for timer → tap "Resend OTP" | New OTP sent, timer restarts at 28s |
| HP-08 | Change Number from OTP screen | Wait for timer → tap "Change Number" | Returns to Screen 0, phone field editable |
| HP-09 | OTP auto-focus | Navigate to Screen 1 | First OTP box is auto-focused, advances to next box after each digit |
| HP-10 | Language toggle on Auth screens | Toggle Hindi/English on Screen 0 and Screen 1 | All text switches correctly |

### 12.2 Error Scenarios

| ID | Test | Steps | Expected Result |
|----|------|-------|-----------------|
| ERR-01 | Phone duplicate | Enter already-registered number → tap Send OTP | Duplicate error card shown, partner blocked |
| ERR-02 | Wrong OTP — first attempt | Enter wrong 4-digit OTP → tap Verify | Red boxes, "2 of 3 attempts remaining" |
| ERR-03 | Wrong OTP — max attempts | Enter wrong OTP 3 times | Blocked. Must resend OTP to try again. |
| ERR-04 | OTP expired | Wait for timer to expire without entering OTP | Boxes faded, "Resend OTP" and "Change Number" appear |
| ERR-05 | No internet on Send OTP | Disable network → tap Send OTP | No Internet overlay shown |
| ERR-06 | No internet on Verify | Disable network → tap Verify | No Internet overlay shown |
| ERR-07 | Server error on Send OTP | Simulate server down → tap Send OTP | Server Error overlay shown |
| ERR-08 | Server error on Verify | Simulate server down → tap Verify | Server Error overlay shown |

### 12.3 Edge Cases

| ID | Test | Steps | Expected Result |
|----|------|-------|-----------------|
| EDGE-01 | Phone >10 digits | Try entering 11+ digits | Extra digits blocked |
| EDGE-02 | Non-numeric phone input | Try entering letters/special chars | Input blocked at keyboard level |
| EDGE-03 | Non-numeric OTP input | Try entering letters/special chars in OTP boxes | Input blocked at keyboard level |
| EDGE-04 | Resend OTP while timer active | Check if "Resend OTP" is visible while timer is counting | "Resend OTP" is hidden while timer is active |
| EDGE-05 | Navigate back from OTP screen, then forward | Go to Screen 1 → back to Screen 0 → re-enter → back to Screen 1 | Phone number preserved on Screen 0; OTP screen resets |
| EDGE-06 | Session expiry mid-auth | Wait for session timeout during OTP screen | Handled as per shared concern — re-auth required, no data loss |
| EDGE-07 | Multiple rapid CTA taps | Tap "Send OTP" rapidly multiple times | Only one OTP is sent — CTA debounced |

### 12.4 UAT Test Cases

| ID | Persona | Scenario | Acceptance Criteria |
|----|---------|----------|---------------------|
| UAT-01 | New partner, valid number | Happy path — phone + OTP | Reaches next module with verified session |
| UAT-02 | Partner with duplicate number | Already registered number | Sees duplicate error, cannot proceed |
| UAT-03 | Partner who misremembers OTP | Enters wrong OTP twice, correct on 3rd | Succeeds on 3rd attempt |
| UAT-04 | Partner with slow typing | OTP timer expires before entry | Sees resend option, resends, completes verification |
| UAT-05 | Partner in low-connectivity area | Intermittent network | Sees appropriate error overlays, no data loss, completes on retry |
| UAT-06 | Hindi-speaking partner | Full flow in Hindi | All text is correct Hindi, culturally appropriate |

---

## Appendix: Cross-Module CTA Dependency

Screen 0 has a **shared CTA** ("Send OTP") that depends on conditions from two modules:

```
┌──────────────────────────────────────────────┐
│              Screen 0: Phone Entry            │
│                                              │
│  ┌─────────────────────────────────────────┐  │
│  │  M1 owns: Phone number input (+91)      │  │
│  │  Condition: 10 digits entered           │  │
│  └─────────────────────────────────────────┘  │
│                                              │
│  ┌─────────────────────────────────────────┐  │
│  │  M2 owns: T&C checkbox                  │  │
│  │  Condition: Checkbox checked             │  │
│  └─────────────────────────────────────────┘  │
│                                              │
│  ┌─────────────────────────────────────────┐  │
│  │  Shared CTA: "Send OTP"                 │  │
│  │  Enabled: M1 condition AND M2 condition │  │
│  └─────────────────────────────────────────┘  │
│                                              │
└──────────────────────────────────────────────┘
```

**Implementation note:** The CTA enable/disable logic must evaluate both modules' conditions. Neither module alone can enable or disable the CTA.

---

*This document covers the complete specification of the Authentication module (M1) for the Wiom CSP Onboarding Service. For the overall module architecture, see [ONBOARDING_SERVICE_MODULES.md](ONBOARDING_SERVICE_MODULES.md).*
