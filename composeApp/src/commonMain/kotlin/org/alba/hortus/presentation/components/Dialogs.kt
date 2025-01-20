package org.alba.hortus.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.download
import hortus.composeapp.generated.resources.ic_camera
import hortus.composeapp.generated.resources.ic_images
import hortus.composeapp.generated.resources.image_source_dialog_camera
import hortus.composeapp.generated.resources.image_source_dialog_gallery
import hortus.composeapp.generated.resources.image_source_dialog_message
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ImageSourceOptionDialog(
    onDismissRequest: () -> Unit,
    onGalleryRequest: () -> Unit = {},
    onCameraRequest: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface(shape = RoundedCornerShape(size = 12.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.image_source_dialog_message),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp).clickable {
                        onCameraRequest.invoke()
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(Res.drawable.ic_camera),
                        contentDescription = null
                    )
                    Text(text = stringResource(Res.string.image_source_dialog_camera))
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp).clickable {
                        onGalleryRequest.invoke()
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(Res.drawable.ic_images),
                        contentDescription = null
                    )
                    Text(text = stringResource(Res.string.image_source_dialog_gallery))
                }
            }
        }
    }
}

@Composable
fun AlertMessageDialog(
    title: String,
    message: String? = null,
    resource: Painter? = painterResource(Res.drawable.download),
    positiveButtonText: String? = null,
    negativeButtonText: String? = null,
    onPositiveClick: () -> Unit = {},
    onNegativeClick: () -> Unit = {},
) {

    Dialog(
        onDismissRequest = {}, properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            shape = RoundedCornerShape(size = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                resource?.let {
                    Image(
                        modifier = Modifier.size(200.dp),
                        painter = it,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                message?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(end = 16.dp, start = 16.dp)
                ) {
                    negativeButtonText?.let {
                        Button(
                            modifier = Modifier.weight(1f), onClick = {
                                onNegativeClick()
                            }
                        ) {
                            Text(text = it, textAlign = TextAlign.Center, maxLines = 1)
                        }

                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    positiveButtonText?.let {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onPositiveClick()
                            },
                        ) {
                            Text(text = it, textAlign = TextAlign.Center, maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}