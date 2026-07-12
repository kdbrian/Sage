package kdbrian.github.io.drag.util

import java.nio.file.Path
import java.nio.file.Paths
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

object FileStoragePathHelper {

    private val ZONE: ZoneId = ZoneId.systemDefault()

    /**
     * Builds a directory path like 2025/JAN, relative to the configured
     * upload root (files.upload.dir already names the "uploads" folder
     * itself -- prepending another literal "uploads" here double-nests it).
     */
    fun resolveDirectory(timeMillis: Long): Path {
        val dateTime = Instant.ofEpochMilli(timeMillis).atZone(ZONE)

        val year = dateTime.year.toString()

        val month = dateTime.month
            .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            .uppercase(Locale.ENGLISH)

        return Paths.get(year, month)
    }

    /**
     * Full file path inside versioned folder
     */
    fun resolveFilePath(timeMillis: Long, filename: String): Path {
        return resolveDirectory(timeMillis).resolve(filename)
    }

    /**
     * Store-friendly relative path
     */
    fun toRelativePath(timeMillis: Long, filename: String): String {
        return resolveFilePath(timeMillis, filename)
            .toString()
            .replace("\\", "/")

    }
}