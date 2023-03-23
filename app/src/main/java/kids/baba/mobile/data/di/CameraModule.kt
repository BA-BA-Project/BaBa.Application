package kids.baba.mobile.data.di

import android.content.Context
import android.os.Environment
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kids.baba.mobile.R
import java.io.File
import java.util.concurrent.Executor
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
// 추후에 얼굴 인식 등의 요구사항이 생긴다면 추가해야하는 ImageAnalyzer
    @Provides
    @Singleton
    fun provideImageAnalyzerExecutor(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }

    @Provides
    @Singleton
    fun provideImageAnalyzer(): ImageAnalysis {
        return ImageAnalysis.Builder().apply {  }.build().also {
            it.setAnalyzer(provideImageAnalyzerExecutor(),LuminosityAnalyzer{luma ->
                //
            })
        }
    }

}

 */
