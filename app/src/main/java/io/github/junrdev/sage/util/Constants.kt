package io.github.junrdev.sage.util

object Constants {

    //gen ai

    val MODEL_NAME: String = "gemini-1.5-flash"


    val DOCUMENT_SUMMARY_PROMPT = """
        Read the provided document and identify its main topic along with a brief description summarizing its key content. 
        Ensure the response is concise, relevant, and avoids unnecessary details. also do not provide the feedback as markup, 
        avoid repeating the document or stating anything that involves pointing out the document with an exception of the document name. 
        Dont make the description (minimum 300 word) so brief use the documents content to come up with a description that is just enough :
    """.trimIndent()


    val dateFormat = "yyyy-mm-dd"

    val DOCUMENT_TOPIC_CATEGORIZING_PROMPT = """
        Analyze the content of the provided document and determine its primary category based on its topic. 
        Provide reasoning or key points supporting your classification. 
        get the feedback in key/value pairs where the key is the topic/category and the value is the supporting text, 
        make the generated topics fully based on the apps relevant contents and the descriptions briefs to maximum of 100 words
        focus mainly on categorizing the contents. provide no markdown outputs
        Input document: 
    """.trimIndent()

}