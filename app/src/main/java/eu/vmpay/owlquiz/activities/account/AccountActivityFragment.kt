package eu.vmpay.owlquiz.activities.account

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import eu.vmpay.owlquiz.AppController
import eu.vmpay.owlquiz.R
import eu.vmpay.owlquiz.rest.models.Player
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.view.*

/**
 * A placeholder fragment containing a simple view.
 */
class AccountActivityFragment : Fragment(), AccountContract.View {
    private val TAG: String = "AccountActivityFragment"

    override var isActive: Boolean = false
        get() = isAdded

    private lateinit var presenter: AccountPresenter

    private lateinit var adapter: PlayersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        presenter = AppController.getInstance().accountPresenter
        presenter.takeView(this)

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
        adapter = PlayersAdapter(mutableListOf())
        view.recyclerView.adapter = adapter

        return view
    }

    private fun query(view: View) {
        if (view.etPlayerId.text.isEmpty())
            presenter.searchForPlayer(etSurname.text.toString(), etName.text.toString(), etPatronymic.text.toString())
        else
            presenter.loadPlayersDetails(etPlayerId.text.toString().toLong())
    }

    override fun onDestroyView() {
        presenter.dropView()
        super.onDestroyView()
    }

    override fun showResultList(result: List<Player>) {
        Log.d(TAG, "showResultList list size $result")
        adapter.replaceList(result)
        adapter.notifyDataSetChanged()
        tvError.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    override fun showNoPlayersFound() {
        Log.d(TAG, "showNoPlayersFound")
        tvError.text = getString(R.string.no_players_found)
        tvError.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    override fun showNetworkError() {
        Log.d(TAG, "showNetworkError")
        tvError.text = getString(R.string.network_error)
        tvError.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    override fun showPlayersDetail(player: Player) {
        Log.d(TAG, "showPlayersDetail $player")
        adapter.replaceList(player)
        adapter.notifyDataSetChanged()
        tvError.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }
}
