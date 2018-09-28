package eu.vmpay.owlquiz

import android.content.Context
import androidx.room.Room
import eu.vmpay.owlquiz.activities.account.AccountPresenter
import eu.vmpay.owlquiz.activities.pref.PrefContract
import eu.vmpay.owlquiz.activities.pref.PrefPresenter
import eu.vmpay.owlquiz.activities.timer.TimerContract
import eu.vmpay.owlquiz.activities.timer.TimerPresenter
import eu.vmpay.owlquiz.repository.AppDatabase
import eu.vmpay.owlquiz.repository.PlayersRepository
import eu.vmpay.owlquiz.repository.RatingChgkService
import eu.vmpay.owlquiz.repository.TeamsRepository
import eu.vmpay.owlquiz.soundpool.SoundPlayer
import eu.vmpay.owlquiz.soundpool.SoundPlayerContract
import eu.vmpay.owlquiz.utils.SharedPreferences
import eu.vmpay.owlquiz.utils.SharedPreferencesContract

/**
 * Created by Andrew on 30/03/2018.
 */
class AppController {

    private val TAG = "AppController"

    companion object {

        private var INSTANCE: AppController? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         *
         * @return the [AppController] instance
         */
        @JvmStatic
        fun getInstance(): AppController {
            return INSTANCE ?: AppController()
                    .apply { INSTANCE = this }
        }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }

    /*---------------------PRESENTERS---------------------*/
    lateinit var prefPresenter: PrefContract.Presenter
    lateinit var timerPresenter: TimerContract.Presenter
    lateinit var accountPresenter: AccountPresenter

    /*---------------------SERVICES---------------------*/
    lateinit var soundPlayer: SoundPlayerContract
    private lateinit var retrofit: RatingChgkService
    private lateinit var appDatabase: AppDatabase
    lateinit var playersRepository: PlayersRepository
    lateinit var teamsRepository: TeamsRepository
    lateinit var sharedPreferences: SharedPreferencesContract

    fun setUp(applicationContext: Context) {
        buildServices(applicationContext)
        createPresenters()
    }

    private fun buildServices(applicationContext: Context) {
        soundPlayer = SoundPlayer(applicationContext)
        retrofit = RatingChgkService.create()
        appDatabase = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "owl_quiz_database").build()
        playersRepository = PlayersRepository(retrofit, appDatabase.playerDao())
        teamsRepository = TeamsRepository(retrofit, appDatabase.teamDao())
        sharedPreferences = SharedPreferences(applicationContext)
    }

    private fun createPresenters() {
        timerPresenter = TimerPresenter(soundPlayer)
        prefPresenter = PrefPresenter()
        accountPresenter = AccountPresenter(playersRepository, teamsRepository, sharedPreferences)
    }


}