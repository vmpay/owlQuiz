package eu.vmpay.owlquiz.repository

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Andrew on 04/04/2018.
 */
/**
 * [
 *  {
 *      "idplayer": "112",
 *      "surname": "Аболмазова",
 *      "name": "Жанна",
 *      "patronymic": "Сергеевна",
 *      "comment": "",
 *      "db_chgk_info_tag": ""
 *  }
 * ]
 */
@Entity(tableName = "players")
data class Player(
        @PrimaryKey
        @ColumnInfo(name = "idplayer")
        val idplayer: Long,
        @ColumnInfo(name = "surname")
        val surname: String,
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "patronymic")
        val patronymic: String,
        @ColumnInfo(name = "comment")
        var comment: String = "",
        @ColumnInfo(name = "db_chgk_info_tag")
        var db_chgk_info_tag: String = "")

/**
 * {
 *      "items": [
 *              {
 *                      "idplayer": "3281",
 *                      "surname": "Бескрёстнов",
 *                      "name": "Андрей",
 *                      "patronymic": "Юрьевич"
 *              }
 *              ],
 *      "total_items": "1",
 *      "current_items": "1-1000"
 * }
 */
data class PlayerSearch(
        val items: List<Player>,
        val total_items: String,
        val current_items: String)

/**
 * {
 *      "idplayer": "147205",
 *      "idrelease": "1360",
 *      "rating": "5504",
 *      "rating_position": "4150",
 *      "date": "2018-09-20",
 *      "tournaments_in_year": "28",
 *      "tournament_count_total": "35"
 * }
 */
@Entity(tableName = "player_rating", primaryKeys = ["idplayer", "idrelease"])
data class PlayerRating(
        @ColumnInfo(name = "idplayer")
        val idplayer: Long,
        @ColumnInfo(name = "idrelease")
        val idrelease: Long,
        @ColumnInfo(name = "rating")
        val rating: Long,
        @ColumnInfo(name = "rating_position")
        val rating_position: Long,
        @ColumnInfo(name = "date")
        val date: String,
        @ColumnInfo(name = "tournaments_in_year")
        val tournaments_in_year: Long,
        @ColumnInfo(name = "tournament_count_total")
        val tournament_count_total: Long)

/**
 * {
 *      "idplayer": "147205",
 *      "idteam": "58380",
 *      "idseason": "52",
 *      "is_captain": "0",
 *      "added_since": "2018-08-08"
 * }
 */
@Entity(tableName = "player_team", primaryKeys = ["idseason", "idplayer", "idteam"])
data class PlayerTeam(
        @ColumnInfo(name = "idseason")
        val idseason: Long,
        @ColumnInfo(name = "idplayer")
        val idplayer: Long,
        @ColumnInfo(name = "idteam")
        val idteam: Long,
        @ColumnInfo(name = "is_captain")
        val is_captain: Long,
        @ColumnInfo(name = "added_since")
        val added_since: String
)

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