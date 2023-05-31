package kids.baba.mobile.presentation.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import kids.baba.mobile.R
import javax.inject.Inject

class DownLoadNotificationManager @Inject constructor(
    private val context: Context,
){

    fun showNotification(uri: Uri) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "image/*")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baba_logo)
            .setContentTitle(context.getString(R.string.save_photo_noti_title))
            .setContentText(context.getString(R.string.save_photo_noti_body))
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object{
        const val CHANNEL_ID = "PHOTO_DOWNLOAD_SUCCESS_ID"
        private const val CHANNEL_NAME = "PHOTO_DOWNLOAD_SUCCESS"
        private const val NOTIFICATION_ID = 1
    }
}