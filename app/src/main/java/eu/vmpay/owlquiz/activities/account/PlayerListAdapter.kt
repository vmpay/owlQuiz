package eu.vmpay.owlquiz.activities.account

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.vmpay.owlquiz.databinding.QueryListContentBinding
import eu.vmpay.owlquiz.repository.Player

class PlayerListAdapter(private val viewModel: AccountViewModel) : ListAdapter<Player, PlayerListAdapter.ViewHolder>(PlayerDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = getItem(position)
        holder.apply {
            bind(createOnClickListener(player.idplayer), player)
            itemView.tag = player
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(QueryListContentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    private fun createOnClickListener(playerId: Long): View.OnClickListener {
        return View.OnClickListener {
            Log.d("PlayerListAdapter", "player id click $playerId")
            Log.d("PlayerListAdapter", "player  click ${it.tag}")
            viewModel.loadPlayerRatingAndTeamDetails(it.tag as Player)

        }
    }

    class ViewHolder(private val binding: QueryListContentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, item: Player) {
            binding.apply {
                clickListener = listener
                player = item
                executePendingBindings()
            }
        }
    }
}