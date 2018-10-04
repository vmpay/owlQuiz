package eu.vmpay.owlquiz.activities.account

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import eu.vmpay.owlquiz.R
import eu.vmpay.owlquiz.repository.*
import eu.vmpay.owlquiz.utils.SharedPreferences
import eu.vmpay.owlquiz.utils.SnackbarMessage
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class AccountViewModel(
        private val playersRepository: PlayersRepository, private val teamsRepository: TeamsRepository,
        private val sharedPreferences: SharedPreferences, private val processScheduler: Scheduler = Schedulers.io(),
        private val androidScheduler: Scheduler = AndroidSchedulers.mainThread()) : ViewModel() {
    val arePlayerDetailsShown = ObservableBoolean(false)

    val surname = ObservableField<String>()
    val name = ObservableField<String>()
    val patronymic = ObservableField<String>()
    val playerId = ObservableField<String>()
    var players = MediatorLiveData<List<Player>>()

    val player = ObservableField<Player>()
    val playersRating = ObservableField<PlayerRating>()
    val team = ObservableField<Team>()
    val teamRating = ObservableField<TeamRating>()

    val errorText = ObservableField<String>()
    val isErrorVisible = ObservableBoolean(false)
    var snackbarMessage = SnackbarMessage()

    var actualPlayerId: Long = 0
    var bookmarkedPlayerId: Long = sharedPreferences.readPlayerId()
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        if (bookmarkedPlayerId != 0L)
            loadPlayerAndTeamDetails(bookmarkedPlayerId)
    }

    fun onFabClick() {
        when {
            arePlayerDetailsShown.get() -> arePlayerDetailsShown.set(false)
            playerId.get().isNullOrEmpty() -> {
                val list = listOf(surname.get(), name.get(), patronymic.get()).map { it ?: "" }
                loadPlayersDetails(list[0], list[1], list[2])
            }
            else -> loadPlayersDetails(playerId.get().toString().toLong())
        }
    }

    fun loadPlayersDetails(surname: String = "", name: String = "", patronymic: String = "") {
        compositeDisposable.add(playersRepository.searchForPlayer(surname, name, patronymic)
                .observeOn(androidScheduler)
                .subscribeOn(processScheduler)
                .subscribe({ result ->
                    if (!result.isEmpty()) {
                        isErrorVisible.set(false)
                        players.value = result
                    } else
                        showError(R.string.no_players_found)
                }, { error ->
                    error.printStackTrace()
                    showError(R.string.network_error)
                })
        )
    }

    fun loadPlayersDetails(playerId: Long) {
        compositeDisposable.add(playersRepository.getPlayer(playerId)
                .observeOn(androidScheduler)
                .subscribeOn(processScheduler)
                .subscribe({ result ->
                    if (!result.isEmpty()) {
                        isErrorVisible.set(false)
                        players.value = result
                    } else
                        showError(R.string.no_players_found)
                }, { error ->
                    error.printStackTrace()
                    showError(R.string.network_error)
                })
        )
    }

    fun onBookmarkClick() {
        when {
            arePlayerDetailsShown.get() -> {
                bookmarkedPlayerId = actualPlayerId
                sharedPreferences.writePlayerId(actualPlayerId)
                snackbarMessage.setValue(R.string.bookmarked_success)
            }
            bookmarkedPlayerId == 0L -> snackbarMessage.setValue(R.string.bookmark_empty)
            else -> loadPlayerAndTeamDetails(bookmarkedPlayerId)
        }
    }

    private fun loadPlayerAndTeamDetails(playerId: Long) {
        compositeDisposable.add(playersRepository.getPlayer(playerId)
                .observeOn(androidScheduler)
                .subscribeOn(processScheduler)
                .subscribe({ result ->
                    loadPlayerRatingAndTeamDetails(result[0])
                }, { error ->
                    error.printStackTrace()
                    showError(R.string.network_error)
                })
        )
    }

    fun loadPlayerRatingAndTeamDetails(player: Player) {
        actualPlayerId = player.idplayer
        loadPlayerRating(player)
        loadPlayerTeamsDetails(player)
    }

    private fun loadPlayerTeamsDetails(player: Player) {
        var team: Team? = null
        this.team.set(null)
        this.teamRating.set(null)
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
                    if (!result.isEmpty() && team != null) {
                        this.team.set(team)
                        this.teamRating.set(result[0])
                    } else
                        showError(R.string.no_team_found)
                }, { error ->
                    error.printStackTrace()
                    showError(R.string.server_internal_error)
                })
        )
    }

    private fun loadPlayerRating(player: Player) {
        compositeDisposable.add(playersRepository.getPlayerRating(player.idplayer)
                .observeOn(androidScheduler)
                .subscribeOn(processScheduler)
                .subscribe({ result ->
                    this.player.set(player)
                    this.playersRating.set(result[result.lastIndex])
                    isErrorVisible.set(false)
                    arePlayerDetailsShown.set(true)
                }, { error ->
                    error.printStackTrace()
                    showError(R.string.network_error)
                })
        )
    }

    private fun showError(errorMessageId: Int) {
        isErrorVisible.set(true)
//        errorText.set(errorMessage)
        snackbarMessage.setValue(errorMessageId)
    }
}