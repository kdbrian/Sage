package kdbrian.github.io.drag.domain.dto

data class DocumentDto(
    val title: String = "",
    val categories: MutableList<DocumentCategoryDto> = mutableListOf(),
    val summary: String = "",
    val source: String = "",
    val authoringSource: String = "",
    val aiSummary: String = "",
)