package org.alba.hortus.presentation.features.login.usecase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.auth

class SignInUseCase {
    suspend operator fun invoke(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            val auth = Firebase.auth
            auth.signInWithEmailAndPassword(email, password)
            if (auth.currentUser != null) {
                onSuccess()
            } else {
                onError(Exception("Authentication failed"))
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            onError(e)
        }
    }
}