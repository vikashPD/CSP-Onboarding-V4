# Wiom CSP Onboarding — CleverTap Event Sheet

**Version:** 1.1 | **Date:** 30 March 2026 | **Status:** Draft
**Source PRD:** PRD_HUMAN.md V3.2
**Platform:** Android (com.wiom.csp)

---

## Naming Convention

- **Event names:** `snake_case`, `verb_noun` format (e.g., `screen_viewed`, `otp_sent`)
- **Property names:** `snake_case` (e.g., `screen_name`, `payment_amount`)
- **Property values:** lowercase strings unless proper nouns (e.g., `"pan"`, `"aadhaar"`, `"Rajesh Kumar"`)
- **Timestamps:** ISO 8601 UTC (e.g., `2026-03-30T10:15:00Z`)
- **Amounts:** Integer INR (e.g., `2000`, `20000`)
- **Boolean:** `true` / `false`

---

## Common Properties (sent with EVERY event)

| Property | Type | Description | Example |
|----------|------|-------------|---------|
| `session_id` | String | Unique session identifier | `"sess_abc123"` |
| `app_version` | String | App version | `"3.2.0"` |
| `platform` | String | Always `"android"` | `"android"` |
| `language` | String | Current language setting | `"hi"` / `"en"` |
| `phase` | String | Current onboarding phase | `"registration"` / `"verification"` / `"activation"` |
| `screen_number` | Integer | Current screen (Pitch = -1, Screens 0-14) | `5` |
| `screen_name` | String | Human-readable screen name | `"kyc_documents"` |
| `partner_phone` | String | Masked phone (last 4 digits) | `"******3210"` |
| `partner_id` | String | Partner ID (after registration) | `"CSP_00123"` |
| `timestamp` | String | Event timestamp (ISO 8601) | `"2026-03-30T10:15:00Z"` |
| `time_on_screen_sec` | Integer | Seconds spent on current screen before event | `45` |

---

## 1. Global Events (All Screens)

### 1.1 Screen Navigation

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `screen_viewed` | Screen becomes visible | `screen_number`, `screen_name`, `phase`, `step_label`, `source` (`"navigation"` / `"back"` / `"deep_link"` / `"dev_skip"`) | Funnel analysis, drop-off tracking |
| `screen_exited` | User leaves screen | `screen_number`, `screen_name`, `time_on_screen_sec`, `exit_method` (`"cta"` / `"back"` / `"app_kill"` / `"timeout"`) | Time-on-screen analysis, drop-off points |
| `back_button_pressed` | User presses Android back | `screen_number`, `screen_name`, `destination_screen` | Navigation pattern analysis |
| `disabled_cta_tapped` | User taps a disabled CTA button | `screen_number`, `screen_name`, `cta_name`, `disabled_reason` | Friction point tracking |
| `session_data_restored` | Form data preserved after back-forward navigation | `screen_number`, `fields_restored_count` | Data persistence verification |

### 1.2 Language & App State

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `language_toggled` | User taps Hindi/English toggle | `from_language`, `to_language`, `screen_number` | Language preference tracking |
| `app_opened` | App launched | `is_fresh_install` (bool), `last_screen_number`, `time_since_last_open_hrs` | Retention, session analysis |
| `app_backgrounded` | App goes to background | `screen_number`, `screen_name`, `time_in_session_sec` | Session duration |
| `app_foregrounded` | App returns to foreground | `screen_number`, `time_in_background_sec` | Re-engagement |

### 1.3 Uninstall & Reinstall

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `app_uninstalled` | CSP uninstalls the app (detected by CleverTap) | `last_screen_number`, `last_screen_name`, `phase`, `days_since_install`, `reg_fee_paid` (bool), `verification_status` (`"not_submitted"` / `"pending"` / `"approved"` / `"rejected"` / `null`) | Uninstall tracking, churn analysis |
| `app_reinstalled` | CSP reinstalls after previous uninstall | `days_since_uninstall`, `reinstall_source` (`"organic"` / `"notification"` / `"unknown"`), `last_phase_before_uninstall` | Re-engagement effectiveness |

### 1.4 Error States

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `error_displayed` | Any error overlay shown | `error_type` (`"no_internet"` / `"server_error"` / `"phone_duplicate"` / etc.), `screen_number`, `is_blocking` (bool), `is_retryable` (bool) | Error frequency, screen-wise error distribution |
| `error_retry_tapped` | User taps retry on error | `error_type`, `screen_number`, `retry_count` | Retry behavior |
| `error_dismissed` | Error dismissed or resolved | `error_type`, `screen_number`, `resolution` (`"retry_success"` / `"changed_input"` / `"dismissed"`) | Error resolution rates |
| `talk_to_us_tapped` | User taps "Talk to Us" CTA | `screen_number`, `error_type`, `phone_number` (`"7836811111"`) | Support escalation tracking |

---

## 2. Pitch Screen (Pre-flow)

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `pitch_screen_viewed` | Pitch screen shown | `is_first_time` (bool) | First impression tracking |
| `pitch_get_started_tapped` | User taps "Get Started" | `time_on_pitch_sec` | Conversion from pitch to registration |

---

## 3. Phase 1 — Registration (Screens 0-4)

### Screen 0: Phone Entry

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `phone_field_focused` | User taps phone input | — | Interaction start |
| `phone_digits_entered` | User types digits | `digit_count` (1-10) | Partial entry tracking |
| `phone_entry_completed` | All 10 digits entered | `time_to_complete_sec` | Form completion speed |
| `tnc_checkbox_toggled` | T&C checkbox checked/unchecked | `is_checked` (bool) | T&C engagement |
| `tnc_link_tapped` | User taps "Read Terms & Conditions" | — | T&C read rate |
| `send_otp_tapped` | User taps "Send OTP" | `phone_masked` | Registration start |
| `phone_duplicate_shown` | Duplicate phone error displayed | `phone_masked` | Duplicate rate |
| `phone_duplicate_login_tapped` | User chooses to login | — | Redirect to login |
| `phone_duplicate_new_number_tapped` | User chooses new number | — | Re-entry behavior |
| `phone_input_blocked` | Non-numeric or >10 digit input blocked | `block_reason` (`"non_numeric"` / `"exceeds_10_digits"`) | Input validation tracking |

### Screen 1: OTP Verification

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `otp_sent` | OTP dispatched to phone | `phone_masked`, `delivery_method` (`"sms"` / `"whatsapp"`) | OTP delivery tracking |
| `otp_digit_entered` | Each OTP digit entered | `digits_filled` (1-4) | Input progress |
| `otp_submitted` | User taps "Verify" | `time_to_enter_sec` | OTP entry speed |
| `otp_verified_success` | OTP matches | `attempt_number` (1-3), `time_to_verify_sec` | Success rate |
| `otp_verified_failed` | OTP does not match | `attempt_number`, `attempts_remaining` | Failure rate |
| `otp_expired` | Timer reaches 0 | `time_elapsed_sec` (28) | Expiry rate |
| `otp_resend_tapped` | User taps "Resend OTP" | `resend_count` | Resend frequency |
| `otp_change_number_tapped` | User taps "Change Number" | — | Number change rate |
| `otp_timer_started` | Countdown timer begins | `duration_sec` (28), `trigger` (`"initial"` / `"resend"`) | Timer tracking |
| `otp_max_attempts_reached` | 3 wrong attempts exhausted, user locked out | `phone_masked` | Lockout tracking |

### Screen 2: Personal & Business Info

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `field_filled` | User completes a form field | `field_name` (`"full_name"` / `"email"` / `"entity_type"` / `"business_name"`), `field_index` (1-4), `is_valid` (bool) | Field-level completion tracking |
| `field_error_shown` | Validation error on field | `field_name`, `error_type` (`"blank"` / `"invalid_email"`) | Input quality |
| `entity_type_selected` | Dropdown selection made | `entity_type` (`"proprietorship"`) | — |
| `personal_info_submitted` | CTA "Add Business Location" tapped | `time_on_screen_sec`, `all_fields_valid` (bool) | Screen completion |
| `field_focused` | User taps into a form field | `field_name` (`"full_name"` / `"email"` / `"entity_type"` / `"business_name"`), `screen_number` | Interaction start per field |
| `business_name_locked` | Business name field locked after reg fee payment | `business_name` | Lock event tracking |

### Screen 3: Business Location

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `state_selected` | State chosen from dropdown | `state_name` | Geographic distribution |
| `city_entered` | City field filled | `city_name` | City-level analysis |
| `pincode_entered` | Pincode filled | `pincode`, `is_valid` (bool) | Location validation |
| `gps_captured` | GPS coordinates obtained | `latitude`, `longitude`, `accuracy_meters` | Location accuracy |
| `gps_capture_failed` | GPS capture failed | `error_reason` | GPS failure rate |
| `location_submitted` | CTA "Pay Registration Fee" tapped | `state_name`, `city_name`, `pincode`, `time_on_screen_sec` | Screen completion |
| `address_entered` | Full address field filled | `char_count` | Address field tracking |
| `gps_capture_initiated` | GPS capture started (before success/fail) | — | GPS flow start |
| `pincode_validation_error` | Invalid pincode entered | `error_type` (`"incomplete"` / `"invalid"`) | Pincode validation |

### Screen 4: Registration Fee (Rs.2,000)

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `payment_initiated` | User taps "Pay ₹2,000 Now" | `amount` (2000), `fee_type` (`"registration"`) | Payment funnel start |
| `payment_processing` | Payment processing begins | `amount`, `fee_type`, `payment_method` | — |
| `payment_completed` | Payment succeeds | `amount`, `fee_type`, `payment_method`, `transaction_id`, `time_to_complete_sec` | Payment success rate |
| `payment_failed` | Payment declined | `amount`, `fee_type`, `error_code`, `error_type` (`"declined"` / `"timeout"`), `retry_count` | Payment failure analysis |
| `payment_retry_tapped` | User retries payment | `amount`, `fee_type`, `retry_count` | Retry behavior |
| `payment_refresh_tapped` | User taps Refresh Status on timeout | `amount`, `fee_type` | Timeout refresh tracking (distinct from retry) |

---

## 4. Phase 2 — Verification (Screens 5-9)

### Screen 5: KYC Documents

#### Sub-stage Navigation

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `kyc_substage_entered` | User enters a KYC sub-stage | `substage` (`"pan"` / `"aadhaar"` / `"gst"`), `substage_index` (1-3) | Sub-stage funnel |
| `kyc_substage_completed` | Sub-stage fully completed | `substage`, `time_on_substage_sec` | Sub-stage completion time |
| `kyc_all_completed` | All 3 sub-stages done | `total_time_sec` | KYC completion rate |
| `kyc_substage_next_tapped` | User taps Next to proceed between sub-stages | `from_substage`, `to_substage` | Sub-stage progression |
| `kyc_substage_locked_tapped` | User tries to access a locked sub-stage | `attempted_substage`, `current_substage` | Friction tracking |

#### Number Entry (PAN / Aadhaar / GST)

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `kyc_number_focused` | User taps number field | `doc_type` (`"pan"` / `"aadhaar"` / `"gst"`) | Interaction start |
| `kyc_number_entered` | Number field filled | `doc_type`, `is_valid` (bool), `char_count` | Entry tracking |
| `kyc_number_validated` | Validation runs on blur | `doc_type`, `is_valid` (bool), `error_type` (`"format_invalid"` / `"pan_mismatch"` / `null`) | Validation results |
| `kyc_number_error_shown` | Validation error displayed | `doc_type`, `error_type`, `error_message` | Error frequency per doc type |

#### Document Upload (PAN / Aadhaar Front / Aadhaar Back / GST)

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `doc_upload_started` | User initiates upload | `doc_type`, `upload_source` (`"camera"` / `"gallery"`), `substage` | Upload method preference |
| `doc_upload_source_selected` | Camera/gallery chosen | `doc_type`, `upload_source` | Source preference |
| `doc_upload_photo_captured` | Photo taken or selected | `doc_type`, `upload_source` | Capture success |
| `doc_upload_photo_retaken` | User retakes photo | `doc_type`, `retake_count` | Quality issues |
| `doc_upload_completed` | Upload finishes successfully | `doc_type`, `upload_source`, `upload_time_sec`, `file_size_kb` | Upload success rate |
| `doc_upload_failed` | Upload fails | `doc_type`, `error_reason` | Upload failure rate |
| `doc_upload_removed` | User removes uploaded doc | `doc_type` | Re-upload rate |
| `view_sample_doc_tapped` | User taps "View sample document" | `doc_type`, `substage` | Sample doc usage |

### Screen 6: Bank Details

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `bank_field_filled` | Bank field completed | `field_name` (`"account_number"` / `"confirm_account_number"` / `"ifsc_code"`), `is_valid` (bool) | Field completion |
| `bank_account_masked` | Account number masked on blur | — | UX confirmation |
| `bank_account_mismatch` | Re-enter doesn't match | `attempt_count` | Mismatch frequency |
| `bank_ifsc_validated` | IFSC validated | `is_valid` (bool), `ifsc_code` | IFSC validation |
| `add_bank_document_tapped` | CTA "Add Bank Document" tapped | `all_fields_valid` (bool) | Bank screen completion |
| `bank_dedup_checked` | Dedup check triggered | — | Dedup check rate |
| `bank_dedup_clear` | No dedup found | — | Clear rate |
| `bank_dedup_found` | Dedup match found | — | Dedup hit rate |
| `bank_dedup_change_tapped` | User taps "Change Bank Details" on dedup bottom sheet | — | Resolution behavior |
| `bank_doc_upload_started` | Bank document upload initiated | `doc_subtype` (`"bank_statement"` / `"cancelled_cheque"` / `"bank_passbook"`), `upload_source` | Doc type preference |
| `bank_doc_upload_completed` | Bank document uploaded | `doc_subtype`, `upload_source`, `upload_time_sec` | Upload success |
| `bank_doc_view_sample_tapped` | User views sample bank doc | — | Sample usage |
| `bank_doc_type_selected` | User selects bank doc type from 3 options | `doc_subtype` (`"bank_statement"` / `"cancelled_cheque"` / `"bank_passbook"`) | Doc type preference |
| `bank_doc_upload_failed` | Bank document upload fails | `doc_subtype`, `error_reason` | Upload failure tracking |
| `bank_doc_submitted` | Bank document screen completed, proceeds to ISP | `doc_subtype`, `upload_time_sec` | Screen completion |

### Screen 7: ISP Agreement Upload

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `isp_upload_started` | Upload initiated | `upload_method` (`"pdf"` / `"camera"` / `"gallery"`) | Method preference |
| `isp_page_added` | Each page captured/selected | `page_number` (1-7), `upload_method` | Multi-page tracking |
| `isp_page_removed` | Page removed | `page_number` | Re-upload |
| `isp_upload_completed` | ISP agreement upload done | `total_pages`, `upload_method`, `total_time_sec` | Completion tracking |
| `isp_view_sample_tapped` | User views sample ISP doc | — | Sample usage |
| `isp_checklist_viewed` | Mandatory checklist viewed | — | Checklist engagement |
| `isp_submitted` | CTA tapped to proceed | `total_pages` | Screen completion |

### Screen 8: Shop & Equipment Photos

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `shop_photo_uploaded` | Shop front photo uploaded | `upload_source`, `upload_time_sec` | Shop photo completion |
| `shop_photo_removed` | Shop photo removed | — | Re-upload |
| `shop_photo_retaken` | Shop photo retaken | `retake_count` | Quality issues |
| `equipment_photo_uploaded` | Equipment photo added | `photo_index` (1-5), `upload_source`, `total_photos`, `equipment_type` (`"power_backup"` / `"olt"` / `"isp_switch"` / `"other"`) | Equipment tracking |
| `equipment_photo_removed` | Equipment photo removed | `photo_index` | Re-upload |
| `shop_view_sample_tapped` | Sample shop photo viewed | — | Sample usage |
| `equipment_view_sample_tapped` | Sample equipment photo viewed | — | Sample usage |
| `photos_submitted` | CTA "Submit for Verification" tapped | `shop_photo_count` (1), `equipment_photo_count` (1-5), `total_time_sec` | Screen completion |
| `equipment_photo_retaken` | Equipment photo retaken | `photo_index`, `retake_count` | Quality issues |
| `shop_photo_upload_failed` | Shop photo upload fails | `error_reason` | Upload failure rate |
| `equipment_photo_upload_failed` | Equipment photo upload fails | `photo_index`, `error_reason` | Upload failure rate |

### Screen 9: Verification Status

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `verification_submitted` | All docs submitted, verification pending | `total_docs_count`, `time_since_reg_fee_hrs` | Submission tracking |
| `verification_status_checked` | User opens/refreshes verification screen | `current_status` (`"pending"` / `"approved"` / `"rejected"`), `days_since_submission` | Status check frequency |
| `verification_approved` | QA team approves | `review_time_hrs`, `reviewer_id` | Approval rate, TAT |
| `verification_rejected` | QA team rejects | `rejection_reason`, `review_time_hrs`, `reviewer_id` | Rejection rate, reasons |
| `refund_initiated` | Auto-refund triggered after rejection | `amount` (2000), `refund_method` | Refund tracking |
| `refund_status_viewed` | User checks refund status | `refund_status` (`"in_progress"` / `"success"` / `"failed"`), `days_since_rejection` | Refund monitoring |
| `refund_completed` | Refund successfully processed | `amount`, `days_to_refund` | Refund TAT |
| `refund_failed` | Refund processing failed | `amount`, `error_reason` | Refund failure rate |

---

## 5. Phase 3 — Activation (Screens 10-14)

### Screen 10: Technical Assessment

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `tech_assessment_pending` | Assessment screen shown | `days_since_verification_approved` | Wait time tracking |
| `tech_assessment_status_checked` | User refreshes/revisits | `current_status` (`"pending"` / `"passed"` / `"rejected"`), `days_since_start` | Check frequency |
| `tech_assessment_passed` | Network Quality team approves | `assessment_time_days`, `assessor_id` | Pass rate, TAT |
| `tech_assessment_rejected` | Network Quality team rejects | `rejection_reason`, `assessment_time_days` | Rejection rate, reasons |
| `tech_assessment_talk_to_us_tapped` | User taps "Talk to Us" after rejection | — | Support escalation |
| `tech_assessment_no_refund_shown` | No refund messaging displayed after rejection | — | User reaction to no-refund messaging |

### Screen 11: Policy & SLA

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `policy_screen_viewed` | Policy & SLA screen shown | `time_since_tech_assessment_passed_hrs` | Time between stages |
| `policy_content_scrolled` | User scrolls policy content | `scroll_depth_percent` (0-100) | Content engagement |
| `policy_commission_viewed` | Commission section visible | — | Section visibility |
| `policy_sla_viewed` | SLA section visible | — | Section visibility |
| `policy_accepted` | CTA "Understood, proceed" tapped | `time_on_screen_sec`, `scroll_depth_percent` | Acceptance rate, read time |

### Screen 12: Onboarding Fee (Rs.20,000)

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `onboarding_fee_screen_viewed` | Screen shown | `reg_fee_paid_date`, `days_since_reg_fee` | Time between payments |
| `investment_summary_viewed` | Investment breakdown visible | — | Engagement |
| `payment_initiated` | User taps "Pay ₹20,000 Now" | `amount` (20000), `fee_type` (`"onboarding"`) | Payment funnel |
| `payment_completed` | Payment succeeds | `amount`, `fee_type`, `payment_method`, `transaction_id`, `time_to_complete_sec` | Success rate |
| `payment_failed` | Payment declined | `amount`, `fee_type`, `error_code`, `error_type`, `retry_count` | Failure analysis |
| `payment_retry_tapped` | User retries | `amount`, `fee_type`, `retry_count` | Retry behavior |
| `payment_processing` | Payment processing begins | `amount` (20000), `fee_type` (`"onboarding"`), `payment_method` | Processing state tracking |
| `payment_refresh_tapped` | User taps Refresh Status on timeout | `amount` (20000), `fee_type` (`"onboarding"`) | Timeout refresh tracking |

### Screen 13: Account Setup

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `account_setup_started` | Setup screen shown | `business_name` | Setup tracking |
| `account_setup_completed` | Auto-progress after 3s | `setup_time_sec`, `business_name` | Success rate |
| `account_setup_failed` | Setup fails | `error_reason`, `retry_count` | Failure rate |
| `account_setup_retry_tapped` | User taps retry | `retry_count` | Retry behavior |
| `account_setup_pending` | Setup taking longer | `wait_time_sec` | Pending rate |
| `account_setup_refresh_tapped` | User taps refresh | — | Refresh behavior |

### Screen 14: Successfully Onboarded

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `onboarding_completed` | Success screen shown | `total_onboarding_time_days`, `total_onboarding_time_hrs`, `partner_name`, `business_name`, `city`, `state` | **KEY METRIC** — Overall completion |
| `app_download_tapped` | User taps "Install Now" | `time_on_success_screen_sec` | App download rate |
| `instructions_viewed` | User scrolls to instructions | `scroll_depth_percent` | Instruction engagement |
| `app_store_redirected` | User redirected to Play Store after Install Now tap | `store_url` | Actual redirect tracking |

---

## 6. System Events (Backend-Triggered)

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `otp_delivery_attempted` | System sends OTP | `phone_masked`, `delivery_method`, `provider` | Delivery tracking |
| `otp_delivery_confirmed` | OTP delivered successfully | `phone_masked`, `delivery_time_ms` | Delivery success rate |
| `otp_delivery_failed` | OTP delivery failed | `phone_masked`, `error_reason` | Delivery failure rate |
| `dedup_check_performed` | Any dedup check runs | `check_type` (`"phone"` / `"bank_account"`), `result` (`"clear"` / `"duplicate"`), `screen_number` | Dedup analysis |
| `payment_gateway_called` | Payment API called | `amount`, `fee_type`, `gateway` (`"razorpay"`), `payment_method` | Gateway performance |
| `payment_gateway_response` | Gateway responds | `amount`, `fee_type`, `status` (`"success"` / `"failed"` / `"timeout"`), `response_time_ms` | Gateway reliability |
| `refund_initiated_system` | Auto-refund triggered | `amount`, `trigger` (`"verification_rejected"` / `"payment_timeout"`), `partner_id` | Refund tracking |
| `refund_processed` | Refund completed by bank | `amount`, `processing_time_hrs`, `partner_id` | Refund TAT |
| `account_provisioned` | Backend account created | `services` (`["razorpayx", "zoho", "ledger"]`), `setup_time_sec` | Provisioning tracking |

---

## 7. Nudge & Reminder Events

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `nudge_sent` | System sends reminder | `nudge_type` (`"reg_fee_day1"` / `"reg_fee_day2"` / `"reg_fee_day3"` / `"reg_fee_day4"`), `channel` (`"push"` / `"sms"` / `"whatsapp"`), `days_since_last_activity` | Nudge delivery |
| `nudge_received` | Nudge delivered to device | `nudge_type`, `channel` | Delivery confirmation |
| `nudge_opened` | User opens/taps nudge | `nudge_type`, `channel`, `time_since_sent_min` | Nudge effectiveness |
| `nudge_dismissed` | User dismisses nudge | `nudge_type`, `channel` | Dismissal rate |
| `notification_sent` | System sends any campaign notification | `campaign_id`, `channel` (`"whatsapp"` / `"sms"` / `"push"`), `csp_id` | Campaign delivery tracking |
| `notification_delivered` | Notification reaches device/WhatsApp | `campaign_id`, `channel`, `delivery_time_ms` | Delivery confirmation |
| `notification_opened` | CSP taps/opens notification | `campaign_id`, `channel`, `time_since_sent_min` | Open rate tracking |
| `notification_action_taken` | CSP performs intended action within 24hrs | `campaign_id`, `channel`, `action` (`"resumed_onboarding"` / `"called_support"` / `"reinstalled_app"`) | Action effectiveness |
| `notification_dismissed` | CSP dismisses notification | `campaign_id`, `channel` | Dismissal tracking |
| `whatsapp_fallback_to_sms` | WhatsApp failed, SMS sent as fallback | `campaign_id`, `csp_id`, `whatsapp_failure_reason` | Fallback rate tracking |
| `auto_reject_triggered` | Day 4 auto-rejection fires | `partner_id`, `days_inactive` | Auto-rejection rate |

---

## 8. Dashboard Events (QA & Admin)

### QA Review Dashboard

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `qa_application_viewed` | QA opens an application | `partner_id`, `reviewer_id`, `application_status` | Review tracking |
| `qa_document_viewed` | QA views a specific document | `partner_id`, `doc_type`, `reviewer_id` | Document review tracking |
| `qa_decision_made` | QA approves or rejects | `partner_id`, `decision` (`"approved"` / `"rejected"`), `rejection_reason` (if rejected), `review_time_min`, `reviewer_id` | Decision tracking, TAT |
| `qa_decision_changed` | QA changes previous decision | `partner_id`, `from_decision`, `to_decision`, `reviewer_id` | Decision reversal tracking |
| `qa_search_performed` | QA searches applications | `search_query`, `filter_applied` (`"all"` / `"pending"` / `"approved"` / `"rejected"`), `results_count` | Search behavior |

### Control Dashboard (Admin)

| Event Name | Trigger | Properties | Purpose |
|------------|---------|------------|---------|
| `admin_screen_navigated` | Admin navigates to a screen | `target_screen_number`, `admin_id` | Admin usage |
| `admin_scenario_triggered` | Admin triggers error scenario | `scenario_name`, `target_screen`, `admin_id` | Testing patterns |
| `admin_scenario_cleared` | Admin clears scenario | `scenario_name`, `admin_id` | — |
| `admin_language_changed` | Admin toggles language | `to_language`, `admin_id` | — |
| `admin_data_filled` | Admin fills/empties form data | `mode` (`"filled"` / `"empty"`), `admin_id` | — |
| `admin_app_restarted` | Admin restarts app | `admin_id` | — |
| `admin_screenshot_taken` | Admin captures screenshot | `screen_number`, `admin_id` | — |
| `admin_state_reset` | Admin resets app state | `admin_id` | State reset tracking |
| `admin_state_dumped` | Admin dumps app state to file | `admin_id`, `dump_status` | State dump tracking |
| `admin_state_read` | Admin reads app state file | `admin_id` | State read tracking |

---

## 9. Key Funnels to Track

### Funnel 1: Overall Onboarding Completion
```
pitch_get_started_tapped
  → send_otp_tapped
    → otp_verified_success
      → personal_info_submitted
        → location_submitted
          → payment_completed (reg fee)
            → kyc_all_completed
              → bank_doc_upload_completed
                → isp_submitted
                  → photos_submitted
                    → verification_approved
                      → tech_assessment_passed
                        → policy_accepted
                          → payment_completed (onboarding fee)
                            → account_setup_completed
                              → onboarding_completed
```

### Funnel 2: Registration Phase
```
pitch_get_started_tapped → send_otp_tapped → otp_verified_success → personal_info_submitted → location_submitted → payment_completed (reg fee)
```

### Funnel 3: KYC Document Completion
```
kyc_substage_entered (pan) → kyc_substage_completed (pan) → kyc_substage_entered (aadhaar) → kyc_substage_completed (aadhaar) → kyc_substage_entered (gst) → kyc_substage_completed (gst) → kyc_all_completed
```

### Funnel 4: Payment Conversion
```
payment_initiated → payment_processing → payment_completed / payment_failed
(tracked separately for reg fee and onboarding fee)
```

### Funnel 5: Verification to Activation
```
verification_submitted → verification_approved → tech_assessment_passed → policy_accepted → payment_completed (onboarding fee)
```

---

## 10. Key Metrics & Dashboards

### Conversion Metrics
| Metric | Calculation | Purpose |
|--------|-------------|---------|
| **Overall Conversion Rate** | `onboarding_completed` / `pitch_get_started_tapped` | End-to-end success |
| **Phase 1 Completion Rate** | `payment_completed (reg fee)` / `send_otp_tapped` | Registration funnel |
| **Phase 2 Completion Rate** | `verification_submitted` / `kyc_substage_entered (pan)` | Document funnel |
| **Phase 3 Completion Rate** | `onboarding_completed` / `verification_approved` | Activation funnel |
| **OTP Success Rate** | `otp_verified_success` / `otp_submitted` | OTP reliability |
| **Payment Success Rate** | `payment_completed` / `payment_initiated` | Payment reliability |
| **Verification Approval Rate** | `verification_approved` / `verification_submitted` | QA pass rate |
| **Tech Assessment Pass Rate** | `tech_assessment_passed` / `tech_assessment_pending` | Assessment pass rate |

### Time-Based Metrics
| Metric | Calculation | Purpose |
|--------|-------------|---------|
| **Total Onboarding Duration** | `onboarding_completed.timestamp` - `pitch_get_started_tapped.timestamp` | End-to-end time |
| **Registration Duration** | `payment_completed (reg).timestamp` - `send_otp_tapped.timestamp` | Phase 1 speed |
| **KYC Completion Time** | `kyc_all_completed.timestamp` - `kyc_substage_entered (pan).timestamp` | KYC speed |
| **Verification TAT** | `verification_approved.timestamp` - `verification_submitted.timestamp` | QA review time |
| **Tech Assessment TAT** | `tech_assessment_passed.timestamp` - `tech_assessment_pending.timestamp` | Assessment time |
| **Time Between Payments** | `payment_completed (onboard).timestamp` - `payment_completed (reg).timestamp` | Payment gap |

### Behavioral Metrics
| Metric | Calculation | Purpose |
|--------|-------------|---------|
| **Drop-off Screen** | Screen with highest `screen_exited` without `screen_viewed` for next screen | Identify friction |
| **Avg. Screen Time** | Avg of `time_on_screen_sec` per screen | Screen complexity |
| **Language Preference** | Distribution of `language` in common properties | Hindi vs English |
| **Upload Method Preference** | Distribution of `upload_source` across doc uploads | Camera vs Gallery |
| **Nudge Response Rate** | `nudge_opened` / `nudge_sent` | Nudge effectiveness |
| **Error Rate per Screen** | `error_displayed` count per `screen_number` | Screen reliability |
| **Retry Success Rate** | Successful events after `error_retry_tapped` / `error_retry_tapped` | Retry effectiveness |
| **Sample Doc View Rate** | `view_sample_doc_tapped` / `doc_upload_started` | Sample usefulness |

### Geographic Metrics
| Metric | Calculation | Purpose |
|--------|-------------|---------|
| **State-wise Registrations** | Count of `location_submitted` grouped by `state_name` | Geographic distribution |
| **City-wise Completion Rate** | `onboarding_completed` / `send_otp_tapped` grouped by `city_name` | City performance |
| **State-wise Drop-off** | Drop-off funnel grouped by `state_name` | Regional friction |

---

## 11. Event Count Summary

| Category | Event Count |
|----------|-------------|
| Global (navigation, language, errors) | 13 |
| Pitch Screen | 2 |
| Screen 0: Phone Entry | 10 |
| Screen 1: OTP Verification | 10 |
| Screen 2: Personal & Business Info | 6 |
| Screen 3: Business Location | 9 |
| Screen 4: Registration Fee | 7 |
| Screen 5: KYC Documents | 17 |
| Screen 6: Bank Details | 15 |
| Screen 7: ISP Agreement | 7 |
| Screen 8: Shop & Equipment Photos | 11 |
| Screen 9: Verification Status | 8 |
| Screen 10: Technical Assessment | 6 |
| Screen 11: Policy & SLA | 5 |
| Screen 12: Onboarding Fee | 8 |
| Screen 13: Account Setup | 6 |
| Screen 14: Successfully Onboarded | 4 |
| System Events | 9 |
| Nudge Events | 5 |
| QA Dashboard | 5 |
| Admin Dashboard | 10 |
| **Total** | **~178 events** |

---

*This event sheet covers all user actions, system events, dashboard events, and nudges across the complete Wiom CSP Onboarding flow (V3.2). Designed for comprehensive funnel analysis, drop-off tracking, behavioral insights, and operational dashboards in CleverTap.*
