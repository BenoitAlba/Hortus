package org.alba.hortus.presentation.managers.images

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope

@Composable
actual fun createPermissionsManager(callback: PermissionCallback): PermissionsManager {
    return remember { PermissionsManager(callback) }
}

actual class PermissionsManager actual constructor(private val callback: PermissionCallback) :
    PermissionHandler {
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    override fun askPermission(permission: PermissionType) {
        when (permission) {
            PermissionType.CAMERA -> {
                val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
                val requestPermissionLauncher = requestPermissionLauncher(permission)

                HandlePermissionRequest(
                    cameraPermissionState,
                    permission,
                    Manifest.permission.CAMERA,
                    callback,
                    requestPermissionLauncher
                )
            }

            PermissionType.GALLERY -> {
                // Granted by default because in Android GetContent API does not require any
                // runtime permissions, and i am using it to access gallery in my app
                callback.onPermissionStatus(
                    permission, PermissionStatus.GRANTED
                )
            }

            PermissionType.ACCESS_FINE_LOCATION -> {
                val locationPermissionState =
                    rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
                val requestPermissionLauncher = requestPermissionLauncher(permission)

                HandlePermissionRequest(
                    locationPermissionState,
                    permission,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    callback,
                    requestPermissionLauncher
                )
            }

            PermissionType.ACCESS_COARSE_LOCATION -> {
                val locationPermissionState =
                    rememberPermissionState(Manifest.permission.ACCESS_COARSE_LOCATION)
                val requestPermissionLauncher = requestPermissionLauncher(permission)

                HandlePermissionRequest(
                    locationPermissionState,
                    permission,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    callback,
                    requestPermissionLauncher
                )
            }
        }
    }

    @Composable
    fun requestPermissionLauncher(
        permission: PermissionType
    ) = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            callback.onPermissionStatus(
                permission, PermissionStatus.GRANTED
            )
        } else {
            callback.onPermissionStatus(
                permission, PermissionStatus.SHOW_RATIONAL
            )
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun HandlePermissionRequest(
        permissionState: PermissionState,
        permissionType: PermissionType,
        permission: String,
        callback: PermissionCallback,
        requestPermissionLauncher: ActivityResultLauncher<String>
    ) {
        LaunchedEffect(permissionState) {
            if (!permissionState.status.isGranted && permissionState.status.shouldShowRationale) {
                callback.onPermissionStatus(
                    permissionType, PermissionStatus.SHOW_RATIONAL
                )
            } else {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    override fun isPermissionGranted(permission: PermissionType): Boolean {
        return when (permission) {
            PermissionType.CAMERA -> {
                val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
                cameraPermissionState.status.isGranted
            }

            PermissionType.GALLERY -> {
                // Granted by default because in Android GetContent API does not require any
                // runtime permissions, and i am using it to access gallery in my app
                true
            }

            PermissionType.ACCESS_FINE_LOCATION -> {
                val locationPermissionState =
                    rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
                locationPermissionState.status.isGranted
            }

            PermissionType.ACCESS_COARSE_LOCATION -> {
                val locationPermissionState =
                    rememberPermissionState(Manifest.permission.ACCESS_COARSE_LOCATION)
                locationPermissionState.status.isGranted
            }
        }
    }

    @Composable
    override fun launchSettings() {
        val context = LocalContext.current
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ).also {
            context.startActivity(it)
        }
    }
}