package eu.vmpay.owlquiz.utils

interface SharedPreferencesContract {
    fun writePlayerId(playerId: Long)

    fun readPlayerId(): Long
}