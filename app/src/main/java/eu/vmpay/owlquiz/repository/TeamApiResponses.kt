package eu.vmpay.owlquiz.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * {
 *      "idteam": "58379",
 *      "name": "Ы!",
 *      "town": "Берлин",
 *      "region_name": "Берлин",
 *      "country_name": "Германия",
 *      "tournaments_this_season": "0",
 *      "tournaments_total": "1",
 *      "comment": ""
 * }
 */
@Entity(tableName = "teams")
data class Team(
        @PrimaryKey
        @ColumnInfo(name = "idteam")
        val idteam: Long,
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "town")
        val town: String,
        @ColumnInfo(name = "region_name")
        val region_name: String,
        @ColumnInfo(name = "country_name")
        val country_name: String,
        @ColumnInfo(name = "tournaments_this_season")
        val tournaments_this_season: Long,
        @ColumnInfo(name = "tournaments_total")
        val tournaments_total: Long,
        @ColumnInfo(name = "comment")
        val comment: String
)

/**
 * {
 *      "idteam": "58380",
 *      "idrelease": "1332",
 *      "rating": "2822",
 *      "rating_position": "699",
 *      "date": "2018-03-15",
 *      "formula": "b"
 * }
 */
@Entity(tableName = "team_rating", primaryKeys = ["idrelease", "idteam"])
data class TeamRating(
        @ColumnInfo(name = "idrelease")
        val idrelease: Long,
        @ColumnInfo(name = "idteam")
        val idteam: Long,
        @ColumnInfo(name = "rating")
        val rating: Long,
        @ColumnInfo(name = "rating_position")
        val rating_position: Long,
        @ColumnInfo(name = "date")
        val date: String,
        @ColumnInfo(name = "formula")
        val formula: String

)