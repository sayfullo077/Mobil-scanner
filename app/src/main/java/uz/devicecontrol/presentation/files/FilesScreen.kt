package uz.devicecontrol.presentation.files

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import uz.devicecontrol.presentation.common.formatBytes

@Composable
fun FilesScreen(viewModel: FilesViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var newName by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Ilova fayllari (xavfsiz hudud)", fontWeight = FontWeight.Bold)
        Text(
            state.currentPath.removePrefix(state.rootPath).ifEmpty { "/" },
            modifier = Modifier.padding(vertical = 4.dp),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = { viewModel.goUp() },
                enabled = state.currentPath != state.rootPath,
            ) { Text("↑ Yuqoriga") }
        }

        OutlinedTextField(
            value = newName,
            onValueChange = { newName = it },
            label = { Text("Yangi fayl/papka nomi") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                if (newName.isNotBlank()) { viewModel.createFile(newName); newName = "" }
            }) { Text("Fayl yaratish") }
            OutlinedButton(onClick = {
                if (newName.isNotBlank()) { viewModel.createDir(newName); newName = "" }
            }) { Text("Papka yaratish") }
        }

        LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
            items(state.entries) { entry ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.open(entry) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            (if (entry.isDirectory) "📁 " else "📄 ") + entry.name,
                            fontWeight = FontWeight.Medium,
                        )
                        if (!entry.isDirectory) Text(formatBytes(entry.sizeBytes))
                    }
                    IconButton(onClick = { viewModel.delete(entry) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "O'chirish")
                    }
                }
            }
        }
    }
}
