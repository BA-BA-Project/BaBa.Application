package kids.baba.mobile

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kids.baba.mobile.core.utils.EncryptedPrefs

@HiltAndroidApp
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        EncryptedPrefs.initSharedPreferences(applicationContext)
    }
}
