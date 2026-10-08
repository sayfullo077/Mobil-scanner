package uz.devicecontrol.presentation.apps

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.devicecontrol.domain.model.InstalledApp

@Composable
fun AppsScreen(viewModel: AppsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)) {
            FilterChip(
                selected = state.includeSystem,
                onClick = { viewModel.toggleSystem() },
                label = { Text("Tizim ilovalarini ko'rsatish") },
            )
        }
        Text(
            "Jami: ${state.apps.size} ta ilova",
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        if (state.loading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
        LazyColumn {
            items(state.apps) { app -> AppRow(app) { viewModel.launch(app.packageName) } }
        }
    }
}

@Composable
private fun AppRow(app: InstalledApp, onClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        Text(app.label, fontWeight = FontWeight.Bold)
        Text(app.packageName)
        Text("Ruxsatlar: ${app.requestedPermissions.size} ta")
        if (expanded) {
            app.requestedPermissions.forEach { perm ->
                Text("• ${perm.substringAfterLast('.')}", modifier = Modifier.padding(start = 8.dp))
            }
            Text(
                "Ochish uchun bosing →",
                modifier = Modifier.padding(top = 4.dp).clickable { onClick() },
            )
        }
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}
