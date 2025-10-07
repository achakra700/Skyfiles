package skyfiles.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Core file operations: copy, move, delete, rename.
 * All operations are suspend functions for coroutine support.
 */
object FileOperations {
    suspend fun copy(source: File, dest: File): Unit = withContext(Dispatchers.IO) {
        if (source.isDirectory) {
            dest.mkdirs()
            source.listFiles()?.forEach { child: File ->
                copy(child, File(dest, child.name))
            }
        } else {
            source.copyTo(dest, overwrite = true)
        }
    }

    suspend fun move(source: File, dest: File) = withContext(Dispatchers.IO) {
        copy(source, dest)
        delete(source)
    }

    suspend fun delete(file: File): Unit = withContext(Dispatchers.IO) {
        if (file.isDirectory) {
            file.listFiles()?.forEach { child: File -> delete(child) }
        }
        file.delete()
    }

    suspend fun rename(file: File, newName: String): Boolean = withContext(Dispatchers.IO) {
        val newFile = File(file.parent, newName)
        file.renameTo(newFile)
    }
}
