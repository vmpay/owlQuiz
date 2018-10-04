package eu.vmpay.owlquiz.activities.account

import androidx.recyclerview.widget.DiffUtil
import eu.vmpay.owlquiz.repository.Player

class PlayerDiffCallback : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem.idplayer == newItem.idplayer
    }

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem == newItem
    }

}
