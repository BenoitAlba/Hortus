package org.alba.hortus.presentation.features.login.choice

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.hortus2
import org.jetbrains.compose.resources.painterResource
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hortus.composeapp.generated.resources.new_user_button_label
import hortus.composeapp.generated.resources.sign_in_button_label
import org.alba.hortus.presentation.features.login.signin.SignInScreen
import org.alba.hortus.presentation.features.login.signup.SignUpScreen
import org.alba.hortus.presentation.utils.safeNavigate
import org.jetbrains.compose.resources.stringResource

class LoginOrSignUpScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    ),
                painter = painterResource(
                    Res.drawable.hortus2
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        navigator.safeNavigate(coroutineScope, SignUpScreen())
                    },
                ) {
                    Text(stringResource(Res.string.new_user_button_label))
                }

                Button(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    onClick = {
                        navigator.safeNavigate(coroutineScope, SignInScreen())
                    },
                ) {
                    Text(stringResource(Res.string.sign_in_button_label))
                }
            }
        }
    }
}