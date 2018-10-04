package eu.vmpay.owlquiz.activities.account

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import eu.vmpay.owlquiz.AppController
import eu.vmpay.owlquiz.R
import eu.vmpay.owlquiz.repository.Player
import eu.vmpay.owlquiz.repository.PlayerRating
import eu.vmpay.owlquiz.repository.Team
import eu.vmpay.owlquiz.repository.TeamRating
import eu.vmpay.owlquiz.utils.InputManagerUtils
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.view.*

/**
 * A placeholder fragment containing a simple view.
 */
@Deprecated("Architecture change to MVVM")
class AccountActivityFragment : Fragment(), AccountContract.View {

    private val TAG: String = "AccountActivityFragment"

    override var isActive: Boolean = false
        get() = isAdded

    private lateinit var presenter: AccountContract.Presenter

    private lateinit var adapter: PlayersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        presenter = AppController.getInstance().accountPresenter
        presenter.takeView(this)
        setHasOptionsMenu(true)

        view.fab.setOnClickListener {
            query(view)
        }
        view.etPlayerId.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    query(view)
                    true
                }
                else -> false
            }
        }
        adapter = PlayersAdapter(mutableListOf(), presenter)
        view.recyclerView.adapter = adapter

        return view
    }

    private fun query(view: View) {
        if (view.llDetails.visibility == VISIBLE) {
            view.llDetails.visibility = GONE
            view.llSearch.visibility = VISIBLE
        } else {
            if (view.etPlayerId.text.isEmpty())
                presenter.searchForPlayer(etSurname.text.toString(), etName.text.toString(), etPatronymic.text.toString())
            else
                presenter.loadPlayersDetails(etPlayerId.text.toString().toLong())
        }
    }

    override fun onDestroyView() {
        presenter.dropView()
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_accout_activity, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_bookmark -> {
            // User chose the "Settings" item, show the app settings UI...
            Log.d(TAG, "Option menu action bookmark click")
            if (llDetails.visibility == VISIBLE) {
                presenter.bookmarkThisPlayer()
                Snackbar.make(view!!, R.string.bookmarked_success, LENGTH_SHORT).show()
            } else
                presenter.getBookmarkedPlayer()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun showResultList(result: List<Player>) {
        Log.d(TAG, "showResultList list size $result")
        adapter.replaceList(result)
        adapter.notifyDataSetChanged()
        tvError.visibility = GONE
        recyclerView.visibility = VISIBLE
    }

    override fun showNoPlayersFound() {
        Log.d(TAG, "showNoPlayersFound")
        tvError.text = getString(R.string.no_players_found)
        tvError.visibility = VISIBLE
        recyclerView.visibility = GONE
    }

    override fun showNoTeamFound() {
        Log.d(TAG, "showNoPlayersFound")
        tvError.text = getString(R.string.no_team_found)
        tvError.visibility = VISIBLE
        recyclerView.visibility = GONE
    }

    override fun showNetworkError() {
        Log.d(TAG, "showNetworkError")
        tvError.text = getString(R.string.network_error)
        tvError.visibility = VISIBLE
        recyclerView.visibility = GONE
    }

    override fun showPlayersDetail(player: Player) {
        Log.d(TAG, "showTeamDetails $player")
        adapter.replaceList(player)
        adapter.notifyDataSetChanged()
        tvError.visibility = GONE
        recyclerView.visibility = VISIBLE
    }

    override fun showPlayersDetail(player: Player, playerRating: PlayerRating) {
        InputManagerUtils.hideKeyboard(activity!!)
        llSearch.visibility = GONE
        llDetails.visibility = VISIBLE
        val fullName = "${player.surname} ${player.name} ${player.patronymic}"
        tvFullName.text = fullName
        tvRating.text = playerRating.rating.toString()
        tvRatingPosition.text = playerRating.rating_position.toString()
        tvRatingDate.text = playerRating.date
    }

    override fun showTeamDetails(team: Team?, teamRating: TeamRating) {
        if (team != null) {
            tvTeamName.text = team.name
            tvCountry.text = team.country_name
            tvTown.text = team.town
            tvTeamRating.text = teamRating.rating.toString()
            tvTeamRatingPosition.text = teamRating.rating_position.toString()
            tvTeamRatingDate.text = teamRating.date
        }
    }

    override fun showApiError() {
        Log.d(TAG, "showApiError")
        tvError.text = getString(R.string.server_internal_error)
        tvError.visibility = VISIBLE
        recyclerView.visibility = GONE
    }

    override fun clearTeamInfo() {
        tvTeamName.text = getString(R.string.not_available)
        tvCountry.text = getString(R.string.not_available)
        tvTown.text = getString(R.string.not_available)
        tvTeamRating.text = getString(R.string.not_available)
        tvTeamRatingPosition.text = getString(R.string.not_available)
        tvTeamRatingDate.text = getString(R.string.not_available)
    }

    override fun showNoBookmarkYet() {
        Snackbar.make(view!!, R.string.bookmark_empty, LENGTH_SHORT).show()
    }
}
