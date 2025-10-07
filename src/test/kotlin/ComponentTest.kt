package skyfiles.test

import skyfiles.core.FileModel
import skyfiles.core.TrashManager
import skyfiles.core.SettingsManager
import java.io.File

/**
 * Simple test to verify all components are accessible.
 */
fun main() {
    println("Testing SkyFiles components...")
    
    // Test FileModel
    val testFile = File(System.getProperty("user.home"))
    val fileModel = FileModel.from(testFile)
    println("FileModel test: ${fileModel.name}")
    
    // Test SettingsManager
    val settings = SettingsManager.loadSettings()
    println("Settings test: ${settings.theme}")
    
    // Test TrashManager
    val trashItems = TrashManager.listTrash()
    println("TrashManager test: ${trashItems.size} items in trash")
    
    println("All tests passed! ✅")
}

