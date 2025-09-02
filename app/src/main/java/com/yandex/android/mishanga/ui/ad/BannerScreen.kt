package com.yandex.android.mishanga.ui.ad

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yandex.android.mishanga.ui.components.CustomBackground
import com.yandex.android.mishanga.ui.components.PrimaryButton

@Composable
fun BannerScreen(
    onBack: () -> Unit,
    onNavigateToDetail: (name: String, width: Int, height: Int) -> Unit,
    topBar: @Composable (title: String, onBack: () -> Unit) -> Unit
) {
    CustomBackground {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            topBar("banner", onBack)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top action buttons
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    PrimaryButton(
                        text = "Load banner 300x250",
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) { onNavigateToDetail("Banner 300x250", 300, 250) }
                    Spacer(Modifier.height(8.dp))
                    PrimaryButton(
                        text = "Load banner 320x50",
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) { onNavigateToDetail("Banner 320x50", 320, 50) }
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}


