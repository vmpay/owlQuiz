package eu.vmpay.owlquiz.activities.account

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.vmpay.owlquiz.R
import eu.vmpay.owlquiz.repository.Player
import kotlinx.android.synthetic.main.query_list_content.view.*

class PlayersAdapter(private val playersList: MutableList<Player>, private val presenter: AccountContract.Presenter) :
        RecyclerView.Adapter<PlayersAdapter.PlayerViewHolder>() {

    class PlayerViewHolder(view: View) : RecyclerView.ViewHolder(view)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PlayersAdapter.PlayerViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.query_list_content, parent, false) as View
        // set the view's size, margins, paddings and layout parameters
        return PlayerViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        // - get element from your data set at this position
        // - replace the contents of the view with that element
        holder.itemView.tvID.text = playersList[position].idplayer.toString()
        val nameText = "${playersList[position].surname} ${playersList[position].name} ${playersList[position].patronymic}"
        holder.itemView.tvName.text = nameText
        holder.itemView.setOnClickListener { presenter.loadPlayerAndTeamDetails(playersList[position]) }
    }

    // Return the size of your data set (invoked by the layout manager)
    override fun getItemCount() = playersList.size

    // Replace current list with a new one
    fun replaceList(list: List<Player>) {
        playersList.clear()
        playersList.addAll(list)
    }

    // Replace current list with one single item
    fun replaceList(player: Player) {
        playersList.clear()
        playersList.addAll(listOf(player))
    }
}