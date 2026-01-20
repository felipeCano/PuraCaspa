package com.pura.caspa.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pura.caspa.presentation.view.CreateOrJoinTable
import com.pura.caspa.presentation.view.ConfirmTableCreation
import androidx.compose.ui.Modifier

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
            },modifier)

        }
        composable (route = "confirm_table_creation"){
            ConfirmTableCreation(modifier)
        }
    }
}