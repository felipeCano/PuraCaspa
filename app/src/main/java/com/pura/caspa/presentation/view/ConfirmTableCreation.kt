package com.pura.caspa.presentation.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pura.caspa.R
import com.pura.caspa.compose.PuraCaspaButton
import com.pura.caspa.compose.TitleFrame
import com.pura.caspa.compose.textFieldColors
import com.pura.caspa.data.util.Resource
import com.pura.caspa.presentation.viewmodel.ConfirmTableCreationViewModel

@Composable
fun ConfirmTableCreation(
    onNavigateToPuraCaspaGameView: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ConfirmTableCreationViewModel = hiltViewModel()
) {
    var idTableCreation by remember { mutableStateOf("") }
    val state by viewModel.createPartyState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state) {
        if (state is Resource.Success) {
            onNavigateToPuraCaspaGameView(idTableCreation)
            viewModel.resetState()
        }
    }

    TitleFrame(stringResource(R.string.create_table)) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.enter_the_room_name),
                    color = Color.LightGray,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 8.dp)
                )

                TextField(
                    value = idTableCreation,
                    onValueChange = { input ->
                        val cleanText = input.filter { !it.isWhitespace() }
                        idTableCreation = cleanText
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(
                            1.dp,
                            colorResource(id = R.color.gold_border),
                            RoundedCornerShape(12.dp)
                        ),
                    colors = textFieldColors(),
                    shape = RoundedCornerShape(12.dp)
                )
                state?.let { currentState ->
                    when (currentState) {
                        is Resource.Loading -> {
                            CircularProgressIndicator(color = colorResource(R.color.gold_border))
                        }

                        is Resource.Error -> {
                            val errorText = currentState.message?.asString(context) ?: ""

                            Text(errorText.ifEmpty { "Error desconocido" }, color = Color.Red)
                        }

                        is Resource.Success -> {
                            Text(stringResource(R.string.room_created_successfully, "'$idTableCreation'"), color = Color.Green)
                        }

                        is Resource.Idle -> {}
                    }
                }
            }
            Column(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PuraCaspaButton(
                    text = stringResource(id = R.string.confirm_table_creation),
                    enabled = true,
                    onClick = {
                        viewModel.createNewRoom(idTableCreation)
                    }
                )
            }
        }
    }

}