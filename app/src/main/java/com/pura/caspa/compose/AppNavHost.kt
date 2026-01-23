package com.pura.caspa.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pura.caspa.presentation.view.CreateOrJoinTable
import com.pura.caspa.presentation.view.ConfirmTableCreation
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.pura.caspa.presentation.view.JoinTable
import com.pura.caspa.presentation.view.PuraCaspaGameView

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
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
            route = "join_table"
        ){
            JoinTable(
                onNavigateJoinToPuraCaspaGameView = { tableName ->
                    navController.navigate("puracaspa_game_view/$tableName")
                },modifier)
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