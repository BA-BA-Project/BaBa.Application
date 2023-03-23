package kids.baba.mobile.data.di

import android.content.Context
import android.os.Environment
import androidx.camera.core.Camera
import androidx.camera.core.CameraProvider
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kids.baba.mobile.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CameraModule {

    @Provides
    @Singleton
    fun provideOutputDirectory(@ApplicationContext context: Context): File {
        val appContext = context.applicationContext // requireContext
        val mediaDir = context.getExternalFilesDirs(Environment.DIRECTORY_PICTURES).firstOrNull()?.let {
            File(it, appContext.resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return mediaDir ?: appContext.filesDir
    }

    @Provides
    @Singleton
    fun provideCameraExecutor(@ApplicationContext context: Context): Executor {
        return ContextCompat.getMainExecutor(context)
    }

    @Provides
    @Singleton
    fun provideImageAnalyzerExecutor(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }

    @Provides
    @Singleton
    fun providePreview(): Preview {
        return Preview.Builder().apply { }.build()
    }


    @Provides
    @Singleton
    fun provideImageCapture(): ImageCapture {
        return ImageCapture.Builder().apply {
            setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        }.build()
    }





}

/*
/*

 */

    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }
 */