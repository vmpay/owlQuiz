package eu.vmpay.owlquiz.utils

import android.content.Context

class SharedPreferences(private val applicationContext: Context) : SharedPreferencesContract {
    private val PREFERENCE_FILE_KEY = "eu.vmpay.owlquiz.sp"
    private val PREFERENCE_KEY_PLAYER_ID = "player_id"

    override fun readPlayerId(): Long {
        return applicationContext.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE).getLong(PREFERENCE_KEY_PLAYER_ID, 0)
    }

    override fun writePlayerId(playerId: Long) {
        val sharedPref = applicationContext.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)
                ?: return
        with(sharedPref.edit()) {
            putLong(PREFERENCE_KEY_PLAYER_ID, playerId)
            apply()
        }
    }
}