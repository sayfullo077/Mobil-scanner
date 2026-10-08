package uz.devicecontrol.presentation.files

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uz.devicecontrol.domain.model.FileEntry
import uz.devicecontrol.domain.repository.FileRepository
import javax.inject.Inject

data class FilesUiState(
    val currentPath: String = "",
    val rootPath: String = "",
    val entries: List<FileEntry> = emptyList(),
)

@HiltViewModel
class FilesViewModel @Inject constructor(
    private val repository: FileRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(FilesUiState())
    val state: StateFlow<FilesUiState> = _state.asStateFlow()

    init {
        val root = repository.rootPath()
        _state.value = FilesUiState(currentPath = root, rootPath = root)
        refresh()
    }

    fun refresh() {
        _state.value = _state.value.copy(entries = repository.list(_state.value.currentPath))
    }

    fun open(entry: FileEntry) {
        if (entry.isDirectory) {
            _state.value = _state.value.copy(currentPath = entry.path)
            refresh()
        }
    }

    fun goUp() {
        val current = _state.value.currentPath
        if (current == _state.value.rootPath) return
        val parent = java.io.File(current).parent ?: return
        _state.value = _state.value.copy(currentPath = parent)
        refresh()
    }

    fun createFile(name: String) {
        repository.createFile(_state.value.currentPath, name, "DeviceControl tomonidan yaratilgan namuna fayl.\n")
        refresh()
    }

    fun createDir(name: String) {
        repository.createDir(_state.value.currentPath, name)
        refresh()
    }

    fun delete(entry: FileEntry) {
        repository.delete(entry.path)
        refresh()
    }
}
