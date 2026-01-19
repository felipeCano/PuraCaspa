package com.pura.caspa.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BackgroundApp() {
    val glowColor = Color(0xFF00E5FF)
    val borderColor = Color(0xFF00B0FF).copy(alpha = 0.4f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020202)) // Negro casi total
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        glowColor.copy(alpha = 0.4f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.35f, size.height * 0.28f),
                    radius = size.width * 0.8f
                ),
                center = Offset(size.width * 0.35f, size.height * 0.28f),
                radius = size.width * 0.8f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BlueGlassBox(modifier = Modifier.fillMaxWidth().height(180.dp), borderColor)

            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    BlueGlassBox(modifier = Modifier.fillMaxWidth().height(140.dp), borderColor)
                    BlueGlassBox(modifier = Modifier.fillMaxWidth().height(110.dp), borderColor)
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BlueGlassBox(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.65f), borderColor)
                    BlueGlassBox(modifier = Modifier.fillMaxWidth().weight(1f), borderColor)
                }
            }
        }
    }
}

@Composable
fun BlueGlassBox(modifier: Modifier = Modifier, borderColor: Color) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.02f))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        borderColor,
                        Color.Transparent,
                        borderColor.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(28.dp)
            )
    )
}