package kdbrian.github.io.drag.data.impl

import kdbrian.github.io.drag.domain.service.FileStorageService
import kdbrian.github.io.drag.util.FileStoragePathHelper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileStorageServiceImpl(
    @Value("\${files.upload.dir}")
    private val uploadRoot : String
) : FileStorageService {
    override fun saveFile(
        file: MultipartFile,
        timeMillis: Long
    ): String {

        val dir = Paths.get(uploadRoot)
            .resolve(FileStoragePathHelper.resolveDirectory(timeMillis))

        Files.createDirectories(dir)

        val filename = generateSafeFilename(file.originalFilename, timeMillis)

        val targetPath = dir.resolve(filename)

        file.inputStream.use { input ->
            Files.copy(
                input,
                targetPath,
                StandardCopyOption.REPLACE_EXISTING
            )
        }

        return FileStoragePathHelper.toRelativePath(timeMillis, filename)
    }

    private fun generateSafeFilename(original: String?, timeMillis: Long): String {
        val clean = (original ?: "file")
            .replace("\\s+".toRegex(), "_")

        return "${timeMillis}_${clean}"
    }
}