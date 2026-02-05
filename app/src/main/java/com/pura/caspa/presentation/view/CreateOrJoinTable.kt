package com.pura.caspa.presentation.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pura.caspa.R
import com.pura.caspa.compose.PuraCaspaButton
import com.pura.caspa.compose.TitleFrame
import com.pura.caspa.presentation.viewmodel.UserNameViewModel

@Composable
fun CreateOrJoinTable(
    onNavigateToConfirmTableCreation: () -> Unit,
    onNavigatetoJoinTable: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserNameViewModel = hiltViewModel()
) {
    val nameState by viewModel.nameState.collectAsState()

    TitleFrame("Welcome to", "Pura Caspa") { paddongValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddongValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.name_player),
                    color = Color.LightGray,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 8.dp)
                )

                TextField(
                    value = nameState,
                    onValueChange = { input ->
                        viewModel.onNameChange(input)
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.name_suggestion),
                            color = Color.Gray
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(
                            1.dp,
                            colorResource(id = R.color.gold_border),
                            RoundedCornerShape(12.dp)
                        ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorResource(id = R.color.input_background),
                        unfocusedContainerColor = colorResource(id = R.color.input_background),
                        disabledContainerColor = colorResource(id = R.color.input_background),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.weight(3f))
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    //.fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PuraCaspaButton(
                    text = stringResource(id = R.string.create_table),
                    enabled = true,
                    onClick = {
                        viewModel.saveName()
                        onNavigateToConfirmTableCreation()
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                PuraCaspaButton(
                    text = stringResource(id = R.string.join_the_room),
                    enabled = true,
                    onClick = {
                        viewModel.saveName()
                        onNavigatetoJoinTable()
                    }
                )
            }
        }
    }

}