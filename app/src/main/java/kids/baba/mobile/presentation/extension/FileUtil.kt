package kids.baba.mobile.presentation.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject


class FileUtil @Inject constructor(
    @ApplicationContext private val context: Context
){
    private val TAG = "FileUtil"

    fun optimizeBitmap(uri: Uri): String {
        try {
            val storage = context.cacheDir
            val fileName = String.format("%s.%s", UUID.randomUUID(), "jpg")

            val tempFile = File(storage, fileName)
            tempFile.createNewFile()

            val fos = FileOutputStream(tempFile)

            decodeBitmapFromUri(uri, context)?.apply {
                compress(Bitmap.CompressFormat.JPEG, /*60*/100, fos)
                recycle()
            } ?: throw NullPointerException()

            fos.flush()
            fos.close()

            return tempFile.absolutePath

        } catch (e: Exception) {
            Log.e(TAG, "FileUtil - ${e.message}")
            return uri.toString()
        }
    }

    private fun decodeBitmapFromUri(uri: Uri, context: Context): Bitmap? {

        val input = BufferedInputStream(context.contentResolver.openInputStream(uri))

        input.mark(input.available())

        var bitmap: Bitmap?

        BitmapFactory.Options().run {
            inJustDecodeBounds = true
            bitmap = BitmapFactory.decodeStream(input, null, this)
            input.reset()
            inSampleSize = calculateInSampleSize(this)
            inJustDecodeBounds = false

            bitmap = BitmapFactory.decodeStream(input, null, this)
            bitmap = bitmap?.let { rotateImageIfRequired(context, it, uri) }

        }

        input.close()

        return bitmap

    }

    private fun calculateInSampleSize(options: BitmapFactory.Options): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > Companion.MAX_HEIGHT || width > Companion.MAX_WIDTH) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= Companion.MAX_HEIGHT && halfWidth / inSampleSize >= Companion.MAX_WIDTH) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun rotateImageIfRequired(context: Context, bitmap: Bitmap, uri: Uri): Bitmap? {
        val input = context.contentResolver.openInputStream(uri) ?: return null
        val exif = ExifInterface(input)// 23 이하 -> ExifInterface(uri.path!!)

        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        Log.d(TAG, "orientation: $orientation")

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180F)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270F)
            ExifInterface.ORIENTATION_TRANSVERSE -> rotateImage(bitmap, 270F)
            else -> bitmap
        }
    }

    private fun rotateImage(bitmap: Bitmap, degree: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    companion object {
        private const val MAX_HEIGHT = 960
        private const val MAX_WIDTH = 1280
    }

}