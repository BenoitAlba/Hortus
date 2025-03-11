package org.alba.hortus.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.baseline_visibility_24
import hortus.composeapp.generated.resources.baseline_visibility_off_24
import hortus.composeapp.generated.resources.outline_cancel_24
import hortus.composeapp.generated.resources.outline_check_circle_24
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ValidationTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isValid: Boolean? = null,
    isPassword: Boolean = false,
    isEmail: Boolean = false,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            val tint = when (isValid) {
                true -> MaterialTheme.colorScheme.tertiary
                false -> MaterialTheme.colorScheme.error
                null -> MaterialTheme.colorScheme.primary
            }

            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(
                            if (passwordVisible) {
                                Res.drawable.baseline_visibility_24
                            } else {
                                Res.drawable.baseline_visibility_off_24
                            }
                        ),
                        contentDescription = null,
                        tint = tint
                    )
                }
            } else {
                val painterResource: DrawableResource? =
                    when (isValid) {
                        true -> Res.drawable.outline_check_circle_24
                        false -> Res.drawable.outline_cancel_24
                        else -> null
                    }
                painterResource?.let {
                    Icon(
                        painter = painterResource(painterResource),
                        contentDescription = null,
                        tint = tint
                    )
                }
            }
        },
        isError = isValid == false,
        visualTransformation = if (isPassword) {
            if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            }
        } else if (isEmail) {
            LowerCaseTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = if (isPassword) {
            KeyboardOptions(keyboardType = KeyboardType.Password)
        } else {
            KeyboardOptions.Default
        }
    )
}

class LowerCaseTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString(text.text.lowercase()),
            offsetMapping = OffsetMapping.Identity
        )
    }
}