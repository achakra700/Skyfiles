package skyfiles.core

import org.slf4j.LoggerFactory
import java.io.File

/**
 * A centralized logger for the SkyFiles application.
 * It configures the logging system to write to a file in the app's data directory.
 */
object AppLogger {
    fun setup() {
        // Set the property for the logback configuration
        val dataDir = System.getenv("APPDATA") ?: System.getProperty("user.home")
        val logDir = File(dataDir, "SkyFiles").absolutePath
        System.setProperty("app.data.dir", logDir)

        // The SLF4J binding will automatically find and use logback.xml
        val logger = LoggerFactory.getLogger(AppLogger::class.java)
        logger.info("SkyFiles application starting. Logging initialized.")
    }

    /**
     * Returns a logger instance for the given class.
     */
    fun getLogger(clazz: Class<*>) = LoggerFactory.getLogger(clazz)
}
