# Wiom CSP Onboarding V4

> **Last updated:** 30 March 2026, 4:00 PM IST

Android app + dashboards for Wiom Channel Sales Partner (CSP) onboarding — 15-screen flow from registration to live partner.

## Quick Start

### 1. Install APK
Download from [`apk/wiom-csp-onboarding-v4.apk`](apk/wiom-csp-onboarding-v4.apk) and install on Android device/emulator.

### 2. Run Dashboards
```bash
# Start file server (serves dashboards + prototype)
python3 -m http.server 8090

# Start bridge (connects dashboards to emulator via ADB)
cd dashboard && python3 bridge.py
```
- **Control Dashboard:** http://localhost:8090/dashboard/control.html
- **QA Review Dashboard:** http://localhost:8090/dashboard/qa-review.html

### 3. View HTML Prototype
Open [`prototype/index.html`](prototype/index.html) in browser or visit http://localhost:8090/prototype/index.html

## Structure

```
CSP-Onboarding-V4/
├── apk/                    # Built APK (ready to install)
├── app/                    # Android source code (Kotlin + Jetpack Compose)
│   └── src/main/java/com/wiom/csp/
│       ├── ui/screens/     # 1 file per screen (16 files)
│       ├── ui/components/  # Reusable UI components (10 files)
│       ├── ui/viewmodel/   # ViewModels per screen
│       ├── ui/navigation/  # Jetpack Navigation graph
│       ├── ui/theme/       # Wiom design tokens
│       ├── data/           # State management
│       └── util/           # Bilingual strings, validation
├── dashboard/              # Control + QA dashboards
│   ├── control.html        # Navigate screens, trigger scenarios
│   ├── qa-review.html      # QA review checklist
│   ├── admin-qa.html       # Admin QA dashboard
│   └── bridge.py           # ADB bridge server (Python)
├── prototype/              # HTML interactive prototype (reference)
│   └── index.html          # All 15 screens
└── docs/                   # Documentation
    ├── PRD_HUMAN.md         # Product Requirements (Human-readable)
    ├── PRD_AI_AGENT.md      # Product Requirements (AI Agent format)
    ├── DEVELOPER_GUIDE.md   # Developer setup guide
    └── CLAUDE.md            # AI context file
```

## Screen Flow (15 screens + Pitch)

```
[Pitch] → 0:Phone → 1:OTP → 2:Personal → 3:Location → 4:RegFee(₹2K)
→ 5:KYC(PAN→Aadhaar→GST) → 6:Bank → 7:ISP → 8:ShopPhotos → 9:Verification
→ 10:Policy → 11:TechAssessment → 12:OnboardFee(₹20K) → 13:AccountSetup → 14:Onboarded
```

## Key Features

- **Bilingual:** Hindi-first with instant English toggle (no app restart)
- **Dashboard Control:** Navigate to any screen, trigger error scenarios (payment failed, OTP expired, etc.)
- **Complete Edge Cases:** Phone duplicate, wrong OTP, bank verification failure, name mismatch, area not serviceable
- **Wiom Design System:** Brand colors, tokens, consistent UI patterns
- **Natural Hinglish:** Copy written for shopkeepers, not government forms

## Tech Stack

- Kotlin + Jetpack Compose + Material 3
- Hilt for DI
- Jetpack Navigation
- MVVM architecture
- Python bridge for dashboard-emulator communication

## Build from Source

```bash
export ANDROID_HOME=~/Library/Android/sdk
./gradlew assembleDebug
# APK at: app/build/outputs/apk/debug/app-debug.apk
```

## Audits Applied

- **17 dead CTAs** wired with proper actions (dialer, navigation, retry)
- **85+ Hindi strings** updated to natural Hinglish (सत्यापन→वेरिफिकेशन, भुगतान→पेमेंट, etc.)
- **Step counters** standardized across all phases
- **Code split** into 26 focused files (1 screen per file, 10 component files)
- **Consistent BottomBar** pattern across all screens
