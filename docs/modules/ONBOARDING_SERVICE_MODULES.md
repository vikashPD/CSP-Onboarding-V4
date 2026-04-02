# Wiom CSP Onboarding Service — Module Architecture Document

**Version:** 1.2 | **Date:** 02 April 2026 | **Status:** Production Specification
**Parent PRD:** [PRD_HUMAN.md v3.2](../PRD_HUMAN.md)
**Audience:** Product, Engineering, QA, Business Stakeholders, External Vendors

> **Note:** This document describes **what each module does in principle** — its role, boundaries, and connections. Specific values (fee amounts, document types, field lists, screen counts) live in the PRD and may change across versions. The module architecture remains stable.

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

The onboarding service takes a new Connection Service Provider from "I'm interested" to "I'm onboarded and ready to serve customers" — organised into **3 phases** powered by **7 modules**.

| Phase | Name | What Happens | Modules |
|-------|------|--------------|---------|
| **Phase 1** | Registration | Sign up, provide details, pay registration fee | M1, M2, M3, M4 |
| **Phase 2** | Verification | Submit documents, QA review | M5 |
| **Phase 3** | Activation | Tech assessment, policy acceptance, pay onboarding fee, account setup | M4, M5, M6, M7 |

---

## 3. Module Registry

| ID | Formal Name | Phase(s) | One-Line Purpose |
|----|-------------|----------|------------------|
| **M1** | Authentication | Phase 1 | Verify the partner owns a unique identity and establish an authenticated session |
| **M2** | Terms & Conditions | Phase 1 | Manage T&C versions, present terms, record acceptance — serves both onboarding and Partner BAU app |
| **M3** | Registration | Phase 1 | Collect personal, business, and location details |
| **M4** | Fee Collection | Phase 1, 3 | Configure fee types, values, and checkpoints; connect to PG for execution; ensure financial reconciliation |
| **M5** | Verification & Assessment | Phase 2, 3 | Verify personal, business, financial, and operational credentials; facilitate QA review and tech assessment |
| **M6** | CSP Policy | Phase 3 | Present policies, SLA, commissions; record acceptance |
| **M7** | CSP Account Setup | Phase 3 | Create backend accounts, confirm onboarding |

---

## 4. Module Details

> Each module below is summarised at a glance. Full specs live in the respective module document.

---

### M1 — Authentication

📄 **Detail doc:** [`M1_AUTH.md`](M1_AUTH.md)

**Objective:** Verify that the partner owns a unique identity and establish an authenticated session before anything else happens.

**Explain Like I'm 10:**
Before joining the Wiom shopkeeper club, you prove who you are. The app checks your identity using a verification method — if it matches, you're in. If someone else already joined with that identity, the app says "already taken" — because one person, one identity, one partnership. That's all this module does: check who you are, verify it's really you, and open the door.

**IS Responsible For:**
- Capture and validate the partner's identity credential
- Check if the identity is already registered (duplicate check)
- Perform identity verification (e.g., OTP, email link, or other configured method)
- Manage verification lifecycle (expiry, resend, attempt limits)
- Establish an authenticated session
- Pass the verified identity to downstream modules

**Is NOT Responsible For:**
- Collecting personal or business details (→ M3)
- T&C presentation or acceptance (→ M2)
- Payments (→ M4)
- Document or KYC handling (→ M5)

**Must NEVER:**
- Store verification secrets in plain text or client-side
- Allow unlimited wrong verification attempts
- Let anyone bypass identity verification to proceed
- Let a duplicate identity proceed

**Dependencies:**

| Depends On | Why |
|------------|-----|
| Identity Verification Provider (3P) | Send and verify identity credentials |
| Wiom User Registry (Internal) | Duplicate identity check |
| Session Management (Internal) | Create authenticated session |

| Depended On By | Why |
|----------------|-----|
| M2 — Terms & Conditions | Needs verified identity before showing T&C |
| M3 — Registration | Receives verified identity as anchor |

| Must NOT Depend On |
|---------------------|
| M3, M4, M5, M6, M7, Payment Gateway, KYC APIs |

---

### M2 — Terms & Conditions

📄 **Detail doc:** [`M2_TNC.md`](M2_TNC.md)

**Objective:** Manage the complete T&C lifecycle — versions, presentation, acceptance tracking, and future addendums — across both the onboarding flow and the Wiom Partner BAU app.

**Explain Like I'm 10:**
Before playing any game, you read the rules and say "I agree." This module is the rulebook. Rules can change over time, so it tracks every version, shows the latest, and records exactly when you agreed. After onboarding, the Partner app can show you which rules you agreed to. And if Wiom adds new rules or changes later, the Partner app is where you'll see and accept those updates. This module handles it all: first-time agreement during onboarding, showing what was accepted, and any future addendums in the BAU app.

**IS Responsible For:**
- Maintain a T&C version registry (version, content/link, created timestamp)
- Serve the current T&C version for display
- Record acceptance per partner (version accepted, timestamp)
- Store full acceptance history (append-only)
- Provide a check for other modules/apps: "Has this partner accepted the latest T&C?"
- Block forward progress during onboarding until accepted
- Serve accepted T&C to the Wiom Partner BAU app for viewing
- Handle any future T&C updates or addendums via the BAU app

**Is NOT Responsible For:**
- Drafting or authoring T&C content (→ Legal/Compliance)
- Authentication (→ M1)
- Collecting personal details (→ M3)
- Enforcing T&C terms post-onboarding (→ operational systems)

**Must NEVER:**
- Let a partner proceed to M3 without recorded acceptance
- Modify or interpret T&C content — store and serve only
- Delete past acceptance records — append-only audit trail
- Pre-check the acceptance on behalf of the partner

**Dependencies:**

| Depends On | Why |
|------------|-----|
| M1 — Authentication | Partner must be verified first (onboarding context) |
| Wiom CMS / Legal Content Service (Internal) | Fetch T&C versions and content |
| Wiom Partner Database (Internal) | Store acceptance records |

| Depended On By | Why |
|----------------|-----|
| M3 — Registration | Cannot begin until T&C accepted |
| Wiom Partner BAU App | Serves accepted T&C for viewing; handles future T&C updates and addendums |

| Must NOT Depend On |
|---------------------|
| M3, M4, M5, M6, M7, Payment Gateway, KYC APIs |

---

### M3 — Registration

📄 **Detail doc:** [`M3_REGISTRATION.md`](M3_REGISTRATION.md)

**Objective:** Build the partner's profile — personal identity, business details, and physical location. This is the foundation every downstream module references.

**Explain Like I'm 10:**
Now you tell Wiom who you are: "My name is Rajesh, my shop is Rajesh Telecom, I'm in Indore, here's my address." Wiom needs this to verify your identity, check your location feasibility, and match your name against your ID documents later. It's like filling a membership form — name, address, shop type. Once you submit this and it gets verified, you can't change it until you're fully onboarded. After that, any changes go through a proper approval process.

**IS Responsible For:**
- Collect personal identity details (name, contact information)
- Collect business details (entity type, business/trade name)
- Collect business location (address, geo-coordinates)
- Validate all fields as per configured rules
- Receive verified identity from M1
- Provide profile data as read-only reference to downstream modules
- Lock business/trade name after registration fee is paid (triggered by M4)
- Freeze all submitted details once verified — no edits allowed until onboarding is complete

**Is NOT Responsible For:**
- Verifying identity against government databases (→ M5)
- Payments (→ M4)
- Identity verification or authentication (→ M1)
- T&C (→ M2)
- Document collection or KYC (→ M5)
- Post-onboarding detail modifications (→ compliance team's post-live modification process)

**Must NEVER:**
- Allow progress to M4 with any required field empty
- Modify the verified identity from M1
- Perform KYC verification or collect documents
- Allow business/trade name edits after registration fee is paid
- Allow any detail edits once submitted and verified — details are immutable until onboarding completes

**Dependencies:**

| Depends On | Why |
|------------|-----|
| M1 — Authentication | Verified identity as anchor |
| M2 — Terms & Conditions | T&C must be accepted first |
| Wiom Geo/Location Service (Internal) | GPS capture, location validation |
| Wiom Partner Database (Internal) | Store partner profile |

| Depended On By | Why |
|----------------|-----|
| M4 — Fee Collection | Needs profile data; triggers trade name lock |
| M5 — Verification & Assessment | References profile for identity and business cross-verification |
| M7 — Account Setup | Receives all CSP details for account setup |

| Must NOT Depend On |
|---------------------|
| M4, M5, M6, M7, Payment Gateway, KYC APIs |

---

### M4 — Fee Collection

📄 **Detail doc:** [`M4_FEE.md`](M4_FEE.md)

**Objective:** Configure fee types, fee values, and payment checkpoints for the onboarding journey. Connect to payment layers for transaction execution and ensure complete financial reconciliation.

**Explain Like I'm 10:**
Joining the Wiom club costs money — paid in stages. This module is the price list and the accountant's ledger — it decides what fees exist, how much each one costs, and at which step in the journey they must be paid. When it's time to actually collect the money, it hands the job to a payment system (like Razorpay). When a refund needs to happen, the payment system handles that too. This module's job is to make sure every rupee is tracked — what was charged, what was paid, what was refunded — so the books always balance.

**IS Responsible For:**
- Configure fee types (registration, onboarding, etc.) and their values
- Define which fee applies at which checkpoint in the journey
- Define refundability rules per fee type
- Present fee details and investment summary to the partner
- Connect to payment gateway layer for transaction execution
- Track payment status (success, failure, timeout, pending) received from PG
- Track refund status received from PG
- Maintain complete financial reconciliation for every partner
- Trigger post-payment actions in other modules (e.g., trade name lock after registration fee)
- Manage payment reminder nudges for pending payments

**Is NOT Responsible For:**
- Processing payments — that's the PG layer
- Processing refunds — that's the PG layer
- Collecting partner information (→ M1, M2, M3)
- Document verification (→ M5)
- Deciding when a refund should happen (→ triggered by M5)

**Must NEVER:**
- Process payments or refunds directly — must delegate to PG layer
- Store PCI-sensitive data (card numbers, CVV) on client
- Allow fee configuration changes to take effect mid-journey for an active partner
- Initiate payment without prerequisite module completions
- Show a fee as "paid" without confirmed status from PG

**Dependencies:**

| Depends On | Why |
|------------|-----|
| M3 — Registration | Partner profile for payment context |
| M5 — Verification & Assessment | For onboarding fee: verification + assessment must pass. For refunds: rejection trigger |
| M6 — CSP Policy | Policy accepted before onboarding fee |
| Payment Gateway (3P) | Executes all payment and refund transactions |
| Wiom Ledger / Finance Service (Internal) | Financial reconciliation records |
| Wiom Notification Service (Internal) | Confirmations, reminders, status updates |

| Depended On By | Why |
|----------------|-----|
| M5 — Verification & Assessment | Verification phase starts after registration fee confirmed |
| M7 — Account Setup | Starts after onboarding fee confirmed |
| M3 — Registration | Trade name lock after registration fee |

| Must NOT Depend On |
|---------------------|
| M1, M2, KYC APIs |

---

### M5 — Verification & Assessment

📄 **Detail doc:** [`M5_VERIFICATION_ASSESSMENT.md`](M5_VERIFICATION_ASSESSMENT.md)

**Objective:** Verify the partner's personal identity, business legitimacy, financial credentials, and operational readiness against compliance, quality, and business standards. This is the gatekeeper — it determines whether a partner qualifies.

**Explain Like I'm 10:**
You've told Wiom your name and paid your deposit. Now they check if you're the real deal — like a school verifying your admission documents. Are you who you say you are? Is your business real? Is your bank account valid? Do you have the right agreements in place? Is your shop actually set up to serve customers? A review team checks all of this. If approved, Wiom sends sample devices to your location and assesses your infrastructure performance from the telemetry data those devices send back. Only if BOTH pass do you move forward. Verification fails → deposit back. Technical fails → no refund.

**IS Responsible For:**
- Verify partner's personal identity against configured compliance checks (KYC)
- Verify business legitimacy through required business documents and agreements
- Verify financial credentials (bank details, dedup checks, bank proof)
- Verify operational readiness (shop setup, equipment, infrastructure)
- Run configured validations, cross-checks, and dedup checks on all submitted data
- Accept inputs via configured methods (document uploads, API verification, or both)
- Provide reference/sample guidance for all verification stages
- Display verification progress and submission status
- Facilitate QA review of the partner's complete application — via API-based verification from 3P suites, human review via Wiom Hub Dashboard, or both
- Receive and handle approved/rejected decisions from QA (automated, human, or combined)
- Signal M4 on verification rejection (M4 coordinates refund via PG)
- Facilitate technical assessment via sample device telemetry to evaluate partner's infrastructure performance
- Handle assessment passed/rejected outcomes

**Is NOT Responsible For:**
- Processing payments or refunds (→ M4; this module only sends the signal)
- Authentication (→ M1)
- Collecting personal/business profile info (→ M3; reads as reference only)
- Presenting policies or terms (→ M6)
- Creating backend accounts (→ M7)
- Making QA decisions on its own — decisions come from configured verification sources (3P APIs, human reviewers via Wiom Hub Dashboard, or both)

**Must NEVER:**
- Approve or reject on its own — decisions must come from configured verification sources (3P APIs, human reviewers, or both)
- Let a partner proceed to activation without QA approval
- Let a partner pass technical assessment without a confirmed "passed" outcome
- Process or hold any payment
- Modify partner profile data owned by M3

**Dependencies:**

| Depends On | Why |
|------------|-----|
| M3 — Registration | Partner profile for identity and business cross-verification |
| M4 — Fee Collection | Verification phase starts after registration fee confirmed |
| Wiom User Registry (Internal) | Dedup checks |
| Wiom Document Storage (Internal) | Store uploaded documents |
| Wiom Hub Dashboard (Internal) | Send data for human review when configured, receive decisions |
| 3P Verification Suites (3P) | API-based automated verification when configured |
| Wiom Technical Assessment Service (Internal) | Request and receive assessment |
| Wiom Notification Service (Internal) | Status updates, decisions, results |
| KYC Verification APIs (3P) — Future | Real identity document verification |
| Bank Verification API (3P) — Future | Real bank account verification |

| Depended On By | Why |
|----------------|-----|
| M4 — Fee Collection | Verification + assessment outcomes gate the onboarding fee; rejection triggers refund |
| M6 — CSP Policy | Presented only after assessment passed |

| Must NOT Depend On |
|---------------------|
| M1, M2, M6, M7, Payment Gateway |

---

### M6 — CSP Policy

📄 **Detail doc:** [`M6_CSP_POLICY.md`](M6_CSP_POLICY.md)

**Objective:** Present Wiom's business policies, SLA terms, commission structure, and payout schedule. Record the partner's explicit acceptance before the final fee payment.

**Explain Like I'm 10:**
You passed all checks. Before you pay the final amount, Wiom says: "Here's how this partnership works" — like a job offer letter. How much you'll earn, when you'll get paid, and what you must maintain. You say "samajh gaya" (understood), and that agreement is recorded forever.

**IS Responsible For:**
- Present configured commission structure and payout schedule
- Present service level requirements the partner must maintain
- Record partner's explicit acceptance
- Maintain policy versions and track which version was accepted and when
- Confirm to M4 that policy is accepted (enabling onboarding fee payment)

**Is NOT Responsible For:**
- Enforcing SLA compliance post-onboarding (→ operational systems)
- Processing payouts or commissions (→ Wiom Payout Service)
- Collecting documents or personal info (→ M5, M3)
- Technical assessment (→ M5)
- Processing payments (→ M4)

**Must NEVER:**
- Let a partner proceed to onboarding fee without recorded acceptance
- Modify policy values — these are served from configuration
- Pre-accept on behalf of the partner
- Delete past acceptance records — append-only

**Dependencies:**

| Depends On | Why |
|------------|-----|
| M5 — Verification & Assessment | Presented only after verification approved + tech assessment passed |
| Wiom CMS / Policy Config (Internal) | Fetch policy content, rates, terms |
| Wiom Partner Database (Internal) | Store acceptance records |

| Depended On By | Why |
|----------------|-----|
| M4 — Fee Collection | Onboarding fee enabled only after policy accepted |

| Must NOT Depend On |
|---------------------|
| M1, M2, M3, M7, Payment Gateway, KYC APIs |

---

### M7 — CSP Account Setup

📄 **Detail doc:** [`M7_CSP_ACCOUNT_SETUP.md`](M7_CSP_ACCOUNT_SETUP.md)

**Objective:** Create the partner's operational accounts across all required backend systems and confirm onboarding completion.

**Explain Like I'm 10:**
You did everything — verified, checked, paid. Now Wiom sets up your "shop" in their system. Like a school creating your student account after admission — login, report card, library card. This module creates your payment account, registers you in the support system, sets up your earnings tracker, and shows a big "Congratulations!" with instructions on what to do next.

**IS Responsible For:**
- Create partner's payout/fund account for receiving commissions
- Register partner in CRM/support system
- Set up partner's financial ledger
- Handle setup progress, failure, and pending states
- Present onboarding success confirmation with next steps
- Mark the onboarding journey as complete

**Is NOT Responsible For:**
- Collecting any partner data (receives all CSP details from upstream modules)
- Processing payments (→ M4)
- Document verification (→ M5)
- T&C or policy presentation (→ M2, M6)
- The partner-facing operational app itself (links to it, doesn't own it)

**Must NEVER:**
- Begin without confirmed onboarding fee from M4
- Modify data collected by other modules
- Skip any required backend registration — all must succeed
- Show success if any setup step has failed
- Allow backward edits after setup begins

**Dependencies:**

| Depends On | Why |
|------------|-----|
| All upstream modules (M1–M6) | Receives all CSP details — identity, profile, verification, financial, policy acceptance — for account setup |
| M4 — Fee Collection | Starts after onboarding fee confirmed |
| Payout Infrastructure (3P) | Create partner fund account |
| CRM System (3P) | Register partner |
| Wiom Ledger / Finance Service (Internal) | Create financial ledger |
| Wiom Notification Service (Internal) | Onboarding success notification |

| Depended On By | Why |
|----------------|-----|
| None | Terminal module — end of the journey |

| Must NOT Depend On |
|---------------------|
| KYC APIs, Payment Gateway |

---

## 5. Inter-Module Dependency Map

```
┌──────────────────────────────────────────────────────────────────┐
│                  ONBOARDING FLOW DIRECTION →                      │
│                                                                  │
│  ┌──────┐   ┌──────┐   ┌──────┐   ┌──────────┐                  │
│  │  M1  │──▶│  M2  │──▶│  M3  │──▶│    M4    │                  │
│  │ Auth │   │ TnC  │   │ Reg  │   │Fee (Reg) │                  │
│  └──────┘   └──────┘   └──────┘   └────┬─────┘                  │
│                                        │                         │
│                                        ▼                         │
│                                   ┌──────┐                       │
│                                   │  M5  │                       │
│                                   │V & A │                       │
│                                   └──┬───┘                       │
│                                      │                           │
│                            ┌─────────┴────────┐                  │
│                            ▼                  ▼                  │
│                       [Approved]         [Rejected]              │
│                            │             → Refund via PG (M4)           │
│                            ▼               END                   │
│                      [Tech Passed]                               │
│                            │                                     │
│                            ▼                                     │
│  ┌──────┐   ┌──────────┐   ┌──────┐                              │
│  │  M6  │──▶│    M4    │──▶│  M7  │                              │
│  │Policy│   │Fee (Onb) │   │Setup │                              │
│  └──────┘   └──────────┘   └──────┘                              │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```

**Key points:**
- **M4 is invoked twice** — registration fee (Phase 1) and onboarding fee (Phase 3)
- **M5 has two branch points** — QA review + technical assessment
- **M5 → M4 reverse flow** — rejection triggers refund
- **M7 is terminal** — nothing depends on it

---

## 6. Complete Connection Matrix

### Module ↔ Module

| From → To | Purpose |
|-----------|---------|
| M1 → M2 | Auth completes before T&C |
| M1 → M3 | Verified identity passed as anchor |
| M2 → M3 | T&C accepted before registration |
| M3 → M4 | Profile data enables registration fee payment |
| M4 → M3 | Registration fee triggers trade name lock |
| M4 → M5 | Registration fee confirmed unlocks verification phase |
| M5 → M4 | Rejection signals M4 to coordinate refund via PG |
| M5 → M6 | Approved + tech passed unlocks policy |
| M6 → M4 | Policy accepted enables onboarding fee |
| M4 → M7 | Onboarding fee confirmed unlocks account setup |
| M1–M6 → M7 | All CSP details passed for account setup |

### Module → Internal Services

| Module | Internal Service | Purpose |
|--------|-----------------|---------|
| M1 | User Registry | Identity duplicate check |
| M1 | Session Management | Authenticated session |
| M2 | CMS / Legal Content | T&C versions and content |
| M2 | Partner Database | Acceptance records |
| M3 | Geo/Location Service | GPS, location validation |
| M3 | Partner Database | Partner profile |
| M4 | Ledger / Finance Service | Financial reconciliation records |
| M4 | Notification Service | Confirmations, reminders |
| M5 | User Registry | Dedup checks |
| M5 | Document Storage | Uploaded documents |
| M5 | Wiom Hub Dashboard | Human review when configured, receive decisions |
| M5 | Technical Assessment Service | Assessment requests/results |
| M5 | Notification Service | Status and decision updates |
| M6 | CMS / Policy Config | Policy content, rates, terms |
| M6 | Partner Database | Policy acceptance records |
| M7 | Ledger / Finance Service | Partner financial ledger |
| M7 | Notification Service | Onboarding success notification |

### Module → Third-Party Integrations

| Module | Integration | Purpose |
|--------|------------|---------|
| M1 | Identity Verification Provider | Verify partner identity |
| M4 | Payment Gateway | Execute payment and refund transactions |
| M5 | KYC Verification API (Future) | Identity document verification |
| M5 | Bank Verification API (Future) | Bank account verification |
| M7 | Payout Infrastructure | Partner fund account creation |
| M7 | CRM System | Partner registration |

---

## 7. Onboarding Flow Sequence

```
PARTNER OPENS APP
       │
       ▼
  ┌─────────┐
  │   M1    │  Verify identity + Authenticate
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
  │   M4    │  Pay registration fee → Lock trade name
  └────┬────┘
       ▼
  ┌─────────┐
  │   M5    │  Verify identity, business, financials, operations → QA Review
  │         │     ├── REJECTED → Refund via PG (M4) → END
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
  │   M4    │  Pay onboarding fee
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
| 1 | **Language Toggle** | All text defaults to the primary language with runtime toggle to secondary. Preference persists across screens. |
| 2 | **Network/Server Errors** | Connectivity and server error overlays can appear on any screen. Blocking until resolved. No data loss. |
| 3 | **Support Channel** | A "Talk to Us" contact point appears on failures, rejections, and setup errors. Production: served from config, not hardcoded. |
| 4 | **State Persistence** | All entered data survives back/forward navigation. Production: persisted to durable storage, not in-memory. |
| 5 | **No-Blame Errors** | Never blame the user. Reassure. Suggest next action, don't describe failure technically. |
| 6 | **Trust Indicators** | Security icons, verification badges, and refund tags appear where relevant to build confidence. |
| 7 | **Design System** | All modules follow the Wiom design token set (colours, radii, typography) as defined in the PRD. |
| 8 | **Audit Trail** | Every module logs: partner ID, module ID, event type, timestamp. Append-only. No deletion. |
| 9 | **Session Management** | All modules operate within M1's authenticated session. Expired session → re-auth via M1, no data loss. |
| 10 | **Data Privacy** | PII encrypted at rest and in transit. Access is role-based and logged. No sharing outside declared dependencies. |

---

## 9. Glossary

| Term | Definition |
|------|-----------|
| **CSP** | Connection Service Provider — local entrepreneur who partners with Wiom to provide internet services |
| **KYC** | Know Your Customer — identity verification using government-issued documents |
| **Dedup** | Deduplication — checking if a value (phone, bank account, identity number) is already registered |
| **OTP** | One-Time Password — temporary code to verify phone ownership |
| **ISP** | Internet Service Provider — company whose internet the CSP resells |
| **SLA** | Service Level Agreement — performance standards the CSP must maintain |
| **TAT** | Turn Around Time — expected duration for a process |
| **PII** | Personally Identifiable Information |
| **CRM** | Customer Relationship Management system |

---

**Module Documents:**
- [`M1_AUTH.md`](M1_AUTH.md) — Authentication
- [`M2_TNC.md`](M2_TNC.md) — Terms & Conditions
- [`M3_REGISTRATION.md`](M3_REGISTRATION.md) — Registration
- [`M4_FEE.md`](M4_FEE.md) — Fee Collection
- [`M5_VERIFICATION_ASSESSMENT.md`](M5_VERIFICATION_ASSESSMENT.md) — Verification & Assessment
- [`M6_CSP_POLICY.md`](M6_CSP_POLICY.md) — CSP Policy
- [`M7_CSP_ACCOUNT_SETUP.md`](M7_CSP_ACCOUNT_SETUP.md) — CSP Account Setup
