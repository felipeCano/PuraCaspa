package com.pura.caspa.compose

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pura.caspa.presentation.view.CreateOrJoinTable
import com.pura.caspa.presentation.view.ConfirmTableCreation
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.util.Consumer
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.pura.caspa.presentation.view.JoinTable
import com.pura.caspa.presentation.view.PuraCaspaGameView


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    //We observer the new intent to navigate
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val listener = Consumer<Intent> { intent ->
            navController.handleDeepLink(intent)
        }
        val activity = context as? ComponentActivity
        activity?.addOnNewIntentListener(listener)
        onDispose {
            activity?.removeOnNewIntentListener(listener)
        }
    }

    //All routes
    NavHost(
        navController = navController,
        startDestination = "create_or_join_table"
    ){
        composable (route = "create_or_join_table"){
            CreateOrJoinTable(onNavigateToConfirmTableCreation = {
                navController.navigate("confirm_table_creation")
            }, onNavigatetoJoinTable = {
                navController.navigate("join_table")
            },modifier)

        }
        composable (
            route = "confirm_table_creation",
        ){
            ConfirmTableCreation(
                onNavigateToPuraCaspaGameView = { tableName ->
                navController.navigate("puracaspa_game_view/$tableName")
            },modifier)
        }

        composable (
            route = "join_table?id={table_id}",
            arguments = listOf(
                navArgument("table_id") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            ),
            deepLinks = listOf(
                navDeepLink { uriPattern = "https://puracaspa.com/join/{table_id}" }
            )
        ){ backStackEntry ->
            val idComingDeepLink = backStackEntry.arguments?.getString("table_id")

            JoinTable(
                onNavigateJoinToPuraCaspaGameView = { tableName ->
                    navController.navigate("puracaspa_game_view/$tableName")
                },
                modifier = modifier,
                initialTableId = idComingDeepLink
            )
        }

        composable (
            route = "puracaspa_game_view/{name_table}",
            arguments = listOf(
                navArgument("name_table"){
                    type = NavType.StringType
                }
            )
        ){
            PuraCaspaGameView(
                modifier,
                nameTable = it.arguments?.getString("name_table") ?: ""
            )
        }

    }
}