package eu.vmpay.owlquiz.repository

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Created by Andrew on 12/04/2018.
 */
@Database(entities = [Player::class, PlayerRating::class, PlayerTeam::class, Team::class, TeamRating::class],
        version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun teamDao(): TeamDao
}