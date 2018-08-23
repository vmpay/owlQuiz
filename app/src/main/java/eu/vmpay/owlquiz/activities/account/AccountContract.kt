package eu.vmpay.owlquiz.activities.account

import eu.vmpay.owlquiz.BasePresenter
import eu.vmpay.owlquiz.BaseView
import eu.vmpay.owlquiz.rest.models.Player

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
    }

    interface Presenter : BasePresenter<View> {
        fun searchForPlayer(surname: String = "", name: String = "", patronymic: String = "")

        fun loadPlayersDetails(playerId: Long)
    }
}