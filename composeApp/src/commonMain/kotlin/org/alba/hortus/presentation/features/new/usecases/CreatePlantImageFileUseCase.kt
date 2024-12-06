package org.alba.hortus.presentation.features.new.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.alba.hortus.presentation.managers.images.saveByteArrayToFile
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CreatePlantImageFileUseCase {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(
        byteArray: ByteArray,
        name: String,
    ) = withContext(Dispatchers.IO) {
        saveByteArrayToFile(
            byteArray = byteArray,
            fileName = "${name.replace(" ", "_")}_${Uuid.random()}.jpeg"
        )
    }
}