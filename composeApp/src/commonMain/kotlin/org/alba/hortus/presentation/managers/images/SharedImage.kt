package org.alba.hortus.presentation.managers.images

import androidx.compose.ui.graphics.ImageBitmap

expect class SharedImage {
    fun toByteArray(): ByteArray?
    fun toImageBitmap(): ImageBitmap?
}