package eu.vmpay.owlquiz.repository

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import eu.vmpay.owlquiz.rest.models.Player
import io.reactivex.Single

@Dao
interface PlayerDao {

    @Query("SELECT * FROM players  WHERE idplayer IN (:playerId)")
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

}
