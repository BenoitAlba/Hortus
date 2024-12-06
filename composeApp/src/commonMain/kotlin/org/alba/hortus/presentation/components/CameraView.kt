package org.alba.hortus.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.preat.peekaboo.ui.camera.PeekabooCamera
import com.preat.peekaboo.ui.camera.rememberPeekabooCameraState
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.baseline_close_24
import hortus.composeapp.generated.resources.baseline_flip_camera_android_24
import hortus.composeapp.generated.resources.ic_camera
import org.jetbrains.compose.resources.painterResource

@Composable
fun CameraView(
    modifier: Modifier = Modifier,
    onCapture: (capture: ByteArray?) -> Unit,
    onBack: () -> Unit,
) {
    val state = rememberPeekabooCameraState(
        onCapture = {
            onCapture(it)
        }
    )
    Box(modifier = modifier) {
        PeekabooCamera(
            state = state,
            modifier = Modifier.fillMaxSize(),
            permissionDeniedContent = {
                PermissionDenied(
                    modifier = Modifier.fillMaxSize(),
                )
            },
        )
        CameraOverlay(
            isCapturing = state.isCapturing,
            onBack = onBack,
            onCapture = { state.capture() },
            onConvert = { state.toggleCamera() },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun PermissionDenied(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_camera),
            contentDescription = "Warning Icon",
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Please grant the camera permission!",
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CameraOverlay(
    isCapturing: Boolean,
    onCapture: () -> Unit,
    onConvert: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.baseline_close_24),
                contentDescription = "Back Button",
                tint = Color.White,
            )
        }
        if (isCapturing) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center),
                color = Color.White.copy(alpha = 0.7f),
                strokeWidth = 8.dp,
            )
        }
        CircularButton(
            painter = painterResource(Res.drawable.baseline_flip_camera_android_24),
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp),
            onClick = onConvert,
        )

        InstagramCameraButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            onClick = onCapture,
        )
    }
}

@Composable
fun CircularButton(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier
            .size(60.dp)
            .clip(CircleShape)
            .run {
                if (enabled) {
                    clickable { onClick() }
                } else {
                    this
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
fun CircularButton(
    painter: Painter,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    CircularButton(
        modifier = modifier,
        content = {
            Icon(painter, null, Modifier.size(34.dp), Color.White)
        },
        enabled = enabled,
        onClick = onClick,
    )
}

@Composable
fun InstagramCameraButton(
    modifier: Modifier = Modifier,
    size: Dp = 70.dp,
    borderSize: Dp = 5.dp,
    onClick: () -> Unit,
) {
    // Outer size including the border
    val outerSize = size + borderSize * 2
    // Inner size excluding the border
    val innerSize = size - borderSize

    Box(
        modifier =
        modifier
            .size(outerSize)
            .clip(CircleShape)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        // Surface for the border effect
        Surface(
            modifier = Modifier.size(outerSize),
            shape = CircleShape,
            color = Color.Transparent,
            border = BorderStroke(borderSize, Color.White),
        ) {}

        // Inner clickable circle
        Box(
            modifier =
            Modifier
                .size(innerSize)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { onClick() },
            contentAlignment = Alignment.Center,
        ) {}
    }
}