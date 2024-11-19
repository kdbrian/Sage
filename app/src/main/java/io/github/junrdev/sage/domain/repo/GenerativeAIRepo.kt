package io.github.junrdev.sage.domain.repo

interface GenerativeAIRepo  {
    suspend fun generateContent(prompt : String) : Result<String>
}