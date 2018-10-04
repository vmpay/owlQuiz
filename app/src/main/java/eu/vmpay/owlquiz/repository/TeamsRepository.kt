package eu.vmpay.owlquiz.repository

import android.util.Log
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class TeamsRepository private constructor(private val serverApi: RatingChgkService, private val teamDao: TeamDao) {
    private val TAG: String = "TeamsRepository"

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: TeamsRepository? = null

        fun getInstance(serverApi: RatingChgkService, teamDao: TeamDao) =
                instance ?: synchronized(this) {
                    instance ?: TeamsRepository(serverApi, teamDao).also { instance = it }
                }
    }

    fun getTeamById(teamId: Long): Observable<List<Team>> {
        return Observable.concatArray(
                getTeamByIdFromDb(teamId),
                getTeamsFromApi(teamId))
    }

    private fun getTeamsFromApi(teamId: Long): Observable<List<Team>> {
        return serverApi.getTeamById(teamId)
                .doOnNext {
                    Log.d(TAG, "Dispatching ${it.size} teams size from API...")
                    storeTeamInDb(it)
                }
    }

    private fun getTeamByIdFromDb(teamId: Long): Observable<List<Team>> {
        return teamDao.getTeamById(teamId).filter { it.isNotEmpty() }
                .toObservable()
                .doOnNext {
                    Log.d(TAG, "Dispatching ${it.size} teams size from DB...")
                }
    }

    private fun storeTeamInDb(teams: List<Team>) {
        Observable.fromCallable { teamDao.insertAllTeams(teams) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                    Log.d(TAG, "Inserted ${teams.size} teams size from API in DB...")
                }
    }

    fun getTeamRatings(teamId: Long): Observable<List<TeamRating>> {
        return Observable.concatArray(
                getTeamRatingsFromDb(teamId),
                getTeamRatingsFromApi(teamId))
    }

    private fun getTeamRatingsFromApi(teamId: Long): Observable<List<TeamRating>> {
        return serverApi.getTeamRatings(teamId)
                .doOnNext {
                    Log.d(TAG, "Dispatching ${it.size} teams size from API...")
                    storeTeamRatingsInDb(it)
                }
    }

    private fun getTeamRatingsFromDb(teamId: Long): Observable<List<TeamRating>> {
        return teamDao.getTeamRatings(teamId).filter { it.isNotEmpty() }
                .toObservable()
                .doOnNext {
                    Log.d(TAG, "Dispatching ${it.size} team ratings size from DB...")
                }
    }

    private fun storeTeamRatingsInDb(teamRatings: List<TeamRating>) {
        Observable.fromCallable { teamDao.insertAllTeamRatings(teamRatings) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                    Log.d(TAG, "Inserted ${teamRatings.size} team ratings size from API in DB...")
                }
    }
}