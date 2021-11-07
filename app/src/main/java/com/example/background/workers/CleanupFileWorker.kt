package com.example.background.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.OUTPUT_PATH
import timber.log.Timber
import java.io.File


/*
 * Davi Aquila
 * aquiladvx
 *
 * 07/11/2021
 */
class CleanupFileWorker(context: Context, params: WorkerParameters): Worker(context, params) {

    override fun doWork(): Result {
        makeStatusNotification("Cleaning up old temporary files", applicationContext)
        sleep()

        return try {
            val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDir.exists()) {
                val entries = outputDir.listFiles()
                entries?.let { list ->
                    list.forEach { file ->
                        val name = file.name
                        if (name.isNotEmpty() && name.endsWith(".png")) {
                            val deleted = file.delete()
                            Timber.i("Deleted $name - $deleted")
                        }
                    }

                }
            }
            Result.success()
        } catch (t: Throwable) {
            Timber.e(t)
            Result.failure()
        }
    }
}