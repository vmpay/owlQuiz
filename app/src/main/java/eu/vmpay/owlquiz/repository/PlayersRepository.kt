package eu.vmpay.owlquiz.repository

import android.util.Log
import eu.vmpay.owlquiz.rest.models.Player
import eu.vmpay.owlquiz.rest.models.RatingChgkService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Andrew on 12/04/2018.
 */
class PlayersRepository(val playerApi: RatingChgkService, val playerDao: PlayerDao) {

    private val TAG = "PlayersRepository"

    fun getPlayer(playerId: Long): Observable<List<Player>> {
        return Observable.concatArray(
                getPlayerFromDb(playerId),
                getPlayerFromApi(playerId))
    }


    fun getPlayerFromDb(playerId: Long): Observable<List<Player>> {
        return playerDao.getPlayers(playerId).filter { it.isNotEmpty() }
                .toObservable()
                .doOnNext {
                    Log.d(TAG, "Dispatching ${it.size} users from DB...")
                }
    }

    fun getPlayerFromApi(playerId: Long): Observable<List<Player>> {
        return playerApi.searchPlayer(playerId)
                .doOnNext {
                    Log.d(TAG, "Dispatching ${it.size} users from API...")
                    storePlayerInDb(it)
                }
    }

    fun storePlayerInDb(users: List<Player>) {
        Observable.fromCallable { playerDao.insertAll(users) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                    Log.d(TAG, "Inserted ${users.size} users from API in DB...")
                }
    }
}