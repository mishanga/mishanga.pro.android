package com.yandex.android.mishanga.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun RandomMiniImage(
    modifier: Modifier = Modifier,
    size: Int = 48,
    alpha: Float = 0.15f,
    rotation: Float = 0f
) {
    val context = LocalContext.current

    // List of all available mini images
    val miniImages = remember {
        listOf(
            "cup_icon_min",
            "rounded_arrow_icon_min",
            "lightning_icon_min",
            "pin_icon_min",
            "heart_icon_min",
            "asterisk_icon_min",
            "gampad_icon_min",
            "vilka_icon_min",
            "box_with_a_gift_icon_min",
            "ruka_icon_min",
            "smileik_icon_min",
            "arrow_icon_min",
            "planet_icon_min"
        )
    }

    // Get random image resource ID
    val randomImageName = remember { miniImages.random() }
    val resourceId = remember {
        try {
            context.resources.getIdentifier(
                randomImageName,
                "drawable",
                context.packageName
            )
        } catch (e: Exception) {
            // Fallback to first image if there's an error
            context.resources.getIdentifier(
                miniImages.first(),
                "drawable",
                context.packageName
            )
        }
    }

    if (resourceId != 0) {
        Box(
            modifier = modifier
                .size(size.dp)
                .alpha(alpha)
                .rotate(rotation)
        ) {
            Image(
                painter = painterResource(id = resourceId),
                contentDescription = "Random mini image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}
