package org.alba.hortus.presentation.managers.images

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIImage
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import platform.UIKit.UIImageJPEGRepresentation


actual class SharedImage(private val image: UIImage?) {
    @OptIn(ExperimentalForeignApi::class)
    actual fun toByteArray(): ByteArray? {
        return if (image != null) {
            val imageData = UIImageJPEGRepresentation(image, COMPRESSION_QUALITY)
                ?: throw IllegalArgumentException("image data is null")
            val bytes = imageData.bytes ?: throw IllegalArgumentException("image bytes is null")
            val length = imageData.length

            val data: CPointer<ByteVar> = bytes.reinterpret()
            ByteArray(length.toInt()) { index -> data[index] }
        } else {
            null
        }

    }

    actual fun toImageBitmap(): ImageBitmap? {
        return toByteArray()?.toImageBitmap()
    }

    private companion object {
        const val COMPRESSION_QUALITY = 0.99
    }
}