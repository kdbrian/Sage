package kdbrian.github.io.drag.domain.service

import org.springframework.web.multipart.MultipartFile

interface FileStorageService {
    fun saveFile(file: MultipartFile, timeMillis: Long = System.currentTimeMillis()) : String
}