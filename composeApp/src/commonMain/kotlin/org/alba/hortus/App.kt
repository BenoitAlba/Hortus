package org.alba.hortus

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.transitions.SlideTransition
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.permission_dialog_message
import hortus.composeapp.generated.resources.permission_dialog_negative_button
import hortus.composeapp.generated.resources.permission_dialog_positive_button
import hortus.composeapp.generated.resources.permission_dialog_title
import org.alba.hortus.presentation.components.AlertMessageDialog
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.alba.hortus.presentation.features.splash.SplashScreen
import org.alba.hortus.presentation.managers.permissions.PermissionCallback
import org.alba.hortus.presentation.managers.permissions.PermissionStatus
import org.alba.hortus.presentation.managers.permissions.PermissionType
import org.alba.hortus.presentation.managers.permissions.createPermissionsManager
import org.alba.hortus.ui.theme.AppTheme
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalVoyagerApi::class)
@Composable
@Preview
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean,
) {

    AppTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor
    ) {
        StartupAuthorisations()
        Navigator(
            disposeBehavior = NavigatorDisposeBehavior(disposeSteps = false),
            screen = SplashScreen()
        ) {
            SlideTransition(
                navigator = it,
                disposeScreenAfterTransitionEnd = true
            )
        }
    }
}

@Composable
private fun StartupAuthorisations() {
    var permissionRationalDialog by remember { mutableStateOf(false) }
    var launchSetting by remember { mutableStateOf(false) }

    if (permissionRationalDialog) {
        AlertMessageDialog(
            title = stringResource(Res.string.permission_dialog_title),
            message = stringResource(Res.string.permission_dialog_message),
            positiveButtonText = stringResource(Res.string.permission_dialog_positive_button),
            negativeButtonText = stringResource(Res.string.permission_dialog_negative_button),
            onPositiveClick = {
                permissionRationalDialog = false
                launchSetting = true
            },
            onNegativeClick = {
                permissionRationalDialog = false
            }
        )
    }

    val permissionsManager = createPermissionsManager(
        object : PermissionCallback {
            override fun onPermissionStatus(
                permissionType: PermissionType,
                status: PermissionStatus
            ) {
                when (status) {
                    PermissionStatus.GRANTED -> {
                        when (permissionType) {
                            PermissionType.ACCESS_FINE_LOCATION -> {
                                println("---> ACCESS_FINE_LOCATION permission granted")
                                // get location
                            }

                            PermissionType.ACCESS_COARSE_LOCATION -> {
                                println("---> ACCESS_COARSE_LOCATION permission granted")
                                // get location
                            }

                            else -> {
                                // nothing to do
                            }
                        }
                    }

                    else -> {
                        permissionRationalDialog = true
                    }
                }
            }
        }
    )

    if (
        permissionsManager.isPermissionGranted(PermissionType.ACCESS_FINE_LOCATION)
        || permissionsManager.isPermissionGranted(PermissionType.ACCESS_COARSE_LOCATION)
    ) {
        // get location
    } else {
        permissionsManager.askPermission(PermissionType.ACCESS_FINE_LOCATION)
    }

    if (launchSetting) {
        permissionsManager.launchSettings()
        launchSetting = false
    }
}