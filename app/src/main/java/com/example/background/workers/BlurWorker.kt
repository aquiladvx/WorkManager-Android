package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import com.example.background.R
import timber.log.Timber
import java.lang.Exception


/*
 * Davi Aquila
 * aquiladvx
 *
 * 07/11/2021
 */
class BlurWorker(context: Context, params: WorkerParameters): Worker(context, params) {

    override fun doWork(): Result {
        val appContext = applicationContext
        makeStatusNotification("Blurring image...", appContext)

        return try {
            val resourceUri = inputData.getString(KEY_IMAGE_URI)
            if (TextUtils.isEmpty(resourceUri)) {
                Timber.e("Invalid input Uri")
            }
            val picture = BitmapFactory.decodeStream(appContext.contentResolver.openInputStream(Uri.parse(resourceUri)))

            val blured = blurBitmap(picture, appContext)
            val output = writeBitmapToFile(appContext, blured)
            makeStatusNotification("Picture output = $output", appContext)
            val outputData = workDataOf(KEY_IMAGE_URI to output.toString())

            Result.success(outputData)
        } catch (t: Throwable) {
            Timber.e(t)
            Result.failure()
        }
    }
}