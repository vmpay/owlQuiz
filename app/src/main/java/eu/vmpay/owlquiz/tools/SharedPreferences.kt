package eu.vmpay.owlquiz.tools

import android.content.Context
import android.preference.PreferenceManager
import eu.vmpay.owlquiz.R

class SharedPreferences private constructor(private val applicationContext: Context) {
    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: SharedPreferences? = null

        fun getInstance(context: Context): SharedPreferences {
            return instance ?: synchronized(this) {
                instance
                        ?: SharedPreferences(context).also { instance = it }
            }
        }
    }

    fun readPlayerId() = applicationContext.getSharedPreferences(getPreferencesFileKey(), Context.MODE_PRIVATE)
            .getLong(getPLayerIdKey(), 0)

    fun writePlayerId(playerId: Long) {
        val sharedPref = applicationContext.getSharedPreferences(getPreferencesFileKey(), Context.MODE_PRIVATE)
                ?: return
        with(sharedPref.edit()) {
            putLong(getPLayerIdKey(), playerId)
            apply()
        }
    }

    fun isNotificationSoundOn() = PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean(
            applicationContext.getString(R.string.timer_sound_notification_key), true)

    private fun getPreferencesFileKey() = applicationContext.getString(R.string.preference_file_key)

    private fun getPLayerIdKey() = applicationContext.getString(R.string.preference_file_key)
}