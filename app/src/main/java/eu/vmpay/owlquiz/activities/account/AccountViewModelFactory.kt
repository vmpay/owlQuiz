package eu.vmpay.owlquiz.activities.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.vmpay.owlquiz.repository.PlayersRepository
import eu.vmpay.owlquiz.repository.TeamsRepository
import eu.vmpay.owlquiz.utils.SharedPreferences

class AccountViewModelFactory(
        private val playersRepository: PlayersRepository, private val teamsRepository: TeamsRepository,
        private val sharedPreferences: SharedPreferences
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AccountViewModel(playersRepository, teamsRepository, sharedPreferences) as T
    }
}