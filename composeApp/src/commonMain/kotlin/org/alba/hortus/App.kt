package org.alba.hortus

import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import org.alba.hortus.presentation.components.AlertMessageDialog
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.alba.hortus.presentation.features.home.HomeScreen
import org.alba.hortus.presentation.managers.permissions.PermissionCallback
import org.alba.hortus.presentation.managers.permissions.PermissionStatus
import org.alba.hortus.presentation.managers.permissions.PermissionType
import org.alba.hortus.presentation.managers.permissions.createPermissionsManager
import org.alba.hortus.ui.theme.AppTheme

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
            screen = HomeScreen()
        )
    }
}

@Composable
private fun StartupAuthorisations() {
    var permissionRationalDialog by remember { mutableStateOf(false) }
    var launchSetting by remember { mutableStateOf(false) }


    if (permissionRationalDialog) {
        AlertMessageDialog(
            title = "Permission Required",
            message = "To use this feature, please allow the permission",
            positiveButtonText = "Settings",
            negativeButtonText = "Cancel",
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