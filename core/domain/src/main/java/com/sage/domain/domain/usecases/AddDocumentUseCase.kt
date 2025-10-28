package com.sage.domain.domain.usecases

import android.net.Uri
import com.sage.domain.domain.model.DocumentModel
import com.sage.domain.domain.repo.DocumentRepo

class AddDocumentUseCase(
    private val documentRepo: DocumentRepo
) {

    suspend operator fun invoke(
        uri: Uri,
        documentModel: DocumentModel
    )= documentRepo.saveDocumentMetaData(
        uri = uri,
        documentModel = documentModel
    )

}