package com.kdbrian.sage.ui.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kdbrian.sage.ui.theme.SageTheme

@Composable
fun RoundedInputField(
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    fieldState: TextFieldState = rememberTextFieldState(),
    onValueChange: (String) -> Unit = {},
    placeholder: String = "Placeholder",
    leadingIcon: (@Composable () -> Unit)? = null,
    keyBoardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    colors: TextFieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = Color.LightGray,
        unfocusedPlaceholderColor = Color.White,
        focusedPlaceholderColor = Color.White,
        focusedContainerColor = Color.White,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        cursorColor = Color.Black,
        unfocusedLeadingIconColor = Color.White,
        focusedLeadingIconColor = Color.White,
    )
) {

    TextField(
        value = fieldState.text.toString(),
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = singleLine,
        minLines = if(singleLine) 1 else 3,
        shape = RoundedCornerShape(12.dp),
        placeholder = {
            Text(text = placeholder, style = MaterialTheme.typography.labelMedium)
        },
        leadingIcon = leadingIcon,
        colors = colors,
        keyboardActions = keyBoardActions,
        keyboardOptions = keyboardOptions
    )
}

@Preview
@Composable
private fun RoundedInputFieldPrev() {
    SageTheme {
        RoundedInputField()
    }
}