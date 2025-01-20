package org.alba.hortus.presentation.features.location.component

import androidx.compose.foundation.focusable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.*
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.baseline_close_24
import org.jetbrains.compose.resources.painterResource

@Composable
fun SearchInputField(
    modifier: Modifier = Modifier,
    inputText: String,
    onSearchInputChanged: (String) -> Unit,
    onClearInputClicked: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        value = inputText,
        onValueChange = { newInput -> onSearchInputChanged(newInput) },
        placeholder = {

        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .focusRequester(focusRequester)
            .focusable(true),
        trailingIcon = {
            if (inputText.isNotBlank()) {
                IconButton(onClick = {
                    onClearInputClicked.invoke()
                }) {
                    Icon(
                        painter = painterResource(Res.drawable.baseline_close_24),
                        contentDescription = "Clear"
                    )
                }
            }
        }
    )
}