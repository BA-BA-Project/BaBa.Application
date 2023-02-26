package kids.baba.mobile.core.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object EncryptedPrefs {
    private var prefs: SharedPreferences? = null

    fun initSharedPreferences(context: Context) {
        synchronized(this) {
            val sharedPrefsFile = "BaBaPrefs"
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            prefs = EncryptedSharedPreferences.create(
                context,
                sharedPrefsFile,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

    fun putString(key: String, value: String) {
        with(prefs?.edit()) {
            this?.putString(key, value)
            this?.apply()
        }
    }

    fun getString(key: String): String {
        return prefs?.getString(key, "") ?: ""
    }

    fun putInt(key: String, value: Int) {
        with(prefs?.edit()) {
            this?.putInt(key, value)
            this?.apply()
        }
    }

    fun getInt(key: String): Int {
        return prefs?.getInt(key, 0) ?: 0
    }

    fun putFloat(key: String, value: Float) {
        with(prefs?.edit()) {
            this?.putFloat(key, value)
            this?.apply()
        }
    }

    fun getFloat(key: String): Float {
        return prefs?.getFloat(key, 0f) ?: 0f
    }

    fun putBoolean(key: String, value: Boolean) {
        with(prefs?.edit()) {
            this?.putBoolean(key, value)
            this?.apply()
        }
    }

    fun getBoolean(key: String): Boolean {
        return prefs?.getBoolean(key, false) ?: false
    }
}