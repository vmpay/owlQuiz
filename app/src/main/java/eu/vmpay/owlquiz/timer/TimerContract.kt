package eu.vmpay.owlquiz.timer

import android.preference.Preference
import eu.vmpay.owlquiz.BasePresenter
import eu.vmpay.owlquiz.BaseView

/**
 * Created by Andrew on 24/03/2018.
 */
interface TimerContract {

    interface View : BaseView<Presenter> {

        var isActive: Boolean

        fun showProgress(secondsUntilFinished: Int, currentProgress: Int)

        fun showTimerFinished()

    }

    interface Presenter : Preference.OnPreferenceChangeListener, BasePresenter<View> {

        fun setTimer(seconds: Long)

        var isTimerStarted: Boolean

        fun startTimer()

        fun resetTimer()

    }
}