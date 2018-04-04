package eu.vmpay.owlquiz.rest.models

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
data class Player(val idplayer: Long, val surname: String, val name: String, val patronymic: String, val comment: String, val db_chgk_info_tag: String)