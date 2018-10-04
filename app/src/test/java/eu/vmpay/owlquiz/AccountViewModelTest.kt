package eu.vmpay.owlquiz

import androidx.lifecycle.MediatorLiveData
import eu.vmpay.owlquiz.activities.account.AccountViewModel
import eu.vmpay.owlquiz.repository.*
import eu.vmpay.owlquiz.tools.SharedPreferences
import eu.vmpay.owlquiz.utils.SnackbarMessage
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import retrofit2.Response

class AccountViewModelTest {
    private val testScheduler: TestScheduler = TestScheduler()
    private val compositeDisposable: CompositeDisposable = Mockito.mock(CompositeDisposable::class.java)
    private val playersRepository: PlayersRepository = Mockito.mock(PlayersRepository::class.java)
    private val teamsRepository: TeamsRepository = Mockito.mock(TeamsRepository::class.java)
    private val sharedPreferences: SharedPreferences = Mockito.mock(SharedPreferences::class.java)
    private val viewModel: AccountViewModel = AccountViewModel(playersRepository, teamsRepository, sharedPreferences, testScheduler, testScheduler)
    private val mockSnackbarMessage: SnackbarMessage = Mockito.mock(SnackbarMessage::class.java)
    private val mockPlayers = Mockito.mock(MediatorLiveData::class.java)

    private val testPlayerId: Long = 2020
    private val testTeamId: Long = 220
    private val mockPlayer = Mockito.mock(Player::class.java)
    private val mockPlayerRating = Mockito.mock(PlayerRating::class.java)
    private val mockPlayerTeam = Mockito.mock(PlayerTeam::class.java)
    private val mockTeam = Mockito.mock(Team::class.java)
    private val mockTeamRating = Mockito.mock(TeamRating::class.java)
    private val throwable = Throwable()
    private val playerResponse = Response.success(listOf(mockPlayer))
    private val playerRatingResponse = Response.success(listOf(mockPlayerRating))
    private val playerTeamResponse = Response.success(listOf(mockPlayerTeam))
    private val teamResponse = Response.success(listOf(mockTeam))
    private val teamRatingResponse = Response.success(listOf(mockTeamRating))

    @Before
    fun setUp() {
        // Prepare default rules
        Mockito.`when`(mockPlayer.idplayer).then { testPlayerId }
        Mockito.`when`(mockTeam.idteam).then { testTeamId }
        Mockito.`when`(compositeDisposable.isDisposed).then { false }

        viewModel.snackbarMessage = mockSnackbarMessage
        viewModel.players = mockPlayers as MediatorLiveData<List<Player>>
    }

    @Test
    fun createViewModelTest() {
        // Prepare response and mocks
        `when`(sharedPreferences.readPlayerId()).thenReturn(testPlayerId)
        buildGetPlayerMockResponse(Observable.just(playerResponse.body()), playersRepository, testPlayerId)
        buildGetPlayerRatingMockResponse(Observable.just(playerRatingResponse.body()), playersRepository, testPlayerId)
        buildLoadPlayerTeamDetailsResponse(
                Observable.just(playerTeamResponse.body()), Observable.just(teamResponse.body()),
                Observable.just(teamRatingResponse.body()), playersRepository, teamsRepository)
        // Create viewModel instance
        val viewModel = AccountViewModel(playersRepository, teamsRepository, sharedPreferences, testScheduler, testScheduler)
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify asserts
        assertEquals(testPlayerId, viewModel.actualPlayerId)
        assertEquals(mockPlayer, viewModel.player.get())
        assertEquals(mockPlayerRating, viewModel.playersRating.get())
        assertFalse(viewModel.isErrorVisible.get())
        assertTrue(viewModel.arePlayerDetailsShown.get())
        assertEquals(mockTeam, viewModel.team.get())
        assertEquals(mockTeamRating, viewModel.teamRating.get())
    }

    @Test
    fun onFabClickDetailsScreenTest() {
        // Pre-configure viewModel
        viewModel.arePlayerDetailsShown.set(true)
        // Trigger action
        viewModel.onFabClick()

        // Verify asserts
        assertFalse(viewModel.arePlayerDetailsShown.get())
    }

    @Test
    fun onFabClickSearchTest() {
        // Prepare response
        buildSearchForPlayerResponse(Observable.just(playerResponse.body()), playersRepository)
        // Pre-configure viewModel
        viewModel.arePlayerDetailsShown.set(false)
        viewModel.playerId.set(null)
        // Trigger action
        viewModel.onFabClick()
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify asserts
        assertFalse(viewModel.isErrorVisible.get())
        verify(mockPlayers).value = listOf(mockPlayer)
    }

    @Test
    fun onFabClickGetByIdTest() {
        // Prepare response
        buildGetPlayerMockResponse(Observable.just(playerResponse.body()), playersRepository, testPlayerId)
        // Pre-configure viewModel
        viewModel.arePlayerDetailsShown.set(false)
        viewModel.playerId.set(testPlayerId.toString())
        // Trigger action
        viewModel.onFabClick()
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify asserts
        assertFalse(viewModel.isErrorVisible.get())
        verify(mockPlayers).value = listOf(mockPlayer)
    }

    @Test
    fun loadPlayerRatingFailTest() {
        // Prepare response and mocks
        buildGetPlayerMockResponse(Observable.just(playerResponse.body()), playersRepository, testPlayerId)
        buildGetPlayerRatingMockResponse(Observable.error<List<PlayerRating>>(throwable), playersRepository, testPlayerId)
        buildLoadPlayerTeamDetailsResponse(
                Observable.just(playerTeamResponse.body()), Observable.just(teamResponse.body()),
                Observable.just(teamRatingResponse.body()), playersRepository, teamsRepository)
        // Create viewModel instance
        viewModel.loadPlayerRatingAndTeamDetails(mockPlayer)
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify asserts
        assertEquals(testPlayerId, viewModel.actualPlayerId)
        assertTrue(viewModel.isErrorVisible.get())
        verify(mockSnackbarMessage).setValue(R.string.network_error)
    }

    @Test
    fun loadTeamRatingFailTest() {
        // Prepare response and mocks
        buildGetPlayerMockResponse(Observable.just(playerResponse.body()), playersRepository, testPlayerId)
        buildGetPlayerRatingMockResponse(Observable.just(playerRatingResponse.body()), playersRepository, testPlayerId)
        buildLoadPlayerTeamDetailsResponse(
                Observable.just(playerTeamResponse.body()), Observable.just(teamResponse.body()),
                Observable.error(throwable), playersRepository, teamsRepository)
        // Create viewModel instance
        viewModel.loadPlayerRatingAndTeamDetails(mockPlayer)
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify asserts
        assertEquals(testPlayerId, viewModel.actualPlayerId)
        assertTrue(viewModel.isErrorVisible.get())
        verify(mockSnackbarMessage).setValue(R.string.server_internal_error)
    }

    @Test
    fun loadTeamRatingEmptyListTest() {
        // Prepare response and mocks
        buildGetPlayerMockResponse(Observable.just(playerResponse.body()), playersRepository, testPlayerId)
        buildGetPlayerRatingMockResponse(Observable.just(playerRatingResponse.body()), playersRepository, testPlayerId)
        buildLoadPlayerTeamDetailsResponse(
                Observable.just(playerTeamResponse.body()), Observable.just(teamResponse.body()),
                Observable.just(Response.success<List<TeamRating>>(listOf()).body()), playersRepository, teamsRepository)
        // Create viewModel instance
        viewModel.loadPlayerRatingAndTeamDetails(mockPlayer)
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify asserts
        assertEquals(testPlayerId, viewModel.actualPlayerId)
        assertTrue(viewModel.isErrorVisible.get())
        verify(mockSnackbarMessage).setValue(R.string.no_team_found)
    }


    @Test
    fun bookmarkThisPlayerTest() {
        // Pre-configure viewModel
        viewModel.arePlayerDetailsShown.set(true)
        viewModel.actualPlayerId = testPlayerId
        // Trigger bookmark action
        viewModel.onBookmarkClick()

        // Verify assert
        assertEquals(testPlayerId, viewModel.bookmarkedPlayerId)
        Mockito.verify(sharedPreferences).writePlayerId(testPlayerId)
        verify(mockSnackbarMessage).setValue(R.string.bookmarked_success)
    }

    @Test
    fun getBookmarkedPlayerHappyTest() {
        // Prepare response
        buildGetPlayerMockResponse(Observable.error<List<Player>>(throwable), playersRepository, testPlayerId)
        // Pre-configure viewModel
        viewModel.bookmarkedPlayerId = testPlayerId
        // Trigger action
        viewModel.onBookmarkClick()
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify assert
        assertTrue(viewModel.isErrorVisible.get())
        verify(mockSnackbarMessage).setValue(R.string.network_error)
    }

    @Test
    fun onBookmarkClickFailTest() {
        // Pre-configure viewModel
        viewModel.arePlayerDetailsShown.set(false)
        viewModel.bookmarkedPlayerId = 0
        // Trigger get bookmarked player action
        viewModel.onBookmarkClick()

        // Verify assert
        verify(mockSnackbarMessage).setValue(R.string.bookmark_empty)
    }

    @Test
    fun searchForPlayerHappyTest() {
        // Prepare response
        buildSearchForPlayerResponse(Observable.just(playerResponse.body()), playersRepository)
        // Trigger player search
        viewModel.loadPlayersDetails()
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify assert
        assertFalse(viewModel.isErrorVisible.get())
        verify(mockPlayers).value = listOf(mockPlayer)
    }

    @Test
    fun searchForPlayerEmptyListTest() {
        // Prepare response
        buildSearchForPlayerResponse(Observable.just(Response.success<List<Player>>(listOf()).body()), playersRepository)
        // Trigger player search
        viewModel.loadPlayersDetails()
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify assert
        assertTrue(viewModel.isErrorVisible.get())
        verify(mockSnackbarMessage).setValue(R.string.no_players_found)
    }

    @Test
    fun searchForPlayerFailTest() {
        // Prepare response
        buildSearchForPlayerResponse(Observable.error(throwable), playersRepository)

        // Trigger player search
        viewModel.loadPlayersDetails()
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify assert
        assertTrue(viewModel.isErrorVisible.get())
        verify(mockSnackbarMessage).setValue(R.string.network_error)
    }

    @Test
    fun loadPlayersDetailsHappyTest() {
        // Prepare response
        buildGetPlayerMockResponse(Observable.just(playerResponse.body()), playersRepository, testPlayerId)
        // Trigger player search
        viewModel.loadPlayersDetails(testPlayerId)
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify assert
        assertFalse(viewModel.isErrorVisible.get())
        verify(mockPlayers).value = listOf(mockPlayer)
    }

    @Test
    fun loadPlayersDetailsEmptyListTest() {
        // Prepare response
        buildGetPlayerMockResponse(Observable.just(Response.success<List<Player>>(listOf()).body()), playersRepository, testPlayerId)
        // Trigger player search
        viewModel.loadPlayersDetails(testPlayerId)
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify assert
        assertTrue(viewModel.isErrorVisible.get())
        verify(mockSnackbarMessage).setValue(R.string.no_players_found)
    }

    @Test
    fun loadPlayersDetailsFailTest() {
        // Prepare response
        buildGetPlayerMockResponse(Observable.error(throwable), playersRepository, testPlayerId)
        // Trigger player search
        viewModel.loadPlayersDetails(testPlayerId)
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify assert
        assertTrue(viewModel.isErrorVisible.get())
        verify(mockSnackbarMessage).setValue(R.string.network_error)
    }

    private fun buildSearchForPlayerResponse(playerObservable: Observable<List<Player>?>?, repository: PlayersRepository) {
        Mockito.doReturn(playerObservable).`when`(repository).searchForPlayer(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())
    }

    private fun buildGetPlayerMockResponse(playerObservable: Observable<List<Player>?>?, repository: PlayersRepository, playerId: Long) {
        Mockito.doReturn(playerObservable).`when`(repository).getPlayer(playerId)
    }

    private fun buildGetPlayerRatingMockResponse(playerRatingObservable: Observable<List<PlayerRating>?>?, repository: PlayersRepository, playerId: Long) {
        Mockito.doReturn(playerRatingObservable).`when`(repository).getPlayerRating(playerId)
    }

    private fun buildLoadPlayerTeamDetailsResponse(
            playerTeamObservable: Observable<List<PlayerTeam>?>?, teamObservable: Observable<List<Team>?>?,
            teamRatingObservable: Observable<List<TeamRating>?>?, playersRepository: PlayersRepository,
            teamsRepository: TeamsRepository) {
        Mockito.doReturn(playerTeamObservable).`when`(playersRepository).getPlayerTeam(Mockito.anyLong())
        Mockito.doReturn(teamObservable).`when`(teamsRepository).getTeamById(Mockito.anyLong())
        Mockito.doReturn(teamRatingObservable).`when`(teamsRepository).getTeamRatings(Mockito.anyLong())
    }
}