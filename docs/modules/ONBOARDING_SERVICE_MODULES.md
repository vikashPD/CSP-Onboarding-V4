# Wiom CSP Onboarding Service — Module Architecture Document

**Version:** 1.0 | **Date:** 01 April 2026 | **Status:** Production Specification
**Parent PRD:** [PRD_HUMAN.md v3.2](../PRD_HUMAN.md)
**Audience:** Product, Engineering, QA, Business Stakeholders, External Vendors

---

## Table of Contents

1. [Purpose of This Document](#1-purpose-of-this-document)
2. [Onboarding Service Overview](#2-onboarding-service-overview)
3. [Module Registry](#3-module-registry)
4. [Module Details](#4-module-details)
   - [M1 — Authentication Service](#m1--authentication-service)
   - [M2 — Terms & Conditions Service](#m2--terms--conditions-service)
   - [M3 — Registration Service](#m3--registration-service)
   - [M4 — Fee Collection Service](#m4--fee-collection-service)
   - [M5 — Verification & Assessment Service](#m5--verification--assessment-service)
   - [M6 — CSP Policy Service](#m6--csp-policy-service)
   - [M7 — CSP Account Setup Service](#m7--csp-account-setup-service)
5. [Inter-Module Dependency Map](#5-inter-module-dependency-map)
6. [Complete Connection Matrix](#6-complete-connection-matrix)
7. [Onboarding Flow Sequence](#7-onboarding-flow-sequence)
8. [Shared Concerns (Applicable to All Modules)](#8-shared-concerns-applicable-to-all-modules)
9. [Glossary](#9-glossary)

---

## 1. Purpose of This Document

This document breaks down the Wiom CSP Onboarding flow into **7 independent, well-bounded service modules**. Each module has a clear objective, defined responsibilities, explicit boundaries, and declared dependencies.

**This document answers:**
- What are the 7 modules that together complete CSP onboarding?
- What does each module do (and what it must never do)?
- How do modules connect to each other, to internal Wiom services, and to third-party vendors?
- What are the rules every module must follow?

**This document does NOT answer:**
- Screen-by-screen implementation details (covered in individual module documents: `M1_AUTH.md` through `M7_CSP_ACCOUNT_SETUP.md`)
- Detailed validation rules, error scenarios, and test cases (covered in individual module documents)

---

## 2. Onboarding Service Overview

The Wiom CSP Onboarding Service takes a new Channel Sales Partner (CSP) from "I'm interested" to "I'm onboarded and ready to serve customers." It is an Android-native experience across **15 screens + 1 Pitch screen**, organized into **3 phases** and powered by **7 service modules**.

### The Three Phases

| Phase | Name | What Happens | Modules Involved |
|-------|------|--------------|------------------|
| **Phase 1** | Registration | Partner signs up, provides identity and business details, pays registration fee | M1, M2, M3, M4 |
| **Phase 2** | Verification | Partner submits documents, Wiom QA team reviews and approves/rejects | M5 |
| **Phase 3** | Activation | Technical assessment, policy acceptance, final payment, account creation | M4, M5, M6, M7 |

### Key Numbers

| Metric | Value |
|--------|-------|
| Total Screens | 15 + 1 Pitch |
| Service Modules | 7 |
| Error Scenarios | 22 across 8 categories |
| Total Fees Collected | Rs.22,000 (Rs.2,000 + Rs.20,000) |
| Companion Dashboards | 2 (Control + QA Review) |

---

## 3. Module Registry

| Module ID | Formal Name | Short Name | Phase(s) | One-Line Purpose |
|-----------|-------------|------------|----------|------------------|
| **M1** | Authentication Service | Auth | Phase 1 | Verify the partner's mobile number and establish identity via OTP |
| **M2** | Terms & Conditions Service | TnC | Phase 1 | Manage T&C versions, present current terms, and record partner's acceptance |
| **M3** | Registration Service | Registration | Phase 1 | Collect personal details, business information, and business location |
| **M4** | Fee Collection Service | Fee | Phase 1, Phase 3 | Collect registration fee (Rs.2K) and onboarding fee (Rs.20K) via payment gateway |
| **M5** | Verification & Assessment Service | Verification | Phase 2, Phase 3 | Collect documents (KYC, Bank, ISP, Photos), run QA review, and perform technical assessment |
| **M6** | CSP Policy Service | Policy | Phase 3 | Present Wiom's policies, SLA terms, commission structure, and record partner's acceptance |
| **M7** | CSP Account Setup Service | Account Setup | Phase 3 | Create partner's operational accounts across backend systems and confirm onboarding |

---

## 4. Module Details

---

### M1 — Authentication Service

**Objective:**
Verify that the partner is a real person with a real, unique mobile number. This is the front door of the onboarding journey — no one gets in without proving they own a phone number that hasn't already been registered.

**Explain Like I'm 10 (Wiom Context):**
Imagine Wiom is a club for shopkeepers who want to sell internet to their neighbours. Before you can join the club, you have to prove you have a real phone number — because that number becomes your identity in the Wiom world. So the app sends a secret code (OTP) to your phone, and you type it back in. If the code matches, the app knows it's really you. If someone else already joined with that number, the app says "this number is already taken" — because one shopkeeper, one number, one partnership. That's all this module does: check your phone, check the code, and let you in the door.

**This Module IS Responsible For:**
- Capturing the partner's 10-digit mobile number
- Validating phone number format (+91, numeric, exactly 10 digits)
- Checking if the phone number is already registered (duplicate check)
- Sending OTP to the provided number
- Verifying the OTP entered by the partner
- Managing OTP lifecycle (expiry, resend, attempt limits)
- Establishing an authenticated session for subsequent modules
- Passing the verified phone number to M3 (Registration Service) as a confirmed identity anchor

**This Module is NOT Responsible For:**
- Collecting the partner's name, email, or any personal details (that's M3)
- Presenting or recording Terms & Conditions acceptance (that's M2)
- Any payment processing (that's M4)
- Storing or managing partner profile data beyond the phone number
- KYC document collection or verification (that's M5)

**Boundary Prohibitions — This Service Must NEVER:**
- Store OTPs in plain text or in any client-side storage
- Allow more than 3 incorrect OTP attempts without blocking
- Allow bypassing OTP verification to proceed to the next module
- Accept phone numbers shorter or longer than 10 digits
- Allow a duplicate phone number to proceed past this module
- Share the OTP or phone number with any third-party service not listed in its dependency declaration
- Modify any data belonging to other modules

**Dependency Declaration:**

| Direction | Service / Module | Why |
|-----------|-----------------|-----|
| **Depends On** | SMS/WhatsApp OTP Gateway (3P) | To send OTP messages to the partner's phone |
| **Depends On** | Wiom User Registry (Internal) | To check if the phone number is already registered (duplicate check) |
| **Depends On** | Session Management (Internal) | To create an authenticated session after successful OTP verification |
| **Depended On By** | M2 — Terms & Conditions Service | M2 cannot proceed without a verified phone number |
| **Depended On By** | M3 — Registration Service | M3 receives the verified phone number as an identity anchor |
| **NOT Allowed to Depend On** | M3, M4, M5, M6, M7 | Auth must be completely independent of downstream modules |
| **NOT Allowed to Depend On** | Payment Gateway (3P) | Auth has no business with payments |
| **NOT Allowed to Depend On** | KYC Verification APIs (3P) | Auth has no business with KYC |

---

### M2 — Terms & Conditions Service

**Objective:**
Manage the lifecycle of Wiom's Terms & Conditions — maintain versions, present the current version to the partner, record their explicit acceptance with a timestamp, and ensure no partner can proceed without accepting the latest T&C.

**Explain Like I'm 10 (Wiom Context):**
Before you start playing any game, someone tells you the rules: "Don't cheat, play fair, here's what happens if you break something." The Terms & Conditions module is exactly that — it's the rulebook. But here's the thing: rules can change over time. Maybe Wiom adds a new rule next month. This module keeps track of which version of the rulebook exists, shows the partner the latest version, and records exactly when they said "I agree." If the rules change later, Wiom knows which version each partner agreed to. It's like getting every player to sign the rulebook before the game starts, and keeping a copy of every rulebook version that ever existed.

**This Module IS Responsible For:**
- Maintaining a registry of T&C versions (version number, content/link, created timestamp)
- Identifying the latest/current T&C version
- Presenting the T&C to the partner with a clear acceptance mechanism (checkbox)
- Recording the partner's acceptance: which version they accepted and when (timestamp)
- Storing the full acceptance history per partner (if T&C is updated and re-accepted later)
- Providing an API/method for other modules to check: "Has this partner accepted the latest T&C?"
- Blocking forward progress in the onboarding flow if T&C is not accepted
- Serving the T&C document content or link for display

**This Module is NOT Responsible For:**
- Drafting or authoring T&C content (that's Legal/Compliance)
- Authenticating the partner (that's M1)
- Collecting personal details (that's M3)
- Any payment processing (that's M4)
- Enforcing T&C terms during the partnership (that's operational/compliance systems)

**Boundary Prohibitions — This Service Must NEVER:**
- Allow a partner to proceed to M3 (Registration) without a recorded T&C acceptance
- Modify or interpret T&C content — it only stores and serves what Legal provides
- Delete or overwrite a partner's past acceptance records (append-only audit trail)
- Pre-check the T&C checkbox on behalf of the partner — acceptance must be an explicit user action
- Accept a T&C version that has been deprecated or superseded without explicit override
- Share partner acceptance data with third parties not listed in its dependency declaration

**Dependency Declaration:**

| Direction | Service / Module | Why |
|-----------|-----------------|-----|
| **Depends On** | M1 — Authentication Service | Partner must be authenticated (verified phone) before T&C is presented |
| **Depends On** | Wiom CMS / Legal Content Service (Internal) | To fetch the latest T&C version, content, and document link |
| **Depends On** | Wiom Partner Database (Internal) | To store and retrieve T&C acceptance records per partner |
| **Depended On By** | M3 — Registration Service | M3 cannot begin until M2 confirms T&C accepted |
| **NOT Allowed to Depend On** | M3, M4, M5, M6, M7 | T&C service is upstream of all business modules |
| **NOT Allowed to Depend On** | Payment Gateway (3P) | T&C has no business with payments |
| **NOT Allowed to Depend On** | KYC Verification APIs (3P) | T&C has no business with KYC |

---

### M3 — Registration Service

**Objective:**
Collect the partner's personal identity, business details, and physical business location. This module builds the partner's profile — the foundation that every subsequent module references.

**Explain Like I'm 10 (Wiom Context):**
Now that you've proven your phone number (M1) and agreed to the rules (M2), it's time to introduce yourself. This is where the shopkeeper tells Wiom: "My name is Rajesh, my shop is called Rajesh Telecom, I'm in Indore, and here's my shop address." Wiom needs this information because they'll send equipment to your shop, they'll show your shop on a map for customers, and they need to know your real name (as per Aadhaar) to match it against your ID documents later. Think of it like filling out a membership form at a library — name, address, what kind of shop you have. Once this is done, Wiom knows who you are and where you are.

**This Module IS Responsible For:**
- Collecting personal details: Full Name (as per Aadhaar), Email ID
- Collecting business details: Entity Type (Proprietorship), Business Name
- Collecting business location: State, City, Pincode, Full Address
- Capturing GPS coordinates of the business location
- Validating all input fields (format, required fields, completeness)
- Receiving the verified phone number from M1 as a confirmed identity field
- Providing the collected profile data to downstream modules (M4, M5, M6, M7) as read-only reference
- Locking the Business Name (trade name) after registration fee is paid (lock triggered by M4)

**This Module is NOT Responsible For:**
- Verifying the partner's identity against government databases (that's M5)
- Accepting payments of any kind (that's M4)
- Verifying phone number or OTP (that's M1)
- Presenting Terms & Conditions (that's M2)
- Validating KYC documents, bank details, or ISP agreements (that's M5)

**Boundary Prohibitions — This Service Must NEVER:**
- Allow forward progress to M4 (Fee Collection) with any required field empty
- Modify the verified phone number received from M1
- Perform KYC verification or document collection
- Accept entity types other than "Proprietorship" in the current version
- Store GPS coordinates without explicit partner awareness (GPS badge must be visible)
- Allow edits to the Business Name after M4 confirms registration fee is paid
- Share personal data with third parties outside its dependency declaration

**Dependency Declaration:**

| Direction | Service / Module | Why |
|-----------|-----------------|-----|
| **Depends On** | M1 — Authentication Service | Receives verified phone number as identity anchor |
| **Depends On** | M2 — Terms & Conditions Service | Partner must have accepted T&C before registration begins |
| **Depends On** | Wiom Geo/Location Service (Internal) | For GPS coordinate capture and state/city/pincode validation |
| **Depends On** | Wiom Partner Database (Internal) | To store the partner profile |
| **Depended On By** | M4 — Fee Collection Service | M4 needs Business Name to display on payment screens; M4 triggers trade name lock |
| **Depended On By** | M5 — Verification & Assessment Service | M5 references Name, Business Name for document cross-checking |
| **Depended On By** | M7 — CSP Account Setup Service | M7 uses the full profile to create backend accounts |
| **NOT Allowed to Depend On** | M4, M5, M6, M7 | Registration is upstream; it must not depend on downstream modules |
| **NOT Allowed to Depend On** | Payment Gateway (3P) | Registration has no business with payments |
| **NOT Allowed to Depend On** | KYC Verification APIs (3P) | Registration collects info but does not verify identity documents |

---

### M4 — Fee Collection Service

**Objective:**
Collect payments from the partner at two defined points in the journey — the registration fee (Rs.2,000, refundable) and the onboarding fee (Rs.20,000, non-refundable). Handle payment success, failure, timeout, and refund scenarios.

**Explain Like I'm 10 (Wiom Context):**
Joining the Wiom club isn't free — it's a business partnership, and there's an investment involved. Think of it in two parts. First, after you fill your form (M3), you pay Rs.2,000 — this is like a "serious applicant" deposit. If Wiom later says "sorry, we can't accept you," they give this money back. But if you pass all the checks and Wiom says "welcome!", you then pay Rs.20,000 — this is your real investment to become a partner and get WiFi equipment. This module is the cashier — it takes the money, gives you a receipt, and if something goes wrong (like your payment fails), it tells you "don't worry, no money was deducted, try again." It also handles giving money back (refunds) if Wiom rejects your application after the first payment.

**This Module IS Responsible For:**
- Presenting the registration fee payment screen (Rs.2,000) — Phase 1
- Presenting the onboarding fee payment screen (Rs.20,000) — Phase 3
- Displaying the investment summary (Rs.2K paid + Rs.20K due = Rs.22K total)
- Initiating payment transactions via the payment gateway
- Handling payment success, failure, and timeout states
- Processing refunds when triggered by M5 (verification rejected)
- Showing refund status (Success / In Progress / Failed)
- Sending payment confirmation to downstream modules
- Triggering trade name lock in M3 after registration fee is paid
- Managing payment reminder nudges (Day 1-4) if registration fee is pending

**This Module is NOT Responsible For:**
- Deciding whether a refund should happen (that decision comes from M5)
- Collecting any partner information (that's M1, M2, M3)
- Verifying documents or KYC (that's M5)
- Determining payment amounts — amounts are business constants, not computed by this module
- Managing the partner's bank account details for receiving commissions (that's M5 for collection, M7 for setup)

**Boundary Prohibitions — This Service Must NEVER:**
- Initiate a payment without the prerequisite module completions (M1+M2+M3 for Rs.2K; M5+M6 for Rs.20K)
- Store full card numbers, CVV, or any PCI-sensitive payment data on the client
- Modify the payment amounts — Rs.2,000 and Rs.20,000 are business constants
- Process a refund on its own authority — refunds are only triggered by M5's rejection outcome
- Allow the partner to skip a payment and proceed to the next module
- Retry a payment automatically without explicit partner action (tap)
- Share payment data with modules or services outside its dependency declaration

**Dependency Declaration:**

| Direction | Service / Module | Why |
|-----------|-----------------|-----|
| **Depends On** | M3 — Registration Service | Needs partner profile (Business Name) for payment screens |
| **Depends On** | M5 — Verification & Assessment Service | For Rs.20K: M5 must confirm verification approved + tech assessment passed. For refunds: M5 triggers refund on rejection |
| **Depends On** | M6 — CSP Policy Service | Partner must accept Policy & SLA before Rs.20K payment |
| **Depends On** | Payment Gateway — Razorpay (3P) | To process payment transactions and refunds |
| **Depends On** | Wiom Ledger / Finance Service (Internal) | To record payment and refund transactions |
| **Depends On** | Wiom Notification Service (Internal) | To send payment confirmations, reminders, and refund updates |
| **Depended On By** | M5 — Verification & Assessment Service | M5 can only begin (Phase 2) after Rs.2K payment is confirmed |
| **Depended On By** | M7 — CSP Account Setup Service | M7 can only begin after Rs.20K payment is confirmed |
| **Depended On By** | M3 — Registration Service | M4 triggers trade name lock in M3 after Rs.2K payment |
| **NOT Allowed to Depend On** | M1 — Authentication Service | M4 has no direct dependency on Auth; it receives context via M3 |
| **NOT Allowed to Depend On** | M2 — Terms & Conditions Service | M4 has no direct dependency on T&C |
| **NOT Allowed to Depend On** | KYC Verification APIs (3P) | Fee collection has no business with KYC verification |

---

### M5 — Verification & Assessment Service

**Objective:**
Collect all required documents (KYC, bank details, ISP agreement, shop/equipment photos), run dedup and validation checks, facilitate QA review of the application, and conduct technical assessment of the partner's infrastructure. This is the largest module — it is the gatekeeper that decides whether a partner qualifies.

**Explain Like I'm 10 (Wiom Context):**
Okay, so you've told Wiom your name and paid your Rs.2,000 deposit. Now Wiom needs to check if you're the real deal. Imagine you're trying out for a cricket team — the coach asks to see your ID card (that's KYC), your bank passbook (so they can pay you later), your contract with the stadium (that's the ISP agreement — it proves you have permission to sell internet), and photos of your cricket gear and practice ground (that's shop and equipment photos). Once you submit all of this, a review team (QA) checks everything — "Is the ID real? Does the bank account match? Is the ISP agreement valid?" If they say "approved," a technical team then comes to check if your ground is actually good enough to play on — "Do you have power backup? Is your internet equipment working? Is your location feasible?" Only if BOTH the document review AND the technical check pass do you move forward. If the documents fail, you get your Rs.2,000 back. If the technical check fails, you don't get the money back because Wiom already spent time and resources checking your setup.

**This Module IS Responsible For:**
- **KYC Collection:** Collecting PAN, Aadhaar, and GST numbers with regex validation on blur
- **KYC Document Upload:** Capturing document photos/scans for PAN, Aadhaar (front + back), GST
- **KYC Cross-Validation:** Ensuring GST characters 3-12 match the PAN number
- **Bank Details Collection:** Account Number, Re-enter Account Number, IFSC Code
- **Bank Dedup Check:** Checking if the bank account is already registered with another partner
- **Bank Document Upload:** Mandatory upload of Bank Statement / Cancelled Cheque / Bank Passbook
- **ISP Agreement Upload:** Multi-page upload (PDF + up to 7 photos) with mandatory details checklist
- **Shop & Equipment Photos:** Shop front photo (single) + equipment photos (up to 5)
- **Sample Documents:** Providing "View sample document" references for all upload screens
- **Verification Status:** Displaying document submission checklist and review status
- **QA Review Facilitation:** Sending submitted data to the QA Review Dashboard for human review
- **QA Decision Handling:** Receiving approved/rejected decisions from the QA team
- **Rejection & Refund Trigger:** On verification rejection, triggering the auto-refund flow in M4
- **Technical Assessment:** Facilitating infrastructure review, network readiness, and location feasibility assessment
- **Technical Assessment Decision:** Handling passed/rejected outcomes of the technical assessment

**This Module is NOT Responsible For:**
- Processing payments or refunds (that's M4 — this module only triggers the refund signal)
- Authenticating the partner (that's M1)
- Collecting personal/business information (that's M3 — this module reads it as reference)
- Presenting policies or SLA terms (that's M6)
- Creating backend accounts (that's M7)
- Making the QA decision itself — it only facilitates the human reviewers

**Boundary Prohibitions — This Service Must NEVER:**
- Approve or reject an application automatically — all QA decisions must be made by human reviewers
- Allow a partner to proceed to Phase 3 without QA approval
- Allow a partner to proceed past technical assessment without a passed outcome
- Process or hold any payment — it must delegate to M4
- Modify the partner's personal or business profile data (owned by M3)
- Allow re-upload of documents after verification rejection in Phase 1 (partner must start fresh or contact support)
- Store unencrypted KYC document images or numbers at rest
- Skip the bank dedup check when the partner taps "Add Bank Document"
- Allow the partner to skip mandatory bank document upload

**Dependency Declaration:**

| Direction | Service / Module | Why |
|-----------|-----------------|-----|
| **Depends On** | M3 — Registration Service | Reads partner Name, Business Name for cross-referencing with documents |
| **Depends On** | M4 — Fee Collection Service | Phase 2 begins only after M4 confirms Rs.2K payment |
| **Depends On** | Wiom User Registry (Internal) | For bank account dedup check against existing partners |
| **Depends On** | Wiom Document Storage Service (Internal) | To store uploaded documents (KYC photos, bank docs, ISP agreement, shop photos) |
| **Depends On** | Wiom QA Review Dashboard (Internal) | To send application data for human review and receive decisions |
| **Depends On** | Wiom Technical Assessment Service (Internal) | To request and receive infrastructure/network/location feasibility assessment |
| **Depends On** | Wiom Notification Service (Internal) | To notify partner of verification status, QA decisions, and assessment results |
| **Depends On** | KYC Verification APIs (3P) — Future | For real PAN/Aadhaar/GST verification (deferred in current version; regex only for now) |
| **Depends On** | Bank Verification / Penny Drop API (3P) — Future | For real bank account verification (deferred in current version) |
| **Depended On By** | M4 — Fee Collection Service | M4 waits for verification approved + tech passed before enabling Rs.20K payment; M4 processes refund when M5 triggers rejection |
| **Depended On By** | M6 — CSP Policy Service | M6 is only presented after M5 confirms tech assessment passed |
| **NOT Allowed to Depend On** | M1 — Authentication Service | No direct dependency; verified phone comes via M3 |
| **NOT Allowed to Depend On** | M2 — Terms & Conditions Service | No direct dependency |
| **NOT Allowed to Depend On** | M6 — CSP Policy Service | Verification & Assessment happens before Policy |
| **NOT Allowed to Depend On** | M7 — CSP Account Setup Service | Verification must complete before account setup |
| **NOT Allowed to Depend On** | Payment Gateway (3P) | This module does not process payments |

---

### M6 — CSP Policy Service

**Objective:**
Present Wiom's business policies, Service Level Agreement (SLA), commission structure, and payout schedule to the partner, and record their explicit acceptance before they proceed to the final payment.

**Explain Like I'm 10 (Wiom Context):**
You've passed all the checks — your documents are verified, your shop equipment looks good, and Wiom's technical team says your location works. Before you pay the final amount and officially become a partner, Wiom sits you down and says: "Here's how this partnership works." It's like getting a job offer letter — before you sign, they tell you how much you'll earn (Rs.300 per new connection, Rs.300 per recharge), when you'll get paid (every Monday by 10 AM), and what you're expected to maintain (fix complaints in 4 hours, keep your internet running 95% of the time, take care of the equipment, follow Wiom's brand rules). You read it, you say "samajh gaya" (understood), and that agreement is recorded. This way, later if there's a dispute, both sides know exactly what was agreed to.

**This Module IS Responsible For:**
- Presenting the commission structure (Rs.300/new connection, Rs.300/recharge)
- Presenting the payout schedule (every Monday by 10 AM via bank transfer)
- Presenting the Service Level requirements (4-hour complaint resolution, 95%+ uptime, equipment care, brand compliance)
- Recording the partner's explicit acceptance ("Understood, proceed")
- Maintaining policy versions (similar to T&C — policies may be updated over time)
- Storing which policy version the partner accepted and when
- Providing confirmation to M4 that partner has accepted policy (enabling Rs.20K payment)

**This Module is NOT Responsible For:**
- Enforcing SLA compliance after onboarding (that's operational systems)
- Processing payouts or commissions (that's the Wiom Payout Service)
- Collecting any documents or information from the partner
- Conducting the technical assessment (that's M5)
- Processing the Rs.20K payment (that's M4)

**Boundary Prohibitions — This Service Must NEVER:**
- Allow the partner to proceed to M4 (Rs.20K payment) without recorded policy acceptance
- Modify commission amounts or SLA terms — these are business constants served from configuration
- Pre-accept the policy on behalf of the partner — acceptance must be an explicit user action
- Delete or overwrite past policy acceptance records (append-only audit trail)
- Bypass or skip any policy section — the partner must see all terms before accepting

**Dependency Declaration:**

| Direction | Service / Module | Why |
|-----------|-----------------|-----|
| **Depends On** | M5 — Verification & Assessment Service | Policy is only presented after verification approved + technical assessment passed |
| **Depends On** | Wiom CMS / Policy Configuration Service (Internal) | To fetch the latest policy content, commission rates, SLA terms |
| **Depends On** | Wiom Partner Database (Internal) | To store policy acceptance records |
| **Depended On By** | M4 — Fee Collection Service | M4 enables Rs.20K payment only after M6 confirms policy accepted |
| **NOT Allowed to Depend On** | M1, M2, M3 | No direct upstream dependency |
| **NOT Allowed to Depend On** | M7 — CSP Account Setup Service | Policy comes before account setup |
| **NOT Allowed to Depend On** | Payment Gateway (3P) | Policy has no business with payments |
| **NOT Allowed to Depend On** | KYC Verification APIs (3P) | Policy has no business with KYC |

---

### M7 — CSP Account Setup Service

**Objective:**
Create the partner's operational accounts across all backend systems (payment infrastructure, CRM, financial ledger), confirm successful setup, and present the final onboarding success screen with next steps.

**Explain Like I'm 10 (Wiom Context):**
Congratulations — you've done everything! Phone verified, rules agreed, form filled, money paid, documents checked, shop inspected, policies understood, final payment made. Now the last step: Wiom sets up your "shop" in their computer system. It's like a school creating your student account after you're admitted — they give you a login, assign you a class, create your report card file, and set up your library card. This module does the same: it creates your account in the payment system (so you can receive commissions), registers you in the CRM (so the support team knows you), sets up your financial ledger (so your earnings are tracked), and then shows you a big "Congratulations!" screen with instructions to download the Wiom Partner Plus app and start serving customers. This is the finish line.

**This Module IS Responsible For:**
- Creating the partner's payout account (for receiving commissions)
- Registering the partner in the CRM system
- Setting up the partner's financial ledger
- Auto-progressing through the setup process (3-second loading screen)
- Handling setup failure and pending states
- Presenting the "Successfully Onboarded" congratulations screen
- Displaying next steps: Download Wiom Partner Plus App + "Install Now" button
- Displaying important instructions: Login, Allow permissions, Complete Mandatory Training
- Marking the onboarding journey as complete in the partner's record

**This Module is NOT Responsible For:**
- Collecting any partner data (it reads from M3's stored profile)
- Processing payments (that's M4 — setup begins after M4 confirms Rs.20K)
- Verifying documents or identity (that's M5)
- Presenting policies or T&C (that's M2, M6)
- The actual Wiom Partner Plus App (this module links to it, does not build it)
- Ongoing partner support or operations post-onboarding

**Boundary Prohibitions — This Service Must NEVER:**
- Begin account creation without confirmed Rs.20K payment from M4
- Modify any data collected by M1, M2, M3, or M5 — it only reads
- Skip any backend system registration (payout + CRM + ledger must all succeed)
- Show "Successfully Onboarded" if any backend setup step has failed
- Allow the partner to go back and modify earlier data after account setup begins
- Store any payment credentials or sensitive data beyond what the backend systems require

**Dependency Declaration:**

| Direction | Service / Module | Why |
|-----------|-----------------|-----|
| **Depends On** | M4 — Fee Collection Service | Account setup begins only after Rs.20K payment confirmed |
| **Depends On** | M3 — Registration Service | Reads full partner profile (name, business name, location, email, phone) |
| **Depends On** | M5 — Verification & Assessment Service | Reads verified bank details for payout account setup |
| **Depends On** | RazorpayX (3P) | To create the partner's payout/fund account for commissions |
| **Depends On** | Zoho CRM (3P) | To register the partner in the CRM system |
| **Depends On** | Wiom Ledger / Finance Service (Internal) | To create the partner's financial ledger |
| **Depends On** | Wiom Notification Service (Internal) | To send onboarding success notification |
| **Depended On By** | None | M7 is the terminal module — no other module depends on it |
| **NOT Allowed to Depend On** | M1 — Authentication Service | No direct dependency; phone comes via M3 |
| **NOT Allowed to Depend On** | M2 — Terms & Conditions Service | No direct dependency |
| **NOT Allowed to Depend On** | M6 — CSP Policy Service | No direct dependency; policy acceptance is a prerequisite enforced by flow, not a runtime call |
| **NOT Allowed to Depend On** | KYC Verification APIs (3P) | Account setup has no business with KYC verification |

---

## 5. Inter-Module Dependency Map

```
┌─────────────────────────────────────────────────────────────────────┐
│                    ONBOARDING FLOW DIRECTION →                      │
│                                                                     │
│   ┌──────┐    ┌──────┐    ┌──────┐    ┌──────┐                     │
│   │  M1  │───▶│  M2  │───▶│  M3  │───▶│  M4  │ (Rs.2K)            │
│   │ Auth │    │ TnC  │    │ Reg  │    │ Fee  │                     │
│   └──────┘    └──────┘    └──────┘    └──┬───┘                     │
│                                          │                          │
│                                          ▼                          │
│                                     ┌──────┐                        │
│                                     │  M5  │                        │
│                                     │V & A │                        │
│                                     └──┬───┘                        │
│                                        │                            │
│                              ┌─────────┴─────────┐                  │
│                              ▼                   ▼                  │
│                         [Approved]          [Rejected]              │
│                              │              ▼ Refund (M4)           │
│                              ▼                                      │
│                      [Tech Passed]                                  │
│                              │                                      │
│                              ▼                                      │
│                         ┌──────┐    ┌──────┐    ┌──────┐            │
│                         │  M6  │───▶│  M4  │───▶│  M7  │            │
│                         │Policy│    │ Fee  │    │Setup │            │
│                         └──────┘    │(Rs20K)│    └──────┘            │
│                                     └──────┘                        │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

**Key observations:**
- **M4 (Fee) is invoked twice** — once in Phase 1 (Rs.2K) and once in Phase 3 (Rs.20K), with different prerequisites each time
- **M5 (Verification) has two branch points** — QA review (approved/rejected) and technical assessment (passed/rejected)
- **M5 → M4 reverse dependency** — M5 triggers refund in M4 when verification is rejected
- **M7 is terminal** — nothing depends on it; it's the end of the journey

---

## 6. Complete Connection Matrix

This matrix shows every connection each module has — to other modules, to internal Wiom services, and to third-party vendors.

### Module-to-Module Connections

| From → To | Connection Type | Purpose |
|-----------|----------------|---------|
| M1 → M2 | Sequential | Auth must complete before T&C is presented |
| M1 → M3 | Data pass | Verified phone number passed as identity anchor |
| M2 → M3 | Sequential | T&C must be accepted before registration begins |
| M3 → M4 | Sequential + Data | Profile data passed; enables Rs.2K payment screen |
| M4 → M3 | Trigger | Rs.2K payment triggers trade name lock |
| M4 → M5 | Sequential | Rs.2K payment confirmation unlocks Phase 2 |
| M5 → M4 | Trigger | Verification rejection triggers refund in M4 |
| M5 → M6 | Sequential | Verification approved + tech passed unlocks policy screen |
| M6 → M4 | Sequential | Policy accepted enables Rs.20K payment |
| M4 → M7 | Sequential | Rs.20K payment confirmation unlocks account setup |
| M3 → M7 | Data read | M7 reads full partner profile for backend account creation |
| M5 → M7 | Data read | M7 reads verified bank details for payout account setup |

### Module-to-Internal-Service Connections

| Module | Internal Service | Purpose |
|--------|-----------------|---------|
| M1 | Wiom User Registry | Phone duplicate check |
| M1 | Session Management | Create authenticated session |
| M2 | Wiom CMS / Legal Content Service | Fetch T&C versions and content |
| M2 | Wiom Partner Database | Store acceptance records |
| M3 | Wiom Geo/Location Service | GPS capture, state/city/pincode validation |
| M3 | Wiom Partner Database | Store partner profile |
| M4 | Wiom Ledger / Finance Service | Record payment and refund transactions |
| M4 | Wiom Notification Service | Payment confirmations, reminders, refund updates |
| M5 | Wiom User Registry | Bank account dedup check |
| M5 | Wiom Document Storage Service | Store uploaded documents |
| M5 | Wiom QA Review Dashboard | Send data for review, receive decisions |
| M5 | Wiom Technical Assessment Service | Request and receive assessment results |
| M5 | Wiom Notification Service | Status updates, QA decisions, assessment results |
| M6 | Wiom CMS / Policy Configuration Service | Fetch policy content, commission rates, SLA terms |
| M6 | Wiom Partner Database | Store policy acceptance records |
| M7 | Wiom Ledger / Finance Service | Create partner financial ledger |
| M7 | Wiom Notification Service | Onboarding success notification |

### Module-to-Third-Party Connections

| Module | Third-Party Vendor | Purpose | Status |
|--------|-------------------|---------|--------|
| M1 | SMS/WhatsApp OTP Gateway | Send OTP messages | Required for production |
| M4 | Razorpay (Payment Gateway) | Process payments and refunds | Required for production |
| M5 | KYC Verification API Provider | Real PAN/Aadhaar/GST verification | Deferred — regex only in current version |
| M5 | Bank Verification / Penny Drop API | Real bank account verification | Deferred — simulated in current version |
| M7 | RazorpayX | Create partner payout/fund account | Required for production |
| M7 | Zoho CRM | Register partner in CRM | Required for production |

---

## 7. Onboarding Flow Sequence

This shows the complete onboarding journey as a sequence of module invocations with decision points.

```
PARTNER OPENS APP
       │
       ▼
  ┌─────────┐
  │   M1    │  Authentication Service
  │  Auth   │  → Verify phone + OTP
  └────┬────┘
       │ Phone verified
       ▼
  ┌─────────┐
  │   M2    │  Terms & Conditions Service
  │  TnC    │  → Present T&C, record acceptance
  └────┬────┘
       │ T&C accepted
       ▼
  ┌─────────┐
  │   M3    │  Registration Service
  │  Reg    │  → Collect personal, business, location details
  └────┬────┘
       │ Profile complete
       ▼
  ┌─────────┐
  │   M4    │  Fee Collection Service (FIRST INVOCATION)
  │  Fee    │  → Collect Rs.2,000 registration fee
  │ (Rs.2K) │  → Lock trade name in M3
  └────┬────┘
       │ Payment confirmed
       ▼
  ┌─────────────────────────────────────────────────┐
  │                    M5                            │
  │     Verification & Assessment Service            │
  │                                                  │
  │  Step 1: KYC Documents (PAN → Aadhaar → GST)    │
  │  Step 2: Bank Details + Bank Document Upload     │
  │  Step 3: ISP Agreement Upload                    │
  │  Step 4: Shop & Equipment Photos                 │
  │  Step 5: Verification Status                     │
  │           │                                      │
  │     ┌─────┴──────┐                               │
  │     ▼            ▼                               │
  │  APPROVED     REJECTED ──→ Trigger refund (M4)   │
  │     │            │         END OF JOURNEY         │
  │     ▼            └───────────────────────────┐    │
  │  Step 6: Technical Assessment                │    │
  │           │                                  │    │
  │     ┌─────┴──────┐                           │    │
  │     ▼            ▼                           │    │
  │   PASSED      REJECTED ──→ No refund         │    │
  │     │           "Talk to Us"                  │    │
  │     │           END OF JOURNEY                │    │
  └─────┼─────────────────────────────────────────┘    
        │ Verification approved + Tech passed
        ▼
  ┌─────────┐
  │   M6    │  CSP Policy Service
  │ Policy  │  → Present commission, payout, SLA terms
  │         │  → Record acceptance
  └────┬────┘
       │ Policy accepted
       ▼
  ┌─────────┐
  │   M4    │  Fee Collection Service (SECOND INVOCATION)
  │  Fee    │  → Collect Rs.20,000 onboarding fee
  │(Rs.20K) │  → Show investment summary
  └────┬────┘
       │ Payment confirmed
       ▼
  ┌─────────┐
  │   M7    │  CSP Account Setup Service
  │ Setup   │  → Create payout account (RazorpayX)
  │         │  → Register in CRM (Zoho)
  │         │  → Create financial ledger
  │         │  → Show "Successfully Onboarded"
  │         │  → Download Wiom Partner Plus App
  └─────────┘
       │
   ONBOARDING COMPLETE
```

---

## 8. Shared Concerns (Applicable to All Modules)

The following concerns are cross-cutting — they apply to every module in the onboarding service. Individual module documents must account for these, but the implementation of these concerns may be centralized in shared infrastructure.

### 8.1 Language Toggle (Hindi / English)

- All user-facing text in every module defaults to **Hindi** with a runtime toggle to **English**
- Language preference persists across screens and modules
- All labels, CTAs, error messages, and informational text must have both Hindi and English versions
- Language toggle must be accessible from every screen

### 8.2 Error Overlays (Network / Server)

- **No Internet** and **Server Error** overlays can appear on any screen in any module
- These are blocking overlays — the partner cannot proceed until connectivity is restored or the server recovers
- Every module must gracefully handle these overlays without losing entered data

### 8.3 "Talk to Us" Support Channel

- Phone number: **7836811111**
- Appears across multiple modules (M4 payment failures, M5 tech rejection, M7 setup failures)
- Must always be tappable (initiates phone call)
- In production: this number should be fetched from configuration, not hardcoded

### 8.4 State Persistence & Navigation

- All data entered by the partner must persist when navigating back and forward across screens and modules
- No module should lose data on back-navigation
- Each module must correctly restore its state when the partner returns to it
- In production: state must be persisted to durable storage (Room / DataStore), not just in-memory

### 8.5 No-Blame Error Philosophy

- Error messages across all modules must follow Wiom's UX principle: **never blame the user**
- Always reassure ("Don't worry," "No money was deducted," "This can happen sometimes")
- Errors should suggest the next action, not describe the failure technically

### 8.6 Trust & Security Indicators

- Lock icons, verification badges, and "Refundable" tags must appear where relevant across modules
- These build partner confidence during payment (M4), document submission (M5), and data entry (M3)

### 8.7 Design System Compliance

All modules must adhere to the Wiom design system:

| Token | Hex | Usage |
|-------|-----|-------|
| Primary | `#D9008D` | CTAs, brand accent |
| Primary Light | `#FFE5F6` | Backgrounds |
| Text | `#161021` | Body text |
| Text Secondary | `#665E75` | Labels |
| Hint | `#A7A1B2` | Placeholders |
| Surface | `#FAF9FC` | Screen backgrounds |
| Positive | `#008043` | Success states |
| Negative | `#D92130` | Error states |
| Warning | `#FF8000` | Pending states |
| Info | `#6D17CE` | Info boxes |
| Header | `#443152` | Status bar, app header |

Corner radii: Small (8dp), Medium (12dp), Large (16dp), Pill (888dp)

### 8.8 Audit Trail

- Every module must log significant events with timestamps: user actions, state transitions, decisions, and errors
- Logs must include: partner ID, module ID, event type, timestamp, and relevant data
- Audit trails are append-only — no deletion or modification of past records
- This supports compliance, debugging, and dispute resolution

### 8.9 Session & Timeout Management

- All modules operate within the authenticated session established by M1
- If the session expires mid-flow, the partner must re-authenticate via M1 without losing progress in other modules
- Inactive session timeout policy must be configurable

### 8.10 Data Privacy & Compliance

- All modules handling personal data (M1, M3, M5) must comply with data protection regulations
- PII (phone, name, Aadhaar, PAN, bank details) must be encrypted at rest and in transit
- Access to partner data must be role-based and logged
- No module may share partner data with services outside its declared dependency list

---

## 9. Glossary

| Term | Definition |
|------|-----------|
| **CSP** | Channel Sales Partner — a local shopkeeper/entrepreneur who partners with Wiom to sell internet services |
| **Onboarding** | The complete process of taking a new CSP from application to active partnership |
| **KYC** | Know Your Customer — identity verification using PAN, Aadhaar, and GST documents |
| **Dedup** | Deduplication — checking if a phone number, bank account, or KYC number is already registered |
| **OTP** | One-Time Password — a temporary code sent via SMS/WhatsApp to verify phone ownership |
| **PAN** | Permanent Account Number — Indian income tax identification (10 characters) |
| **Aadhaar** | Indian unique identity number (12 digits) |
| **GST** | Goods and Services Tax identification number (15 characters) |
| **IFSC** | Indian Financial System Code — identifies a specific bank branch (11 characters) |
| **ISP** | Internet Service Provider — the company whose internet the CSP resells |
| **SLA** | Service Level Agreement — the performance standards the CSP must maintain |
| **QA Review** | Human review of the partner's submitted documents by Wiom's QA team |
| **Technical Assessment** | On-ground evaluation of the partner's infrastructure, network, and location |
| **Razorpay** | Third-party payment gateway used for collecting fees |
| **RazorpayX** | Razorpay's payout product used for creating partner fund accounts |
| **Zoho CRM** | Third-party CRM system where partner records are maintained |
| **TAT** | Turn Around Time — expected duration for a process to complete |
| **CMS** | Content Management System — internal system serving T&C, policy, and other content |
| **PII** | Personally Identifiable Information — data that can identify an individual |

---

*This document is the master reference for the Wiom CSP Onboarding Service module architecture. For screen-by-screen specifications, validation rules, error scenarios, and test cases, refer to the individual module documents (M1 through M7) in this directory.*

---

**Module Documents (to be created):**
- `M1_AUTH.md` — Authentication Service
- `M2_TNC.md` — Terms & Conditions Service
- `M3_REGISTRATION.md` — Registration Service
- `M4_FEE.md` — Fee Collection Service
- `M5_VERIFICATION_ASSESSMENT.md` — Verification & Assessment Service
- `M6_CSP_POLICY.md` — CSP Policy Service
- `M7_CSP_ACCOUNT_SETUP.md` — CSP Account Setup Service
