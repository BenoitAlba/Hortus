package org.alba.hortus.presentation.features.login.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.password_label
import hortus.composeapp.generated.resources.repeat_password_label
import hortus.composeapp.generated.resources.username_label
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hortus.composeapp.generated.resources.bad_cred_message
import hortus.composeapp.generated.resources.baseline_arrow_back_ios_24
import hortus.composeapp.generated.resources.close_button_content_description
import hortus.composeapp.generated.resources.create_user
import hortus.composeapp.generated.resources.new_user_screen_title
import hortus.composeapp.generated.resources.sign_in_button_label
import hortus.composeapp.generated.resources.user_already_exist
import org.alba.hortus.presentation.components.InformationBox
import org.alba.hortus.presentation.components.ObserveAsEvents
import org.alba.hortus.presentation.components.ValidationTextField
import org.alba.hortus.presentation.features.home.HomeScreen
import org.jetbrains.compose.resources.painterResource

class SignUpScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<SignUpScreenViewModel>()
        val uiState = viewModel.loginUiState.collectAsState()
        val uiEffect = viewModel.uiEffect

        val navigator = LocalNavigator.currentOrThrow
        ObserveAsEvents(uiEffect) { event ->
            when (event) {
                is LoginScreenUIEffect.NavigateToHome -> {
                    navigator.replaceAll(HomeScreen())
                }
            }
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(Res.string.new_user_screen_title),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                        )
                    },
                    navigationIcon = {
                        Icon(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .clickable {
                                    navigator.pop()
                                },
                            painter = painterResource(Res.drawable.baseline_arrow_back_ios_24),
                            contentDescription = stringResource(Res.string.close_button_content_description)
                        )
                    },
                )
            },
        ) {
            AccountCreationScreen(viewModel, uiState.value, it)
        }
    }
}

@Composable
fun AccountCreationScreen(
    viewModel: SignUpScreenViewModel,
    uiState: LoginScreenUIState,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(
            top = paddingValues.calculateTopPadding() + 16.dp,
            bottom = paddingValues.calculateBottomPadding(),
            start = 16.dp,
            end = 16.dp,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var repeatPassword by remember { mutableStateOf("") }

        ValidationTextField(
            label = stringResource(Res.string.username_label),
            value = username,
            onValueChange = {
                username = it
                viewModel.isEmailValid(it)
            },
            isValid = uiState.isEmailValid,
            isEmail = true
        )

        ValidationTextField(
            label = stringResource(Res.string.password_label),
            value = password,
            onValueChange = {
                password = it
                viewModel.isPassWordValid(it)
            },
            isValid = uiState.isPasswordValid,
            isPassword = true
        )

        ValidationTextField(
            label = stringResource(Res.string.repeat_password_label),
            value = repeatPassword,
            onValueChange = {
                repeatPassword = it
                viewModel.isSecondPassWordValid(password, repeatPassword)
            },
            isValid = uiState.isConfirmationPasswordValid,
            isPassword = true
        )

        if (uiState.badCredentials) {
            InformationBox {
                Text(
                    style = MaterialTheme.typography.labelMedium,
                    text = stringResource(Res.string.bad_cred_message)
                )
            }
        } else if (uiState.userAlreadyExist) {
            InformationBox {
                Text(
                    style = MaterialTheme.typography.labelMedium,
                    text = stringResource(Res.string.user_already_exist)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility(uiState.userAlreadyExist) {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    if (
                        uiState.isEmailValid == true &&
                        uiState.isPasswordValid == true &&
                        uiState.isConfirmationPasswordValid == true
                    ) {
                        viewModel.connect(username, password)
                    }
                },
            ) {
                Text(stringResource(Res.string.sign_in_button_label))
            }
        }

        Button(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            onClick = {
                if (
                    uiState.isEmailValid == true &&
                    uiState.isPasswordValid == true &&
                    uiState.isConfirmationPasswordValid == true
                ) {
                    viewModel.createUserWithEmailAndPassword(username, password)
                }
            },
        ) {
            Text(stringResource(Res.string.create_user))
        }
    }
}