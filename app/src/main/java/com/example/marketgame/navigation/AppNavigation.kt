package com.example.marketgame.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.marketgame.ui.screens.auth.LoginScreen
import com.example.marketgame.ui.screens.auth.RegisterScreen
import com.example.marketgame.ui.screens.game.arithmetic.ArithmeticScreen
import com.example.marketgame.ui.screens.game.matchcard.MatchCardScreen
import com.example.marketgame.ui.screens.game.mythfacts.MythFactScreen
import com.example.marketgame.ui.screens.game.quiz.QuizScreen
import com.example.marketgame.ui.screens.game.river.CleanRiverScreen
import com.example.marketgame.ui.screens.game.sudoku.SudokuScreen
import com.example.marketgame.ui.screens.game.trashsort.TrashSortScreen
import com.example.marketgame.ui.screens.home.HomeScreen
import com.example.marketgame.ui.screens.profile.ProfileScreen
import com.example.marketgame.ui.screens.profile.achievement.AchievementHubScreen
import com.example.marketgame.ui.screens.profile.achievement.MatchCardAchievementDetailScreen
import com.example.marketgame.ui.screens.profile.achievement.MatchCardAchievementScreen
import com.example.marketgame.ui.screens.profile.achievement.MythFactAchievementScreen
import com.example.marketgame.ui.screens.profile.achievement.QuizAchievementScreen
import com.example.marketgame.ui.screens.profile.achievement.TrashSortAchievementScreen
import com.example.marketgame.ui.screens.profile.vouchers.VouchersScreen
import com.example.marketgame.ui.screens.settings.ApiSettingsScreen
import com.example.marketgame.ui.screens.splash.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) { SplashScreen(navController = navController) }
        composable(Screen.Login.route) { LoginScreen(navController = navController) }
        composable(Screen.Register.route) { RegisterScreen(navController = navController) }
        composable(Screen.Home.route) { HomeScreen(navController = navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController = navController) }
        composable(Screen.Quiz.route) { QuizScreen(navController = navController) }
        composable(Screen.TrashSort.route) { TrashSortScreen(navController = navController) }
        composable(Screen.MatchCard.route) { MatchCardScreen(navController = navController) }
        composable(Screen.CleanRiver.route) { CleanRiverScreen(navController = navController) }
        composable(Screen.MythFact.route) { MythFactScreen(navController = navController) }
        composable(Screen.ArithmeticKilat.route) { ArithmeticScreen(navController = navController) }
        composable(Screen.Sudoku.route) { SudokuScreen(navController = navController) }
        composable(Screen.Vouchers.route) { VouchersScreen(navController = navController) }
        composable(Screen.ApiSettings.route) { ApiSettingsScreen() }
        composable(Screen.AchievementHub.route) { AchievementHubScreen(navController = navController) }
        composable(Screen.AchievementQuiz.route) { QuizAchievementScreen(navController = navController) }
        composable(Screen.AchievementMatchCard.route) { MatchCardAchievementScreen(navController = navController) }
        composable(Screen.AchievementMythFact.route) { MythFactAchievementScreen(navController = navController) }
        composable(Screen.AchievementTrashSort.route) { TrashSortAchievementScreen(navController = navController) }
        composable(
            route = Screen.AchievementMatchCardDetail.route,
            arguments = listOf(navArgument("sdgId") { type = NavType.IntType })
        ) { backStackEntry ->
            val sdgId = backStackEntry.arguments?.getInt("sdgId") ?: 1
            MatchCardAchievementDetailScreen(navController = navController, sdgId = sdgId)
        }
    }
}