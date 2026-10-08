package uz.devicecontrol.domain.repository

import uz.devicecontrol.domain.model.FileEntry

/**
 * Fayllar faqat ilovaning o'z katalogida (scoped storage) boshqariladi —
 * boshqa ilovalarning fayllariga tegilmaydi.
 */
interface FileRepository {
    fun rootPath(): String
    fun list(path: String): List<FileEntry>
    fun createFile(path: String, name: String, content: String): Boolean
    fun createDir(path: String, name: String): Boolean
    fun delete(path: String): Boolean
}
