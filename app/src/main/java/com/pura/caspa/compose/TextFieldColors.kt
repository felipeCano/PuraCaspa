package com.pura.caspa.compose

import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.pura.caspa.R

@Composable
fun textFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = colorResource(id = R.color.input_background),
    unfocusedContainerColor = colorResource(id = R.color.input_background),
    disabledContainerColor = colorResource(id = R.color.input_background),
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White
)