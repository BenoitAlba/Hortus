package org.alba.hortus.presentation.features.login.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.bad_cred_message
import hortus.composeapp.generated.resources.password_label
import hortus.composeapp.generated.resources.sign_in_button_label
import hortus.composeapp.generated.resources.username_label
import org.alba.hortus.presentation.components.InformationBox
import org.alba.hortus.presentation.components.ObserveAsEvents
import org.alba.hortus.presentation.components.ValidationTextField
import org.alba.hortus.presentation.features.home.HomeScreen
import org.jetbrains.compose.resources.stringResource

class SignInScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<SignInScreenViewModel>()
        val uiState = viewModel.signInUiState.collectAsState().value
        val uiEffect = viewModel.uiEffect
        val navigator = LocalNavigator.currentOrThrow
        ObserveAsEvents(uiEffect) { event ->
            when (event) {
                is SignInScreenUIEffect.NavigateToHome -> {
                    navigator.replaceAll(HomeScreen())
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            var username by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            ValidationTextField(
                label = stringResource(Res.string.username_label),
                value = username,
                onValueChange = {
                    username = it
                },
                isEmail = true
            )

            ValidationTextField(
                label = stringResource(Res.string.password_label),
                value = password,
                onValueChange = {
                    password = it
                },
                isPassword = true
            )

            if (uiState.badCredentials) {
                InformationBox {
                    Text(
                        style = MaterialTheme.typography.labelMedium,
                        text = stringResource(Res.string.bad_cred_message)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                onClick = {
                    if (
                        username.isNotBlank() &&
                        password.isNotBlank()
                    ) {
                        viewModel.connect(username, password)
                    }
                },
            ) {
                Text(stringResource(Res.string.sign_in_button_label))
            }
        }
    }
}