package eu.vmpay.owlquiz

import eu.vmpay.owlquiz.activities.account.AccountContract
import eu.vmpay.owlquiz.activities.account.AccountPresenter
import eu.vmpay.owlquiz.repository.*
import eu.vmpay.owlquiz.utils.SharedPreferences
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import retrofit2.Response

class AccountPresenterTests {
    private val testScheduler: TestScheduler = TestScheduler()
    private val compositeDisposable: CompositeDisposable = Mockito.mock(CompositeDisposable::class.java)
    private val view: AccountContract.View = Mockito.mock(AccountContract.View::class.java)
    private val playersRepository: PlayersRepository = Mockito.mock(PlayersRepository::class.java)
    private val teamsRepository: TeamsRepository = Mockito.mock(TeamsRepository::class.java)
    private val sharedPreferences: SharedPreferences = Mockito.mock(SharedPreferences::class.java)
    private val presenter: AccountPresenter = AccountPresenter(playersRepository, teamsRepository, sharedPreferences, testScheduler, testScheduler)

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
        `when`(mockPlayer.idplayer).then { testPlayerId }
        `when`(mockTeam.idteam).then { testTeamId }
        `when`(view.isActive).then { true }
        `when`(compositeDisposable.isDisposed).then { false }

        // Required for late init view
        presenter.takeView(view)
    }

    @Test
    fun takeViewHappyTest() {
        // Prepare response and mocks
        buildGetPlayerMockResponse(Observable.just(playerResponse.body()), playersRepository, testPlayerId)
        buildGetPlayerRatingMockResponse(Observable.just(playerRatingResponse.body()), playersRepository, testPlayerId)
        buildLoadPlayerTeamDetailsResponse(
                Observable.just(playerTeamResponse.body()), Observable.just(teamResponse.body()),
                Observable.just(teamRatingResponse.body()), playersRepository, teamsRepository)
        // Pre-configure presenter
        presenter.bookmarkPlayerId = testPlayerId

        // Trigger take view
        presenter.takeView(view)
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify asserts
        assertEquals(view, presenter.accountView)
        assertNotEquals(compositeDisposable, presenter.compositeDisposable)
        assertEquals(testPlayerId, presenter.actualPlayerId)
        verify(view).showPlayersDetail(mockPlayer, mockPlayerRating)
        verify(view).showTeamDetails(mockTeam, mockTeamRating)
    }

    @Test
    fun noTeamFoundTest() {
        // Prepare response and mocks
        buildGetPlayerMockResponse(Observable.just(playerResponse.body()), playersRepository, testPlayerId)
        buildGetPlayerRatingMockResponse(Observable.just(playerRatingResponse.body()), playersRepository, testPlayerId)
        buildLoadPlayerTeamDetailsResponse(
                Observable.just(playerTeamResponse.body()), Observable.just(teamResponse.body()),
                Observable.just(Response.success<List<TeamRating>>(listOf()).body()), playersRepository, teamsRepository)
        // Pre-configure presenter
        presenter.bookmarkPlayerId = testPlayerId

        // Trigger take view
        presenter.takeView(view)
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify asserts
        assertEquals(view, presenter.accountView)
        assertNotEquals(compositeDisposable, presenter.compositeDisposable)
        assertEquals(testPlayerId, presenter.actualPlayerId)
        verify(view).showPlayersDetail(mockPlayer, mockPlayerRating)
        verify(view).showNoTeamFound()
    }

    @Test
    fun takeViewGetPlayerFailTest() {
        // Prepare response and mocks
        buildGetPlayerMockResponse(Observable.error<List<Player>>(throwable), playersRepository, testPlayerId)
        // Pre-configure presenter
        presenter.bookmarkPlayerId = testPlayerId

        // Trigger take view
        presenter.takeView(view)
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify asserts
        assertEquals(view, presenter.accountView)
        assertNotEquals(compositeDisposable, presenter.compositeDisposable)
        verify(view).showNetworkError()
    }

    @Test
    fun takeViewGetPlayerRatingFailTest() {
        // Prepare response and mocks
        buildGetPlayerMockResponse(Observable.just(playerResponse.body()), playersRepository, testPlayerId)
        buildGetPlayerRatingMockResponse(Observable.error<List<PlayerRating>>(throwable), playersRepository, testPlayerId)
        buildLoadPlayerTeamDetailsResponse(
                Observable.error<List<PlayerTeam>>(throwable), Observable.error<List<Team>>(throwable),
                Observable.error<List<TeamRating>>(throwable), playersRepository, teamsRepository)
        // Pre-configure presenter
        presenter.bookmarkPlayerId = testPlayerId

        // Trigger take view
        presenter.takeView(view)
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify asserts
        assertEquals(view, presenter.accountView)
        assertNotEquals(compositeDisposable, presenter.compositeDisposable)
        assertEquals(testPlayerId, presenter.actualPlayerId)
        verify(view).showApiError()
        verify(view).clearTeamInfo()
        verify(view).showNetworkError()
    }

    @Test
    fun dropViewTest() {
        // Pre-configure presenter
        presenter.compositeDisposable = compositeDisposable
        // Trigger drop view
        presenter.dropView()

        // Verify assert
        verify(compositeDisposable).dispose()
    }

    @Test
    fun bookmarkThisPlayerTest() {
        // Pre-configure presenter
        presenter.actualPlayerId = testPlayerId
        // Trigger bookmark action
        presenter.bookmarkThisPlayer()

        // Verify assert
        assertEquals(testPlayerId, presenter.bookmarkPlayerId)
        verify(sharedPreferences).writePlayerId(testPlayerId)
    }

    @Test
    fun getBookmarkedPlayerHappyTest() {
        // Prepare response
        buildGetPlayerMockResponse(Observable.error<List<Player>>(throwable), playersRepository, testPlayerId)
        // Pre-configure presenter
        presenter.bookmarkPlayerId = testPlayerId
        // Trigger action
        presenter.getBookmarkedPlayer()
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify assert
        verify(view).showNetworkError()
    }

    @Test
    fun getBookmarkedPlayerFailTest() {
        // Pre-configure presenter
        presenter.bookmarkPlayerId = 0
        // Trigger get bookmarked player action
        presenter.getBookmarkedPlayer()
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify assert
        verify(view).showNoBookmarkYet()
    }

    @Test
    fun searchForPlayerHappyTest() {
        // Prepare response
        buildSearchForPlayerResponse(Observable.just(playerResponse.body()), playersRepository)
        // Trigger player search
        presenter.searchForPlayer()
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify assert
        verify(view).showResultList(listOf(mockPlayer))
    }

    @Test
    fun searchForPlayerEmptyListTest() {
        // Prepare response
        buildSearchForPlayerResponse(Observable.just(Response.success<List<Player>>(listOf()).body()), playersRepository)
        // Trigger player search
        presenter.searchForPlayer()
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify assert
        verify(view).showNoPlayersFound()
    }

    @Test
    fun searchForPlayerFailTest() {
        // Prepare response
        buildSearchForPlayerResponse(Observable.error(throwable), playersRepository)

        // Trigger player search
        presenter.searchForPlayer()
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify assert
        verify(view).showNetworkError()
    }


    @Test
    fun loadPlayersDetailsHappyTest() {
        // Prepare response
        buildGetPlayerMockResponse(Observable.just(playerResponse.body()), playersRepository, testPlayerId)
        // Trigger player search
        presenter.loadPlayersDetails(testPlayerId)
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify assert
        verify(view).showPlayersDetail(mockPlayer)
    }

    @Test
    fun loadPlayersDetailsEmptyListTest() {
        // Prepare response
        buildGetPlayerMockResponse(Observable.just(Response.success<List<Player>>(listOf()).body()), playersRepository, testPlayerId)
        // Trigger player search
        presenter.loadPlayersDetails(testPlayerId)
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify assert
        verify(view).showNoPlayersFound()
    }

    @Test
    fun loadPlayersDetailsFailTest() {
        // Prepare response
        buildGetPlayerMockResponse(Observable.error(throwable), playersRepository, testPlayerId)
        // Trigger player search
        presenter.loadPlayersDetails(testPlayerId)
        // Trigger Schedulers
        testScheduler.triggerActions()

        // Verify assert
        verify(view).showNetworkError()
    }

    private fun buildSearchForPlayerResponse(playerObservable: Observable<List<Player>?>?, repository: PlayersRepository) {
        doReturn(playerObservable).`when`(repository).searchForPlayer(anyString(), anyString(), anyString())
    }

    private fun buildGetPlayerMockResponse(playerObservable: Observable<List<Player>?>?, repository: PlayersRepository, playerId: Long) {
        doReturn(playerObservable).`when`(repository).getPlayer(playerId)
    }

    private fun buildGetPlayerRatingMockResponse(playerRatingObservable: Observable<List<PlayerRating>?>?, repository: PlayersRepository, playerId: Long) {
        doReturn(playerRatingObservable).`when`(repository).getPlayerRating(playerId)
    }

    private fun buildLoadPlayerTeamDetailsResponse(
            playerTeamObservable: Observable<List<PlayerTeam>?>?, teamObservable: Observable<List<Team>?>?,
            teamRatingObservable: Observable<List<TeamRating>?>?, playersRepository: PlayersRepository,
            teamsRepository: TeamsRepository) {
        doReturn(playerTeamObservable).`when`(playersRepository).getPlayerTeam(anyLong())
        doReturn(teamObservable).`when`(teamsRepository).getTeamById(anyLong())
        doReturn(teamRatingObservable).`when`(teamsRepository).getTeamRatings(anyLong())
    }
}
