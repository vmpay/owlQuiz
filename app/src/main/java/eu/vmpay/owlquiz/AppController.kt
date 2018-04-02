package eu.vmpay.owlquiz

import android.content.Context
import eu.vmpay.owlquiz.pref.PrefContract
import eu.vmpay.owlquiz.pref.PrefPresenter
import eu.vmpay.owlquiz.timer.TimerContract
import eu.vmpay.owlquiz.timer.TimerPresenter

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

    fun setUp(applicationContext: Context) {
        createPresenters(applicationContext)
    }


    private fun createPresenters(applicationContext: Context) {
        timerPresenter = TimerPresenter(applicationContext)
        prefPresenter = PrefPresenter()
    }


}