package kids.baba.mobile.core

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import kids.baba.mobile.BuildConfig
import kids.baba.mobile.core.utils.EncryptedPrefs

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        EncryptedPrefs.initSharedPreferences(applicationContext)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}
