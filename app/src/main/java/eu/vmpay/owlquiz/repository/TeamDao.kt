package eu.vmpay.owlquiz.repository

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
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