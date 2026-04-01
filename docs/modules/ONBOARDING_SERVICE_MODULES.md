# Wiom CSP Onboarding Service — Module Architecture Document

**Version:** 1.1 | **Date:** 01 April 2026 | **Status:** Production Specification
**Parent PRD:** [PRD_HUMAN.md v3.2](../PRD_HUMAN.md)
**Audience:** Product, Engineering, QA, Business Stakeholders, External Vendors

---

## Table of Contents

1. [Purpose](#1-purpose)
2. [Onboarding Service Overview](#2-onboarding-service-overview)
3. [Module Registry](#3-module-registry)
4. [Module Details](#4-module-details)
5. [Inter-Module Dependency Map](#5-inter-module-dependency-map)
6. [Complete Connection Matrix](#6-complete-connection-matrix)
7. [Onboarding Flow Sequence](#7-onboarding-flow-sequence)
8. [Shared Concerns](#8-shared-concerns-applicable-to-all-modules)
9. [Glossary](#9-glossary)

---

## 1. Purpose

This document breaks down the CSP Onboarding flow into **7 service modules** — their objectives, boundaries, and connections. It is the single-page map for any stakeholder to understand what each module does, what it must never do, and how they all fit together.

**For detailed specs** (screens, validations, errors, test cases) → see individual module documents: `M1_AUTH.md` through `M7_CSP_ACCOUNT_SETUP.md`

---

## 2. Onboarding Service Overview

The onboarding service takes a new Channel Sales Partner from "I'm interested" to "I'm onboarded and ready to serve customers" — **15 screens + 1 Pitch**, **3 phases**, **7 modules**.

| Phase | Name | What Happens | Modules |
|-------|------|--------------|---------|
| **Phase 1** | Registration | Sign up, provide details, pay Rs.2K | M1, M2, M3, M4 |
| **Phase 2** | Verification | Submit documents, QA review | M5 |
| **Phase 3** | Activation | Tech assessment, policy, pay Rs.20K, account setup | M4, M5, M6, M7 |

| Metric | Value |
|--------|-------|
| Total Screens | 15 + 1 Pitch |
| Service Modules | 7 |
| Error Scenarios | 22 across 8 categories |
| Total Fees | Rs.22,000 (Rs.2,000 + Rs.20,000) |
| Dashboards | 2 (Control + QA Review) |

---

## 3. Module Registry

| ID | Formal Name | Phase(s) | One-Line Purpose |
|----|-------------|----------|------------------|
| **M1** | Authentication Service | Phase 1 | Verify partner's mobile number via OTP and establish identity |
| **M2** | Terms & Conditions Service | Phase 1 | Manage T&C versions, present terms, record acceptance |
| **M3** | Registration Service | Phase 1 | Collect personal, business, and location details |
| **M4** | Fee Collection Service | Phase 1, 3 | Collect Rs.2K and Rs.20K payments, handle refunds |
| **M5** | Verification & Assessment Service | Phase 2, 3 | Collect documents, facilitate QA review, conduct tech assessment |
| **M6** | CSP Policy Service | Phase 3 | Present policies, SLA, commissions; record acceptance |
| **M7** | CSP Account Setup Service | Phase 3 | Create backend accounts, confirm onboarding |

---

## 4. Module Details

> Each module below is summarised at a glance. Full specs live in the respective module document.

---

### M1 — Authentication Service

📄 **Detail doc:** [`M1_AUTH.md`](M1_AUTH.md)

**Objective:** Verify the partner owns a real, unique mobile number before anything else happens.

**Explain Like I'm 10:**
Before joining the Wiom shopkeeper club, you prove you have a real phone number. The app sends a secret code, you type it back. If it matches — you're in. If that number is already taken — sorry, one person, one number. That's all this module does: check your phone, check the code, open the door.

**IS Responsible For:**
- Capture + validate 10-digit mobile number (+91)
- Phone duplicate check
- Send OTP, verify OTP, manage OTP lifecycle (expiry, resend, 3-attempt limit)
- Establish authenticated session
- Pass verified phone number to downstream modules

**Is NOT Responsible For:**
- Collecting name, email, or any personal details (→ M3)
- T&C presentation or acceptance (→ M2)
- Payments (→ M4)
- KYC or document handling (→ M5)

**Must NEVER:**
- Store OTPs in plain text or client-side
- Allow >3 wrong OTP attempts without blocking
- Let anyone bypass OTP to proceed forward
- Accept phone numbers ≠ 10 digits
- Let a duplicate phone proceed

**Dependencies:**

| Depends On | Why |
|------------|-----|
| SMS/WhatsApp OTP Gateway (3P) | Send OTP |
| Wiom User Registry (Internal) | Phone duplicate check |
| Session Management (Internal) | Create authenticated session |

| Depended On By | Why |
|----------------|-----|
| M2 — TnC Service | Needs verified phone before showing T&C |
| M3 — Registration Service | Receives verified phone as identity anchor |

| Must NOT Depend On |
|---------------------|
| M3, M4, M5, M6, M7, Payment Gateway, KYC APIs |

---

### M2 — Terms & Conditions Service

📄 **Detail doc:** [`M2_TNC.md`](M2_TNC.md)

**Objective:** Present the current T&C, record the partner's explicit acceptance with version and timestamp, and block progress until accepted.

**Explain Like I'm 10:**
Before playing any game, you read the rules and say "I agree." This module is the rulebook. Rules can change over time, so it tracks every version, shows the latest, and records exactly when you agreed. If rules change later, Wiom knows which version you signed.

**IS Responsible For:**
- Maintain T&C version registry (version, content/link, created timestamp)
- Serve the latest T&C for display
- Record acceptance per partner (version accepted, timestamp)
- Store full acceptance history (append-only)
- Provide "has this partner accepted latest T&C?" check to other modules
- Block forward progress until accepted

**Is NOT Responsible For:**
- Drafting T&C content (→ Legal/Compliance)
- Authentication (→ M1)
- Collecting personal details (→ M3)
- Enforcing T&C terms post-onboarding (→ operational systems)

**Must NEVER:**
- Let a partner proceed to M3 without recorded acceptance
- Modify or interpret T&C content — store and serve only
- Delete past acceptance records — append-only audit trail
- Pre-check the checkbox for the partner

**Dependencies:**

| Depends On | Why |
|------------|-----|
| M1 — Authentication Service | Partner must be verified first |
| Wiom CMS / Legal Content Service (Internal) | Fetch T&C versions and content |
| Wiom Partner Database (Internal) | Store acceptance records |

| Depended On By | Why |
|----------------|-----|
| M3 — Registration Service | Cannot begin until T&C accepted |

| Must NOT Depend On |
|---------------------|
| M3, M4, M5, M6, M7, Payment Gateway, KYC APIs |

---

### M3 — Registration Service

📄 **Detail doc:** [`M3_REGISTRATION.md`](M3_REGISTRATION.md)

**Objective:** Build the partner's profile — personal identity, business details, and physical location. This is the foundation every downstream module references.

**Explain Like I'm 10:**
Now you tell Wiom who you are: "My name is Rajesh, my shop is Rajesh Telecom, I'm in Indore, here's my address." Wiom needs this to send equipment, show your shop to customers, and match your name against your ID documents later. It's like filling a membership form — name, address, shop type.

**IS Responsible For:**
- Collect: Full Name (as per Aadhaar), Email, Entity Type, Business Name
- Collect: State, City, Pincode, Full Address, GPS coordinates
- Validate all fields (format, required, completeness)
- Receive verified phone number from M1
- Provide profile data as read-only reference to M4, M5, M6, M7
- Lock Business Name after M4 confirms Rs.2K payment

**Is NOT Responsible For:**
- Identity verification against government databases (→ M5)
- Payments (→ M4)
- Phone/OTP verification (→ M1)
- T&C (→ M2)
- KYC, bank, or document collection (→ M5)

**Must NEVER:**
- Allow progress to M4 with any required field empty
- Modify the verified phone number from M1
- Perform KYC verification or collect documents
- Accept entity types other than Proprietorship (current version)
- Allow Business Name edits after Rs.2K is paid

**Dependencies:**

| Depends On | Why |
|------------|-----|
| M1 — Authentication Service | Verified phone as identity anchor |
| M2 — Terms & Conditions Service | T&C must be accepted first |
| Wiom Geo/Location Service (Internal) | GPS capture, location validation |
| Wiom Partner Database (Internal) | Store partner profile |

| Depended On By | Why |
|----------------|-----|
| M4 — Fee Collection | Needs Business Name; triggers trade name lock |
| M5 — Verification | References Name, Business Name for cross-checking |
| M7 — Account Setup | Uses full profile for backend accounts |

| Must NOT Depend On |
|---------------------|
| M4, M5, M6, M7, Payment Gateway, KYC APIs |

---

### M4 — Fee Collection Service

📄 **Detail doc:** [`M4_FEE.md`](M4_FEE.md)

**Objective:** Collect payments at two points — Rs.2,000 (refundable, Phase 1) and Rs.20,000 (non-refundable, Phase 3). Handle success, failure, timeout, and refunds.

**Explain Like I'm 10:**
Joining the Wiom club costs money. First you pay Rs.2,000 as a "serious applicant" deposit — if Wiom says no, you get it back. If you pass all checks, you pay Rs.20,000 to become a real partner and get WiFi equipment. This module is the cashier: takes money, gives receipts, and returns money if needed.

**IS Responsible For:**
- Present Rs.2K payment screen (Phase 1) and Rs.20K payment screen (Phase 3)
- Display investment summary (Rs.2K paid + Rs.20K due = Rs.22K)
- Initiate transactions via payment gateway
- Handle payment success, failure, timeout
- Process refunds when triggered by M5 (verification rejected)
- Show refund status (Success / In Progress / Failed)
- Trigger trade name lock in M3 after Rs.2K payment
- Manage payment reminder nudges (Day 1-4)

**Is NOT Responsible For:**
- Deciding whether a refund should happen (→ M5 decides)
- Collecting partner information (→ M1, M2, M3)
- Document verification (→ M5)
- Determining payment amounts — these are business constants

**Must NEVER:**
- Initiate payment without prerequisite modules completed
- Store PCI-sensitive data (card numbers, CVV) on client
- Modify payment amounts (Rs.2K and Rs.20K are constants)
- Process a refund without M5's rejection trigger
- Auto-retry payment without explicit partner tap

**Dependencies:**

| Depends On | Why |
|------------|-----|
| M3 — Registration Service | Partner profile for payment screens |
| M5 — Verification Service | For Rs.20K: verification approved + tech passed. For refunds: rejection trigger |
| M6 — CSP Policy Service | Policy accepted before Rs.20K payment |
| Razorpay (3P) | Process payments and refunds |
| Wiom Ledger / Finance Service (Internal) | Record transactions |
| Wiom Notification Service (Internal) | Confirmations, reminders, refund updates |

| Depended On By | Why |
|----------------|-----|
| M5 — Verification Service | Phase 2 starts after Rs.2K confirmed |
| M7 — Account Setup | Starts after Rs.20K confirmed |
| M3 — Registration Service | Trade name lock after Rs.2K |

| Must NOT Depend On |
|---------------------|
| M1, M2, KYC APIs |

---

### M5 — Verification & Assessment Service

📄 **Detail doc:** [`M5_VERIFICATION_ASSESSMENT.md`](M5_VERIFICATION_ASSESSMENT.md)

**Objective:** Collect all documents, run checks, facilitate human QA review, and conduct technical assessment. This is the gatekeeper — it decides whether a partner qualifies.

**Explain Like I'm 10:**
You've told Wiom your name and paid your deposit. Now they check if you're the real deal — like trying out for a cricket team. Show your ID card (KYC), bank passbook, your contract with the stadium (ISP agreement), and photos of your gear and ground. A review team checks everything. If approved, a technical team checks if your ground is good enough. Only if BOTH pass do you move forward. Documents fail → money back. Technical fails → no refund.

**IS Responsible For:**
- KYC collection: PAN, Aadhaar, GST numbers + document uploads + regex validation on blur
- KYC cross-validation: GST chars 3-12 must match PAN
- Bank details: Account Number, Re-enter, IFSC + dedup check + mandatory bank document upload
- ISP Agreement: multi-page upload (PDF + up to 7 photos)
- Shop & Equipment Photos: shop front (1) + equipment (up to 5)
- Sample documents for all upload screens
- Verification status display and document checklist
- Send data to QA Review Dashboard, receive approved/rejected decisions
- Trigger refund in M4 on verification rejection
- Technical assessment: infrastructure, network, location feasibility
- Handle tech assessment passed/rejected outcomes

**Is NOT Responsible For:**
- Processing payments or refunds (→ M4, this module only triggers the signal)
- Authentication (→ M1)
- Collecting personal/business info (→ M3, reads as reference only)
- Presenting policies (→ M6)
- Creating backend accounts (→ M7)
- Making QA decisions — it facilitates human reviewers only

**Must NEVER:**
- Auto-approve or auto-reject — all QA decisions are human
- Let a partner proceed to Phase 3 without QA approval
- Let a partner pass tech assessment without a "passed" outcome
- Process or hold any payment
- Allow re-upload after verification rejection in Phase 1
- Skip bank dedup check on "Add Bank Document" tap
- Skip mandatory bank document upload

**Dependencies:**

| Depends On | Why |
|------------|-----|
| M3 — Registration Service | Partner Name, Business Name for cross-referencing |
| M4 — Fee Collection | Phase 2 starts after Rs.2K confirmed |
| Wiom User Registry (Internal) | Bank account dedup check |
| Wiom Document Storage (Internal) | Store uploaded documents |
| Wiom QA Review Dashboard (Internal) | Send data for review, receive decisions |
| Wiom Technical Assessment Service (Internal) | Request and receive assessment |
| Wiom Notification Service (Internal) | Status updates, decisions, results |
| KYC Verification APIs (3P) — Future | Real PAN/Aadhaar/GST verification (deferred; regex only now) |
| Bank Verification / Penny Drop (3P) — Future | Real bank verification (deferred) |

| Depended On By | Why |
|----------------|-----|
| M4 — Fee Collection | Verification approved + tech passed enables Rs.20K; rejection triggers refund |
| M6 — CSP Policy | Presented only after tech assessment passed |

| Must NOT Depend On |
|---------------------|
| M1, M2, M6, M7, Payment Gateway |

---

### M6 — CSP Policy Service

📄 **Detail doc:** [`M6_CSP_POLICY.md`](M6_CSP_POLICY.md)

**Objective:** Present Wiom's business policies, SLA, commission structure, and payout schedule. Record the partner's explicit acceptance before final payment.

**Explain Like I'm 10:**
You passed all checks. Before you pay the final amount, Wiom says: "Here's how this works" — like a job offer letter. How much you'll earn (Rs.300 per connection, Rs.300 per recharge), when you'll get paid (every Monday), and what you must maintain (fix complaints fast, keep internet running, take care of equipment). You say "samajh gaya" (understood), and that's recorded.

**IS Responsible For:**
- Present commission structure, payout schedule, SLA requirements
- Record partner's explicit acceptance ("Understood, proceed")
- Maintain policy versions and track which version was accepted and when
- Confirm to M4 that policy is accepted (enabling Rs.20K payment)

**Is NOT Responsible For:**
- Enforcing SLA compliance post-onboarding (→ operational systems)
- Processing payouts or commissions (→ Wiom Payout Service)
- Collecting documents or info (→ M5, M3)
- Tech assessment (→ M5)
- Processing Rs.20K payment (→ M4)

**Must NEVER:**
- Let a partner proceed to Rs.20K payment without recorded acceptance
- Modify commission or SLA values — served from configuration
- Pre-accept on behalf of the partner
- Delete past acceptance records — append-only

**Dependencies:**

| Depends On | Why |
|------------|-----|
| M5 — Verification Service | Presented only after verification approved + tech passed |
| Wiom CMS / Policy Config (Internal) | Fetch policy content, rates, terms |
| Wiom Partner Database (Internal) | Store acceptance records |

| Depended On By | Why |
|----------------|-----|
| M4 — Fee Collection | Rs.20K enabled only after policy accepted |

| Must NOT Depend On |
|---------------------|
| M1, M2, M3, M7, Payment Gateway, KYC APIs |

---

### M7 — CSP Account Setup Service

📄 **Detail doc:** [`M7_CSP_ACCOUNT_SETUP.md`](M7_CSP_ACCOUNT_SETUP.md)

**Objective:** Create the partner's operational accounts across all backend systems and confirm onboarding completion.

**Explain Like I'm 10:**
You did everything — verified, documents checked, money paid. Now Wiom sets up your "shop" in their system. Like a school creating your student account after admission — login, class, report card, library card. This module creates your payment account, registers you in the CRM, sets up your earnings tracker, and shows a big "Congratulations!" screen with instructions to download the Wiom Partner Plus app.

**IS Responsible For:**
- Create payout account (for commissions)
- Register partner in CRM
- Set up financial ledger
- Auto-progress loading (3 seconds)
- Handle setup failure and pending states
- Show "Successfully Onboarded" screen + download link + instructions
- Mark onboarding journey as complete

**Is NOT Responsible For:**
- Collecting any partner data (reads from M3, M5)
- Processing payments (→ M4)
- Document verification (→ M5)
- T&C or Policy presentation (→ M2, M6)
- The Wiom Partner Plus App itself (links to it, doesn't build it)

**Must NEVER:**
- Begin without confirmed Rs.20K from M4
- Modify data collected by M1, M2, M3, M5
- Skip any backend registration (payout + CRM + ledger must all succeed)
- Show "Successfully Onboarded" if any setup step failed
- Allow backward edits after setup begins

**Dependencies:**

| Depends On | Why |
|------------|-----|
| M4 — Fee Collection | Starts after Rs.20K confirmed |
| M3 — Registration Service | Reads full partner profile |
| M5 — Verification Service | Reads verified bank details for payout setup |
| RazorpayX (3P) | Create partner payout/fund account |
| Zoho CRM (3P) | Register partner in CRM |
| Wiom Ledger / Finance Service (Internal) | Create financial ledger |
| Wiom Notification Service (Internal) | Onboarding success notification |

| Depended On By | Why |
|----------------|-----|
| None | Terminal module — end of the journey |

| Must NOT Depend On |
|---------------------|
| M1, M2, M6, KYC APIs |

---

## 5. Inter-Module Dependency Map

```
┌──────────────────────────────────────────────────────────────┐
│                  ONBOARDING FLOW DIRECTION →                  │
│                                                              │
│  ┌──────┐   ┌──────┐   ┌──────┐   ┌──────┐                  │
│  │  M1  │──▶│  M2  │──▶│  M3  │──▶│  M4  │ (Rs.2K)          │
│  │ Auth │   │ TnC  │   │ Reg  │   │ Fee  │                  │
│  └──────┘   └──────┘   └──────┘   └──┬───┘                  │
│                                      │                       │
│                                      ▼                       │
│                                 ┌──────┐                     │
│                                 │  M5  │                     │
│                                 │V & A │                     │
│                                 └──┬───┘                     │
│                                    │                         │
│                          ┌─────────┴────────┐                │
│                          ▼                  ▼                │
│                     [Approved]         [Rejected]            │
│                          │             → Refund (M4)         │
│                          ▼               END                 │
│                    [Tech Passed]                             │
│                          │                                   │
│                          ▼                                   │
│                     ┌──────┐   ┌──────┐   ┌──────┐           │
│                     │  M6  │──▶│  M4  │──▶│  M7  │           │
│                     │Policy│   │(Rs20K)│   │Setup │           │
│                     └──────┘   └──────┘   └──────┘           │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

**Key points:**
- **M4 is invoked twice** — Rs.2K (Phase 1) and Rs.20K (Phase 3)
- **M5 has two branch points** — QA review + technical assessment
- **M5 → M4 reverse flow** — rejection triggers refund
- **M7 is terminal** — nothing depends on it

---

## 6. Complete Connection Matrix

### Module ↔ Module

| From → To | Purpose |
|-----------|---------|
| M1 → M2 | Auth completes before T&C |
| M1 → M3 | Verified phone passed as identity anchor |
| M2 → M3 | T&C accepted before registration |
| M3 → M4 | Profile data enables Rs.2K payment |
| M4 → M3 | Rs.2K triggers trade name lock |
| M4 → M5 | Rs.2K confirmed unlocks Phase 2 |
| M5 → M4 | Rejection triggers refund |
| M5 → M6 | Approved + tech passed unlocks policy |
| M6 → M4 | Policy accepted enables Rs.20K |
| M4 → M7 | Rs.20K confirmed unlocks account setup |
| M3 → M7 | Profile read for backend accounts |
| M5 → M7 | Bank details read for payout setup |

### Module → Internal Services

| Module | Internal Service | Purpose |
|--------|-----------------|---------|
| M1 | User Registry | Phone duplicate check |
| M1 | Session Management | Authenticated session |
| M2 | CMS / Legal Content | T&C versions and content |
| M2 | Partner Database | Acceptance records |
| M3 | Geo/Location Service | GPS, location validation |
| M3 | Partner Database | Partner profile |
| M4 | Ledger / Finance Service | Payment/refund records |
| M4 | Notification Service | Confirmations, reminders |
| M5 | User Registry | Bank dedup check |
| M5 | Document Storage | Uploaded documents |
| M5 | QA Review Dashboard | Review data, receive decisions |
| M5 | Technical Assessment Service | Assessment requests/results |
| M5 | Notification Service | Status and decision updates |
| M6 | CMS / Policy Config | Policy content, rates, terms |
| M6 | Partner Database | Policy acceptance records |
| M7 | Ledger / Finance Service | Partner financial ledger |
| M7 | Notification Service | Onboarding success notification |

### Module → Third-Party Vendors

| Module | Vendor | Purpose | Status |
|--------|--------|---------|--------|
| M1 | SMS/WhatsApp OTP Gateway | Send OTP | Production required |
| M4 | Razorpay | Payments and refunds | Production required |
| M5 | KYC Verification API | PAN/Aadhaar/GST verification | Deferred (regex only now) |
| M5 | Bank Verification / Penny Drop | Bank account verification | Deferred (simulated now) |
| M7 | RazorpayX | Partner payout account | Production required |
| M7 | Zoho CRM | Partner CRM registration | Production required |

---

## 7. Onboarding Flow Sequence

```
PARTNER OPENS APP
       │
       ▼
  ┌─────────┐
  │   M1    │  Verify phone + OTP
  └────┬────┘
       ▼
  ┌─────────┐
  │   M2    │  Accept T&C
  └────┬────┘
       ▼
  ┌─────────┐
  │   M3    │  Personal + Business + Location
  └────┬────┘
       ▼
  ┌─────────┐
  │   M4    │  Pay Rs.2,000 → Lock trade name
  └────┬────┘
       ▼
  ┌─────────┐
  │   M5    │  KYC → Bank → ISP → Photos → QA Review
  │         │     ├── REJECTED → Refund (M4) → END
  │         │     └── APPROVED → Tech Assessment
  │         │           ├── REJECTED → No refund → END
  │         │           └── PASSED ↓
  └────┬────┘
       ▼
  ┌─────────┐
  │   M6    │  Policy & SLA → Accept
  └────┬────┘
       ▼
  ┌─────────┐
  │   M4    │  Pay Rs.20,000
  └────┬────┘
       ▼
  ┌─────────┐
  │   M7    │  Account Setup → Successfully Onboarded
  └─────────┘
       │
   ONBOARDING COMPLETE
```

---

## 8. Shared Concerns (Applicable to All Modules)

These concerns are cross-cutting — they apply to **every module**. Implementation may be centralised, but every module must account for them.

| # | Concern | Rule |
|---|---------|------|
| 1 | **Language Toggle** | All text defaults to Hindi with runtime toggle to English. Preference persists across screens. |
| 2 | **Network/Server Errors** | "No Internet" and "Server Error" overlays can appear on any screen. Blocking until resolved. No data loss. |
| 3 | **"Talk to Us" Support** | Phone: 7836811111. Tappable. Appears on payment failures, rejections, setup errors. Production: from config, not hardcoded. |
| 4 | **State Persistence** | All entered data survives back/forward navigation. Production: Room + DataStore, not in-memory. |
| 5 | **No-Blame Errors** | Never blame the user. Reassure ("Don't worry"). Suggest next action, don't describe failure technically. |
| 6 | **Trust Indicators** | Lock icons, verification badges, "Refundable" tags where relevant. |
| 7 | **Design System** | Primary `#D9008D`, radii 8/12/16/888dp, full token set in PRD §12. |
| 8 | **Audit Trail** | Every module logs: partner ID, module ID, event type, timestamp. Append-only. No deletion. |
| 9 | **Session Management** | All modules operate within M1's authenticated session. Expired session → re-auth via M1, no data loss. |
| 10 | **Data Privacy** | PII encrypted at rest and in transit. Access is role-based and logged. No sharing outside declared dependencies. |

---

## 9. Glossary

| Term | Definition |
|------|-----------|
| **CSP** | Channel Sales Partner — local entrepreneur who partners with Wiom to sell internet |
| **KYC** | Know Your Customer — identity verification via PAN, Aadhaar, GST |
| **Dedup** | Deduplication — checking if a value (phone, bank account) is already registered |
| **OTP** | One-Time Password — temporary code to verify phone ownership |
| **PAN** | Permanent Account Number — tax ID (10 characters) |
| **Aadhaar** | Indian unique identity number (12 digits) |
| **GST** | Goods and Services Tax ID (15 characters) |
| **IFSC** | Indian Financial System Code — bank branch identifier (11 characters) |
| **ISP** | Internet Service Provider — company whose internet the CSP resells |
| **SLA** | Service Level Agreement — performance standards the CSP must maintain |
| **TAT** | Turn Around Time — expected duration for a process |
| **PII** | Personally Identifiable Information |

---

**Module Documents:**
- [`M1_AUTH.md`](M1_AUTH.md) — Authentication Service
- [`M2_TNC.md`](M2_TNC.md) — Terms & Conditions Service
- [`M3_REGISTRATION.md`](M3_REGISTRATION.md) — Registration Service
- [`M4_FEE.md`](M4_FEE.md) — Fee Collection Service
- [`M5_VERIFICATION_ASSESSMENT.md`](M5_VERIFICATION_ASSESSMENT.md) — Verification & Assessment Service
- [`M6_CSP_POLICY.md`](M6_CSP_POLICY.md) — CSP Policy Service
- [`M7_CSP_ACCOUNT_SETUP.md`](M7_CSP_ACCOUNT_SETUP.md) — CSP Account Setup Service
