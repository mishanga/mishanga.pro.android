package pro.mishanga.android.ui.component

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.yandex.android.mishanga.data.AdSettings
import com.yandex.android.mishanga.data.SettingsStore
import pro.mishanga.android.ui.theme.Black
import pro.mishanga.android.ui.theme.BrandPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    topBar: @Composable (title: String, onBack: () -> Unit) -> Unit
) {
    val context = LocalContext.current
    val store = remember { SettingsStore.get(context) }
    var settings by remember { mutableStateOf(store.read()) }

    fun save() {
        store.write(settings)
    }

    fun clear() {
        settings = AdSettings(); store.clear()
    }

    LaunchedEffect(settings) { save() }

    Scaffold(
        topBar = { topBar("settings", onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Поле для загрузки рекламы
            OutlinedTextField(
                value = settings.aimBannerId,
                onValueChange = { settings = settings.copy(aimBannerId = it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("aim banner id") }
            )
            Spacer(Modifier.size(8.dp))
            // Поле как request params в запросе рекламы
            OutlinedTextField(
                value = settings.regionId,
                onValueChange = { settings = settings.copy(regionId = it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("region id") }
            )
            Spacer(Modifier.size(8.dp))
            // Поле как request params в запросе рекламы
            OutlinedTextField(
                value = settings.uuid,
                onValueChange = { settings = settings.copy(uuid = it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("uuid") }
            )
            Spacer(Modifier.size(8.dp))
            // Поле для загрузки рекламы
            OutlinedTextField(
                value = settings.adUnitId,
                onValueChange = { settings = settings.copy(adUnitId = it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("ad unit id") }
            )
            Spacer(Modifier.size(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    clear()
                    Toast.makeText(context, "Cleared", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandPrimary,
                    contentColor = Black
                ),
                border = BorderStroke(2.dp, Black),
                shape = RoundedCornerShape(12.dp)
            ) { Text("clear") }
        }
    }
}



