package eu.vmpay.owlquiz.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface TeamDao {
    @Query("SELECT * FROM teams WHERE idteam IN (:teamId)")
    fun getTeamById(teamId: Long): Single<List<Team>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTeams(teams: List<Team>)

    @Query("SELECT * FROM team_rating WHERE idteam IN (:teamId)")
    fun getTeamRatings(teamId: Long): Single<List<TeamRating>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTeamRatings(teams: List<TeamRating>)
}