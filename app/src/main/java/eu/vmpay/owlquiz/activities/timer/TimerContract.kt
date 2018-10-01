package eu.vmpay.owlquiz.activities.timer

import eu.vmpay.owlquiz.BasePresenter
import eu.vmpay.owlquiz.BaseView

/**
 * Created by Andrew on 24/03/2018.
 */
@Deprecated("Architecture change to MVVM")
interface TimerContract {

    interface View : BaseView<Presenter> {

        var isActive: Boolean

        fun showProgress(secondsUntilFinished: Int, currentProgress: Int)

        fun showTimerFinished()

    }

    interface Presenter : BasePresenter<View> {

        fun setTimer(seconds: Long)

        var isTimerStarted: Boolean

        fun startTimer()

        fun resetTimer()

    }
}