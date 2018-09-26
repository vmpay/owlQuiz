package eu.vmpay.owlquiz.activities.account

import eu.vmpay.owlquiz.BasePresenter
import eu.vmpay.owlquiz.BaseView
import eu.vmpay.owlquiz.repository.Player
import eu.vmpay.owlquiz.repository.PlayerRating
import eu.vmpay.owlquiz.repository.Team
import eu.vmpay.owlquiz.repository.TeamRating

/**
 * Created by Andrew on 12/04/2018.
 */
interface AccountContract {

    interface View : BaseView<Presenter> {

        var isActive: Boolean

        fun showResultList(result: List<Player>)

        fun showNoPlayersFound()

        fun showNetworkError()

        fun showPlayersDetail(player: Player)

        fun showPlayersDetail(player: Player, playerRating: PlayerRating)

        fun showPlayersDetail(team: Team?, teamRating: TeamRating)

        fun showApiError()

        fun clearTeamInfo()

        fun showNoBookmarkYet()
    }

    interface Presenter : BasePresenter<View> {
        fun searchForPlayer(surname: String = "", name: String = "", patronymic: String = "")

        fun loadPlayersDetails(playerId: Long)

        fun loadPlayerAndTeamDetails(playerId: Long)

        fun loadPlayerAndTeamDetails(player: Player)

        fun bookmarkThisPlayer()

        fun getBookmarkedPlayer()
    }
}