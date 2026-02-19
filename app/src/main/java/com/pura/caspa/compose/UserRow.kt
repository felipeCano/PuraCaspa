package com.pura.caspa.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun UserRow(name: String, isSelected: Boolean) {
    val backgroundColor = if (isSelected) Color.Black else Color.Transparent
    val contentColor = if (isSelected) Color.White else Color.Black
    val borderColor = Color.Black

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = CircleShape,
        color = backgroundColor,
        border = BorderStroke(2.dp, borderColor)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Person,
                contentDescription = null,
                tint = if (isSelected) Color.Green else contentColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = name,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = contentColor,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
            )
        }
    }
}