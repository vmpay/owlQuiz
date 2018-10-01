package eu.vmpay.owlquiz.activities.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.vmpay.owlquiz.soundpool.SoundPlayer

class TimerViewModelFactory(private val soundPlayer: SoundPlayer) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TimerViewModel(soundPlayer) as T
    }
}