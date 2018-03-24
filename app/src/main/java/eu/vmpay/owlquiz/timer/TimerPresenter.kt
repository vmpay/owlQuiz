package eu.vmpay.owlquiz.timer

import android.os.CountDownTimer
import android.util.Log

/**
 * Created by Andrew on 24/03/2018.
 */
class TimerPresenter(val timerView: TimerContract.View) : TimerContract.Presenter {

    override var isTimerStarted: Boolean = false
        get() = isStarted

    private val TAG = "TimerPresenter"

    private var isStarted: Boolean = false
    private var timerLength: Long = 60
    private var currentSecondsUntilFinished: Long = 0
    private lateinit var timer: CountDownTimer

    init {
        timerView.presenter = this
    }

    override fun start() {
        Log.d(TAG, "start")
    }

    override fun setTimer(seconds: Long) {
        timerLength = seconds
    }

    override fun startTimer() {
        if (isStarted) {
            timer.cancel()
        } else {
            timer = object : CountDownTimer(
                    if (currentSecondsUntilFinished == 0L) timerLength * 1000 + 1000
                    else currentSecondsUntilFinished, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    if (millisUntilFinished / 1000 == 1L) {
                        currentSecondsUntilFinished = 0
                        isStarted = false
                        timerView.showTimerFinished()
                    } else {
                        currentSecondsUntilFinished = millisUntilFinished
                        timerView.showProgress((millisUntilFinished / 1000 - 1).toInt(), (timerLength - millisUntilFinished / 1000 + 1).toInt())
                    }
                }

                override fun onFinish() {
                }
            }
            timer.start()
        }
        isStarted = !isStarted
    }

    override fun resetTimer() {
        currentSecondsUntilFinished = 0
        isStarted = false
        timer.cancel()
        timerView.showProgress(timerLength.toInt(), 0)
    }
}