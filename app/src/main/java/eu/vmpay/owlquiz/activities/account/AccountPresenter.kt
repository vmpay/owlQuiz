package eu.vmpay.owlquiz.activities.account

import eu.vmpay.owlquiz.repository.Player
import eu.vmpay.owlquiz.repository.PlayersRepository
import eu.vmpay.owlquiz.repository.Team
import eu.vmpay.owlquiz.repository.TeamsRepository
import eu.vmpay.owlquiz.utils.SharedPreferencesContract
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Andrew on 12/04/2018.
 */
class AccountPresenter(private val playersRepository: PlayersRepository,
                       private val teamsRepository: TeamsRepository,
                       private val spContract: SharedPreferencesContract,
                       private val processScheduler: Scheduler = Schedulers.io(),
                       private val androidScheduler: Scheduler = AndroidSchedulers.mainThread())
    : AccountContract.Presenter {

    lateinit var accountView: AccountContract.View
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    var actualPlayerId: Long = 0
    var bookmarkPlayerId: Long = spContract.readPlayerId()

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
                .observeOn(androidScheduler)
                .subscribeOn(processScheduler)
                .subscribe({ result ->
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
                .observeOn(androidScheduler)
                .subscribeOn(processScheduler)
                .subscribe({ result ->
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
                .observeOn(androidScheduler)
                .subscribeOn(processScheduler)
                .subscribe({ result ->
                    loadPlayerRatingAndTeamDetails(result[0])
                }, { error ->
                    error.printStackTrace()
                    if (accountView.isActive) {
                        accountView.showNetworkError()
                    }
                })
        )
    }

    override fun loadPlayerRatingAndTeamDetails(player: Player) {
        actualPlayerId = player.idplayer
        loadPlayerRating(player)
        loadPlayerTeamsDetails(player)
    }

    private fun loadPlayerRating(player: Player) {
        compositeDisposable.add(playersRepository.getPlayerRating(player.idplayer)
                .observeOn(androidScheduler)
                .subscribeOn(processScheduler)
                .subscribe({ result ->
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
    }

    private fun loadPlayerTeamsDetails(player: Player) {
        var team: Team? = null
        compositeDisposable.add(playersRepository.getPlayerTeam(player.idplayer)
                .observeOn(processScheduler)
                .subscribeOn(processScheduler)
                .flatMap { playerTeams -> teamsRepository.getTeamById(playerTeams[playerTeams.lastIndex].idteam) }
                .flatMap { teams: List<Team> ->
                    team = teams[teams.lastIndex]
                    teamsRepository.getTeamRatings(teams[teams.lastIndex].idteam)
                }
                .observeOn(androidScheduler)
                .subscribe({ result ->
                    if (accountView.isActive) {
                        if (!result.isEmpty() && team != null)
                            accountView.showTeamDetails(team, result[0])
                        else
                            accountView.showNoTeamFound()
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