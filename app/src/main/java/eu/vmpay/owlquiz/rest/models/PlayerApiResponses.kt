package eu.vmpay.owlquiz.rest.models

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