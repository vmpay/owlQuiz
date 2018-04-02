package eu.vmpay.owlquiz

import android.content.Context
import eu.vmpay.owlquiz.activities.pref.PrefContract
import eu.vmpay.owlquiz.activities.pref.PrefPresenter
import eu.vmpay.owlquiz.activities.timer.TimerContract
import eu.vmpay.owlquiz.activities.timer.TimerPresenter
import eu.vmpay.owlquiz.soundpool.SoundPlayer
import eu.vmpay.owlquiz.soundpool.SoundPlayerContract

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

    /*---------------------SERVICES---------------------*/
    lateinit var soundPlayer: SoundPlayerContract

    fun setUp(applicationContext: Context) {
        buildServices(applicationContext)
        createPresenters()
    }

    private fun buildServices(applicationContext: Context) {
        soundPlayer = SoundPlayer(applicationContext)
    }


    private fun createPresenters() {
        timerPresenter = TimerPresenter(soundPlayer)
        prefPresenter = PrefPresenter()
    }


}