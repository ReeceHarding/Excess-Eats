package com.exceseats.util

import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

class ImageUploadUtils @Inject constructor(
    private val storage: FirebaseStorage
) {
    suspend fun uploadImage(imageUri: Uri, folder: String): String? {
        return try {
            val filename = "${UUID.randomUUID()}.jpg"
            val ref = storage.reference.child("$folder/$filename")

            ref.putFile(imageUri).await()
            ref.downloadUrl.await().toString()
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    suspend fun deleteImage(imageUrl: String) {
        try {
            storage.getReferenceFromUrl(imageUrl).delete().await()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
