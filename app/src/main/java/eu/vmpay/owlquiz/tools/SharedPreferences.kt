package eu.vmpay.owlquiz.tools

import android.content.Context

class SharedPreferences private constructor(private val applicationContext: Context) {
    private val PREFERENCE_FILE_KEY = "eu.vmpay.owlquiz.sp"
    private val PREFERENCE_KEY_PLAYER_ID = "player_id"

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

    fun readPlayerId(): Long {
        return applicationContext.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE).getLong(PREFERENCE_KEY_PLAYER_ID, 0)
    }

    fun writePlayerId(playerId: Long) {
        val sharedPref = applicationContext.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)
                ?: return
        with(sharedPref.edit()) {
            putLong(PREFERENCE_KEY_PLAYER_ID, playerId)
            apply()
        }
    }
}