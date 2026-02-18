package com.pura.caspa.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.pura.caspa.R

@Composable
fun BlueCircularProgress(
     modifier: Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(enabled = false) { },
        contentAlignment = Alignment.Center
    ) {
    val circularProgressSize = 120.dp
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(circularProgressSize)
    ) {
        CircularProgressIndicator(
            modifier.fillMaxWidth(),
            strokeWidth = 8.dp,
            color = colorResource(R.color.blue)
        )
    }
}
}