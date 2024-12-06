package org.alba.hortus.presentation.managers.images

import androidx.compose.ui.graphics.ImageBitmap

expect fun ByteArray.toImageBitmap(): ImageBitmap
