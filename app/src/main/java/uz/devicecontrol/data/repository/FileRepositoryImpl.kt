package uz.devicecontrol.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.devicecontrol.domain.model.FileEntry
import uz.devicecontrol.domain.repository.FileRepository
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Faqat ilovaning tashqi fayllar katalogi (scoped storage) ichida ishlaydi.
 * Bu Android'da ruxsatsiz, xavfsiz hudud — boshqa ilovalarning fayllariga tegilmaydi.
 */
@Singleton
class FileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : FileRepository {

    private val root: File = (context.getExternalFilesDir(null) ?: context.filesDir).also {
        if (!it.exists()) it.mkdirs()
    }

    override fun rootPath(): String = root.absolutePath

    override fun list(path: String): List<FileEntry> {
        val dir = resolveSafe(path) ?: return emptyList()
        if (!dir.isDirectory) return emptyList()
        return dir.listFiles()?.map { it.toEntry() }
            ?.sortedWith(compareByDescending<FileEntry> { it.isDirectory }.thenBy { it.name.lowercase() })
            ?: emptyList()
    }

    override fun createFile(path: String, name: String, content: String): Boolean {
        val dir = resolveSafe(path) ?: return false
        if (!name.isValidName()) return false
        return try {
            File(dir, name).writeText(content)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun createDir(path: String, name: String): Boolean {
        val dir = resolveSafe(path) ?: return false
        if (!name.isValidName()) return false
        return File(dir, name).mkdirs()
    }

    override fun delete(path: String): Boolean {
        val file = resolveSafe(path) ?: return false
        if (file == root) return false
        return file.deleteRecursively()
    }

    /** Katalog chegarasidan chiqib ketishning (path traversal) oldini oladi. */
    private fun resolveSafe(path: String): File? {
        val target = File(path).canonicalFile
        val base = root.canonicalFile
        return if (target == base || target.path.startsWith(base.path + File.separator)) target else null
    }

    private fun String.isValidName() = isNotBlank() && !contains("/") && !contains("..")

    private fun File.toEntry() = FileEntry(
        name = name,
        path = absolutePath,
        isDirectory = isDirectory,
        sizeBytes = if (isFile) length() else 0L,
    )
}
