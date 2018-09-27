package eu.vmpay.owlquiz.repository

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single

@Dao
interface PlayerDao {

    @Query("SELECT * FROM players WHERE idplayer IN (:playerId)")
    fun getPlayersById(playerId: Long): Single<List<Player>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(player: Player)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(players: List<Player>)

    @Query("SELECT * FROM players WHERE "
            + "surname LIKE :surname AND "
            + "name LIKE :name AND "
            + "patronymic LIKE :patronymic"
    )
    fun getPlayersByFullName(surname: String, name: String, patronymic: String): Single<List<Player>>

    @Query("SELECT * FROM player_rating WHERE idplayer IN (:playerId)")
    fun getPlayerRating(playerId: Long): Single<List<PlayerRating>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(playerRating: PlayerRating)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRatings(playerRatings: List<PlayerRating>)

    @Query("SELECT * FROM player_team WHERE idplayer IN (:playerId)")
    fun getPlayerTeam(playerId: Long): Single<List<PlayerTeam>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPlayerTeams(playerTeams: List<PlayerTeam>)
}
