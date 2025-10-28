package com.sage.domain.domain.usecases

import com.sage.core.FlowUseCase
import com.sage.core.Result
import com.sage.domain.domain.model.DocumentModel
import com.sage.domain.domain.repo.DocumentRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed class DocumentListErrors {

}

sealed class DocumentListSuccess {
    data class Documents(val documentModel: List<DocumentModel?>) : DocumentListSuccess()
}

class ListDocumentsUseCase(
    val documentRepo: DocumentRepo,
) : FlowUseCase<Unit, DocumentListSuccess, DocumentListErrors>(dispatcher = Dispatchers.IO) {
//    operator fun invoke(): Flow<List<DocumentModel?>> = flow {
//        documentRepo.loadDocumentsAll().onSuccess { documentsAll ->
//            emit(documentsAll)
//        }
//    }

    override fun execute(parameters: Unit): Flow<Result<DocumentListSuccess, DocumentListErrors>> =
        flow {
            emit(Result.Loading)

            documentRepo
                .loadDocumentsAll()
                .onSuccess { documentsAll ->
                    emit(Result.Success(DocumentListSuccess.Documents(documentsAll)))
                }


        }
}