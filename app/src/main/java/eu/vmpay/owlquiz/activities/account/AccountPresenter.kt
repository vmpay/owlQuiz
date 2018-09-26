package eu.vmpay.owlquiz.activities.account

import android.util.Log
import eu.vmpay.owlquiz.repository.Player
import eu.vmpay.owlquiz.repository.PlayersRepository
import eu.vmpay.owlquiz.repository.Team
import eu.vmpay.owlquiz.utils.SharedPreferencesContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Andrew on 12/04/2018.
 */
class AccountPresenter(private val playersRepository: PlayersRepository, private val spContract: SharedPreferencesContract) : AccountContract.Presenter {

    private lateinit var accountView: AccountContract.View
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var actualPlayerId: Long = 0
    private var bookmarkPlayerId: Long = spContract.readPlayerId()

    override fun takeView(view: AccountContract.View) {
        accountView = view
        if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
        compositeDisposable = CompositeDisposable()
        if (bookmarkPlayerId != 0L) {
            loadPlayerAndTeamDetails(bookmarkPlayerId)
        }
    }

    override fun dropView() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    override fun searchForPlayer(surname: String, name: String, patronymic: String) {
        compositeDisposable.add(playersRepository.searchForPlayer(surname, name, patronymic)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    Log.d("retrofit", "Searching for players size ${result.size}")
                    if (accountView.isActive) {
                        if (!result.isEmpty())
                            accountView.showResultList(result)
                        else
                            accountView.showNoPlayersFound()
                    }
                }, { error ->
                    error.printStackTrace()
                    if (accountView.isActive) {
                        accountView.showNetworkError()
                    }
                })
        )
    }

    override fun loadPlayersDetails(playerId: Long) {
        compositeDisposable.add(playersRepository.getPlayer(playerId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    Log.d("retrofit", "Searching for players by id size ${result.size}")
                    if (accountView.isActive) {
                        if (!result.isEmpty())
                            accountView.showPlayersDetail(result[0])
                        else
                            accountView.showNoPlayersFound()
                    }
                }, { error ->
                    error.printStackTrace()
                    if (accountView.isActive) {
                        accountView.showNetworkError()
                    }
                })
        )
    }

    override fun loadPlayerAndTeamDetails(playerId: Long) {
        compositeDisposable.add(playersRepository.getPlayer(playerId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    Log.d("retrofit", "Searching for players by id size ${result.size}")
                    loadPlayerAndTeamDetails(result[0])
                }, { error ->
                    error.printStackTrace()
                    if (accountView.isActive) {
                        accountView.showNetworkError()
                    }
                })
        )
    }

    override fun loadPlayerAndTeamDetails(player: Player) {
        actualPlayerId = player.idplayer
        compositeDisposable.add(playersRepository.getPlayerRating(player.idplayer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    Log.d("retrofit", "Searching for playersRating by id size ${result.size}")
                    if (accountView.isActive) {
                        accountView.showPlayersDetail(player, result[result.lastIndex])
                    }
                }, { error ->
                    error.printStackTrace()
                    if (accountView.isActive) {
                        accountView.showNetworkError()
                    }
                })
        )
        var team: Team? = null
        compositeDisposable.add(playersRepository.getPlayerTeam(player.idplayer)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .flatMap { playerTeams -> playersRepository.getTeamById(playerTeams[playerTeams.lastIndex].idteam) }
                .flatMap { playerTeams: List<Team> ->
                    team = playerTeams[playerTeams.lastIndex]
                    playersRepository.getTeamById(playerTeams[playerTeams.lastIndex].idteam)
                }
                .flatMap { teams: List<Team> -> playersRepository.getTeamRatings(teams[0].idteam) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    Log.d("retrofit", "Searching for player team rating by id size ${result.size}")
                    if (accountView.isActive) {
                        if (!result.isEmpty() && team != null)
                            accountView.showPlayersDetail(team, result[0])
                        else
                            accountView.showNoPlayersFound()
                    }
                }, { error ->
                    error.printStackTrace()
                    if (accountView.isActive) {
                        accountView.showApiError()
                        accountView.clearTeamInfo()
                    }
                })
        )
    }

    override fun bookmarkThisPlayer() {
        bookmarkPlayerId = actualPlayerId
        spContract.writePlayerId(bookmarkPlayerId)
    }

    override fun getBookmarkedPlayer() {
        if (accountView.isActive)
            if (bookmarkPlayerId == 0L)
                accountView.showNoBookmarkYet()
            else
                loadPlayerAndTeamDetails(bookmarkPlayerId)
    }
}