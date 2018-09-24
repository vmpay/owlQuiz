package eu.vmpay.owlquiz.repository

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by Andrew on 12/04/2018.
 */
@Database(entities = arrayOf(Player::class, PlayerRating::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): PlayerDao
}