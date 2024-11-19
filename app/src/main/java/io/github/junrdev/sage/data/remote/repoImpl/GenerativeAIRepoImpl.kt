package io.github.junrdev.sage.data.remote.repoImpl

import com.google.ai.client.generativeai.GenerativeModel
import io.github.junrdev.sage.domain.repo.GenerativeAIRepo

class GenerativeAIRepoImpl(
    val generativeModel: GenerativeModel
) : GenerativeAIRepo {

    override suspend fun generateContent(prompt: String): Result<String> {
        return try {
            val feedback = generativeModel.generateContent(prompt)
            Result.success(feedback.text ?: "Something went wrong")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}