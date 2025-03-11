package org.alba.hortus.presentation.features.login.signin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.bad_cred_message
import hortus.composeapp.generated.resources.baseline_arrow_back_ios_24
import hortus.composeapp.generated.resources.close_button_content_description
import hortus.composeapp.generated.resources.email_password_required
import hortus.composeapp.generated.resources.email_required
import hortus.composeapp.generated.resources.email_sent_message
import hortus.composeapp.generated.resources.loading_button
import hortus.composeapp.generated.resources.password_label
import hortus.composeapp.generated.resources.reset_password_label
import hortus.composeapp.generated.resources.sign_in_button_label
import hortus.composeapp.generated.resources.username_label
import kotlinx.coroutines.launch
import org.alba.hortus.presentation.components.InformationBox
import org.alba.hortus.presentation.components.ObserveAsEvents
import org.alba.hortus.presentation.components.TertiaryButton
import org.alba.hortus.presentation.components.ValidationTextField
import org.alba.hortus.presentation.features.home.HomeScreen
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class SignInScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val snackBarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
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

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(Res.string.loading_button),
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
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            },
        ) {
            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = it.calculateTopPadding() + 16.dp,
                        bottom = it.calculateBottomPadding(),
                        start = 16.dp,
                        end = 16.dp,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                var username by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                ValidationTextField(
                    label = stringResource(Res.string.username_label),
                    value = username,
                    onValueChange = { value ->
                        username = value
                    },
                    isEmail = true
                )

                ValidationTextField(
                    label = stringResource(Res.string.password_label),
                    value = password,
                    onValueChange = { value ->
                        password = value
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

                TertiaryButton(
                    text = stringResource(Res.string.reset_password_label),
                    onClick = {
                        if (username.isNotBlank()) {
                            viewModel.resetPassword(username)
                            coroutineScope.launch {
                                snackBarHostState.showSnackbar(message = getString(Res.string.email_sent_message))
                            }
                        } else {
                            coroutineScope.launch {
                                snackBarHostState.showSnackbar(message = getString(Res.string.email_required))
                            }
                        }
                    }
                )

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
                        } else {
                            coroutineScope.launch {
                                snackBarHostState.showSnackbar(message = getString(Res.string.email_password_required))
                            }
                        }
                    },
                ) {
                    Text(stringResource(Res.string.sign_in_button_label))
                }
            }
        }
    }
}