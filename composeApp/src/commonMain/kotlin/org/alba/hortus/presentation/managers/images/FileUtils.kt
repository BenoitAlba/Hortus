package org.alba.hortus.presentation.managers.images

expect fun saveByteArrayToFile(byteArray: ByteArray, fileName: String): String

expect fun deleteFile(fileName: String): Boolean