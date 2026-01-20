package com.pura.caspa.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pura.caspa.R

@Composable
fun TitleFrame(){
    Surface(
        color = colorResource(id = R.color.gold_border),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(2.dp, Color(0xFF8B5E3C))
    ) {
        Text(
            text = stringResource(id= R.string.welcome_to_puracaspa),
            modifier = Modifier.padding(horizontal = 38.dp, vertical = 8.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

    }
}