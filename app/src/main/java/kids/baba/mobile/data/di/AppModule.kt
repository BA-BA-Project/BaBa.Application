package kids.baba.mobile.data.di

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kids.baba.mobile.presentation.util.notification.DownLoadNotificationManager

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @Provides
    fun provideDownLoadNotificationManager(
        @ApplicationContext
        context: Context
    ) = DownLoadNotificationManager(context)
}