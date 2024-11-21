package io.github.junrdev.sage.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.junrdev.sage.domain.model.FirebaseUploadedDocument
import io.github.junrdev.sage.ui.theme.SageTheme


@Composable
fun DocumentPreview(
    modifier: Modifier = Modifier,
    firebaseUploadedDocument: FirebaseUploadedDocument = FirebaseUploadedDocument(
        title = "Lorem ipsum dolor sit amet",
        description = "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        summarizedWithAi = true,
    )
) {


}

@Preview
@Composable
private fun DocumentPreviewPrev() {
    SageTheme {
        DocumentPreview()
    }
}