package com.pura.caspa.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pura.caspa.data.model.Player

@Composable
fun RacerResultRow(player: Player, position: Int) {
    val (backgroundBrush, textColor) = when (position) {
        1 -> Brush.horizontalGradient(listOf(Color(0xFFFFD700), Color(0xFFDAA520))) to Color.White
        2 -> Brush.horizontalGradient(listOf(Color(0xFFC0C0C0), Color(0xFF8E8E8E))) to Color.White
        3 -> Brush.horizontalGradient(listOf(Color(0xFFCD7F32), Color(0xFF8B4513))) to Color.White
        else -> Brush.horizontalGradient(listOf(Color(0xFF1B263B), Color(0xFF1B263B))) to Color.LightGray
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (position <= 3) 6.dp else 0.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .background(backgroundBrush)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${position}º",
                style = TextStyle(
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = textColor
                ),
                modifier = Modifier.width(40.dp)
            )

            Text(
                text = player.name,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = textColor
                ),
                modifier = Modifier.weight(1f)
            )

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${player.votes}",
                    style = TextStyle(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = if (position <= 3) Color.White else Color(0xFFE0E1DD)
                    )
                )
                Text(
                    text = "VOTOS",
                    style = TextStyle(fontSize = 10.sp, color = textColor.copy(alpha = 0.7f))
                )
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "RESULTADOS DE VOTACIÓN",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700), // Dorado
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "¡RONDA FINALIZADA!",
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Color.Red,
                shadow = Shadow(
                    color = Color(0xFFE91E63),
                    offset = Offset(5f, 5f),
                    blurRadius = 2f
                )
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

    }
}