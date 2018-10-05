package eu.vmpay.owlquiz.utils

import android.content.Context
import eu.vmpay.owlquiz.activities.account.AccountViewModelFactory
import eu.vmpay.owlquiz.activities.timer.TimerViewModelFactory
import eu.vmpay.owlquiz.repository.AppDatabase
import eu.vmpay.owlquiz.repository.PlayersRepository
import eu.vmpay.owlquiz.repository.RatingChgkService
import eu.vmpay.owlquiz.repository.TeamsRepository
import eu.vmpay.owlquiz.tools.SharedPreferences
import eu.vmpay.owlquiz.tools.SoundPlayer

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {
    fun getPlayersRepository(context: Context) = PlayersRepository.getInstance(
            RatingChgkService.getInstance(), AppDatabase.getInstance(context).playerDao())

    fun getTeamRepository(context: Context) = TeamsRepository.getInstance(
            RatingChgkService.getInstance(), AppDatabase.getInstance(context).teamDao())

    fun getSoundPlayer(context: Context) = SoundPlayer.getInstance(context, getSharedPrefs(context))

    fun getSharedPrefs(context: Context) = SharedPreferences.getInstance(context)

    fun provideTimerViewModelFactory(context: Context) = TimerViewModelFactory(getSoundPlayer(context))

    fun provideAccountViewModelFactory(context: Context) = AccountViewModelFactory(
            getPlayersRepository(context), getTeamRepository(context), getSharedPrefs(context))
}