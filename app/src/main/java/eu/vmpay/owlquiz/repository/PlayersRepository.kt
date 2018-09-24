package eu.vmpay.owlquiz.repository

import android.util.Log
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Andrew on 12/04/2018.
 */
class PlayersRepository(private val playerApi: RatingChgkService, private val playerDao: PlayerDao) {

    private val TAG = "PlayersRepository"

    fun getPlayer(playerId: Long): Observable<List<Player>> {
        return Observable.concatArray(
                getPlayerFromDb(playerId),
                getPlayerFromApi(playerId))
    }

    private fun getPlayerFromDb(playerId: Long): Observable<List<Player>> {
        return playerDao.getPlayersById(playerId).filter { it.isNotEmpty() }
                .toObservable()
                .doOnNext {
                    Log.d(TAG, "Dispatching ${it.size} player from DB...")
                }
    }

    private fun getPlayerFromApi(playerId: Long): Observable<List<Player>> {
        return playerApi.searchPlayerById(playerId)
                .doOnNext {
                    Log.d(TAG, "Dispatching ${it.size} player from API...")
                    storePlayerInDb(it)
                }
    }

    private fun storePlayerInDb(players: List<Player>) {
        Observable.fromCallable { playerDao.insertAll(players) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                    Log.d(TAG, "Inserted ${players.size} players from API in DB...")
                }
    }

    fun searchForPlayer(surname: String = "", name: String = "", patronymic: String = ""): Observable<List<Player>> {
        return Observable.concatArray(
                getPlayerFromDb(surname, name, patronymic),
                getPlayerFromApi(surname, name, patronymic))
    }

    private fun getPlayerFromApi(surname: String, name: String, patronymic: String): Observable<List<Player>> {
        return playerApi.searchPlayerByFullName(surname, name, patronymic)
                .map { it.items }
                .doOnNext { it ->
                    Log.d(TAG, "Dispatching ${it.size} players from API...")
                    it.forEach {
                        if (it.comment.isNullOrEmpty()) it.comment = ""
                        if (it.db_chgk_info_tag.isNullOrEmpty()) it.db_chgk_info_tag = ""
                    }
                    storePlayerInDb(it)
                }
    }

    private fun getPlayerFromDb(surname: String, name: String, patronymic: String): Observable<List<Player>> {
        return playerDao.getPlayersByFullName(surname, name, patronymic).filter { it.isNotEmpty() }
                .toObservable()
                .doOnNext {
                    Log.d(TAG, "Dispatching ${it.size} players from DB...")
                }
    }

    fun getPlayerRating(playerId: Long): Observable<List<PlayerRating>> {
        return Observable.concatArray(
                getPlayerRatingFromDb(playerId),
                getPlayerRatingFromApi(playerId))
    }

    private fun getPlayerRatingFromApi(playerId: Long): Observable<List<PlayerRating>> {
        return playerApi.getPlayerRating(playerId)
                .doOnNext {
                    Log.d(TAG, "Dispatching ${it.size} playerRating size from API...")
                    storeRatingsInDb(it)
                }
    }

    private fun getPlayerRatingFromDb(playerId: Long): Observable<List<PlayerRating>> {
        return playerDao.getPlayerRating(playerId).filter { it.isNotEmpty() }
                .toObservable()
                .doOnNext {
                    Log.d(TAG, "Dispatching ${it.size} playerRating size from DB...")
                }
    }

    private fun storeRatingsInDb(playerRatings: List<PlayerRating>) {
        Observable.fromCallable { playerDao.insertAllRatings(playerRatings) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                    Log.d(TAG, "Inserted $playerRatings playerRatings from API in DB...")
                }
    }


}