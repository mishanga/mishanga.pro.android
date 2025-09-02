package com.yandex.android.mishanga.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.yandex.android.mishanga.ui.theme.BackgroundAccent
import com.yandex.android.mishanga.ui.theme.BackgroundPrimary
import com.yandex.android.mishanga.ui.theme.BackgroundSecondary

@Composable
fun CustomBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BackgroundPrimary,
                        BackgroundSecondary,
                        BackgroundAccent
                    )
                )
            )
    ) {
        // Content
        content()
    }
}
