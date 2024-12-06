package org.alba.hortus.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.default_plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alba.hortus.presentation.managers.images.PermissionCallback
import org.alba.hortus.presentation.managers.images.PermissionStatus
import org.alba.hortus.presentation.managers.images.PermissionType
import org.alba.hortus.presentation.managers.images.createPermissionsManager
import org.alba.hortus.presentation.managers.images.rememberGalleryManager
import org.jetbrains.compose.resources.painterResource

@Composable
fun ImagePicker(
    modifier: Modifier = Modifier,
    image: ImageBitmap? = null,
    onStartCamera: (isLaunchCamera: Boolean) -> Unit,
    onImageSelected: (image: ImageBitmap) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var imageSourceOptionDialog by remember { mutableStateOf(false) }
    var launchCamera by remember { mutableStateOf(false) }
    var launchGallery by remember { mutableStateOf(false) }
    var launchSetting by remember { mutableStateOf(false) }
    var permissionRationalDialog by remember { mutableStateOf(false) }
    var showLoader by remember { mutableStateOf(false) }

    val permissionsManager = createPermissionsManager(object : PermissionCallback {
        override fun onPermissionStatus(
            permissionType: PermissionType,
            status: PermissionStatus
        ) {
            when (status) {
                PermissionStatus.GRANTED -> {
                    when (permissionType) {
                        PermissionType.CAMERA -> launchCamera = true
                        PermissionType.GALLERY -> launchGallery = true
                    }
                }

                else -> {
                    permissionRationalDialog = true
                }
            }
        }
    })

    val galleryManager = rememberGalleryManager { image ->
        showLoader = true
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                image?.toImageBitmap()
            }
            showLoader = false
            onImageSelected(bitmap!!)
        }
    }

    if (imageSourceOptionDialog) {
        ImageSourceOptionDialog(
            onDismissRequest = {
                imageSourceOptionDialog = false
            }, onGalleryRequest = {
                imageSourceOptionDialog = false
                launchGallery = true
            }, onCameraRequest = {
                imageSourceOptionDialog = false
                launchCamera = true
            })
    }

    if (launchGallery) {
        if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
            galleryManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.GALLERY)
        }
        launchGallery = false
    }

    if (launchCamera) {
        if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
            onStartCamera(launchCamera)
        } else {
            permissionsManager.askPermission(PermissionType.CAMERA)
        }
        launchCamera = false
    }

    if (launchSetting) {
        permissionsManager.launchSettings()
        launchSetting = false
    }

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
            })

    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (image != null) {
            Box {  }
            Image(
                bitmap = image,
                contentDescription = "Profile",
                modifier = Modifier.size(200.dp).clip(CircleShape).clickable {
                    imageSourceOptionDialog = true
                },
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                modifier = Modifier.size(200.dp).clip(CircleShape).clickable {
                    imageSourceOptionDialog = true
                },
                painter = painterResource(Res.drawable.default_plant),
                contentDescription = "Profile",
            )
        }
        if (showLoader) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center),
                color = Color.White.copy(alpha = 0.7f),
                strokeWidth = 8.dp,
            )
        }
    }
}