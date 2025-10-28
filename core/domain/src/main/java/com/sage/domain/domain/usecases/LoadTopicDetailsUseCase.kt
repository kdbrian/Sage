package com.sage.domain.domain.usecases

import com.sage.domain.domain.repo.DocumentRepo

class LoadTopicDetailsUseCase (
    private val documentRepo: DocumentRepo
) {

    suspend operator fun invoke(topicId: String) = documentRepo.loadDocumentsByTopic(topicId)
}