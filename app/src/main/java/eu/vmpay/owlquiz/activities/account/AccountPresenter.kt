package eu.vmpay.owlquiz.activities.account

import android.util.Log
import eu.vmpay.owlquiz.repository.Player
import eu.vmpay.owlquiz.repository.PlayersRepository
import eu.vmpay.owlquiz.repository.Team
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Andrew on 12/04/2018.
 */
class AccountPresenter(private val playersRepository: PlayersRepository) : AccountContract.Presenter {

    private lateinit var accountView: AccountContract.View
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun takeView(view: AccountContract.View) {
        accountView = view
        if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
        compositeDisposable = CompositeDisposable()
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

    override fun loadPlayersDetails(player: Player) {
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
}