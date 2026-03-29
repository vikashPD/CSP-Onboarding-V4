package com.wiom.csp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.wiom.csp.data.OnboardingState
import com.wiom.csp.ui.screens.*
import com.wiom.csp.ui.viewmodel.*

// Screen index → route mapping
private val screenRoutes = listOf(
    Routes.PHONE,              // 0
    Routes.OTP,                // 1
    Routes.PERSONAL,           // 2
    Routes.LOCATION,           // 3
    Routes.REG_FEE,            // 4
    Routes.KYC,                // 5
    Routes.BANK,               // 6
    Routes.ISP_AGREEMENT,      // 7
    Routes.PHOTOS,             // 8
    Routes.VERIFICATION,       // 9
    Routes.POLICY_SLA,         // 10
    Routes.TECH_ASSESSMENT,    // 11
    Routes.ONBOARD_FEE,        // 12
    Routes.ACCOUNT_SETUP,      // 13
    Routes.SUCCESSFULLY_ONBOARDED, // 14
)

@Composable
fun OnboardingNavGraph(navController: NavHostController) {
    // Observe dashboard navigation commands
    LaunchedEffect(Unit) {
        snapshotFlow { OnboardingState.dashboardNavTarget }
            .collect { target ->
                if (target in screenRoutes.indices) {
                    val route = screenRoutes[target]
                    OnboardingState.dashboardNavTarget = -1 // consume
                    navController.navigate(route) {
                        popUpTo(Routes.PITCH) { inclusive = false }
                    }
                } else if (target == -2) {
                    // Special: go to pitch
                    OnboardingState.dashboardNavTarget = -1
                    navController.navigate(Routes.PITCH) {
                        popUpTo(Routes.PITCH) { inclusive = true }
                    }
                }
            }
    }

    NavHost(navController = navController, startDestination = Routes.PITCH) {
        composable(Routes.PITCH) {
            PitchScreen(onGetStarted = { navController.navigate(Routes.PHONE) { popUpTo(Routes.PITCH) { inclusive = true } } })
        }
        composable(Routes.PHONE) {
            val vm: PhoneViewModel = hiltViewModel()
            PhoneEntryScreen(
                viewModel = vm,
                onNext = { navController.navigate(Routes.OTP) },
            )
        }
        composable(Routes.OTP) {
            val vm: OtpViewModel = hiltViewModel()
            OtpTncScreen(
                viewModel = vm,
                onNext = { navController.navigate(Routes.PERSONAL) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.PERSONAL) {
            val vm: PersonalInfoViewModel = hiltViewModel()
            PersonalInfoScreen(
                viewModel = vm,
                onNext = { navController.navigate(Routes.LOCATION) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.LOCATION) {
            val vm: LocationViewModel = hiltViewModel()
            LocationScreen(
                viewModel = vm,
                onNext = { navController.navigate(Routes.REG_FEE) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.REG_FEE) {
            val vm: PaymentViewModel = hiltViewModel()
            RegFeeScreen(
                viewModel = vm,
                onNext = { navController.navigate(Routes.KYC) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.KYC) {
            val vm: KycViewModel = hiltViewModel()
            KycScreen(
                viewModel = vm,
                onNext = { navController.navigate(Routes.BANK) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.BANK) {
            val vm: BankViewModel = hiltViewModel()
            BankDetailsScreen(
                viewModel = vm,
                onNext = { navController.navigate(Routes.ISP_AGREEMENT) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.ISP_AGREEMENT) {
            val vm: IspAgreementViewModel = hiltViewModel()
            IspAgreementScreen(
                viewModel = vm,
                onNext = { navController.navigate(Routes.PHOTOS) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.PHOTOS) {
            val vm: PhotosViewModel = hiltViewModel()
            ShopPhotosScreen(
                viewModel = vm,
                onNext = { navController.navigate(Routes.VERIFICATION) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.VERIFICATION) {
            val vm: VerificationViewModel = hiltViewModel()
            VerificationScreen(
                viewModel = vm,
                onNext = { navController.navigate(Routes.POLICY_SLA) },
            )
        }
        composable(Routes.POLICY_SLA) {
            PolicySlaScreen(
                onNext = { navController.navigate(Routes.TECH_ASSESSMENT) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.TECH_ASSESSMENT) {
            val vm: TechAssessmentViewModel = hiltViewModel()
            TechAssessmentScreen(
                viewModel = vm,
                onNext = { navController.navigate(Routes.ONBOARD_FEE) },
            )
        }
        composable(Routes.ONBOARD_FEE) {
            val vm: PaymentViewModel = hiltViewModel()
            OnboardingFeeScreen(
                viewModel = vm,
                onNext = { navController.navigate(Routes.ACCOUNT_SETUP) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.ACCOUNT_SETUP) {
            val vm: AccountSetupViewModel = hiltViewModel()
            AccountSetupScreen(
                viewModel = vm,
                onNext = { navController.navigate(Routes.SUCCESSFULLY_ONBOARDED) { popUpTo(Routes.PITCH) { inclusive = true } } },
            )
        }
        composable(Routes.SUCCESSFULLY_ONBOARDED) {
            SuccessfullyOnboardedScreen()
        }
    }
}
