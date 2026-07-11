package kdbrian.github.io.drag.domain.dto

data class DocumentUploadRequestDto(
    val documentName: String = "",
    val documentSummary: String = "",
    val documentSize: Float = 0f,
    val source: String = ""
)
