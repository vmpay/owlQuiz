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
    private var timerLength: Long = 60000
    private var currentSecondsUntilFinished: Long = 0
    private lateinit var timer: CountDownTimer
    private val TICK: Long = 10

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
                    if (currentSecondsUntilFinished == 0L) timerLength + TICK
                    else currentSecondsUntilFinished, TICK) {
                override fun onTick(millisUntilFinished: Long) {
                    if (millisUntilFinished / TICK == 1L) {
                        Log.d(TAG, "finished $millisUntilFinished")
                        currentSecondsUntilFinished = 0
                        isStarted = false
                        timerView.showTimerFinished()
                    } else {
                        Log.d(TAG, "tick $millisUntilFinished")
                        currentSecondsUntilFinished = millisUntilFinished
                        timerView.showProgress((millisUntilFinished - TICK).toInt(), (timerLength - millisUntilFinished + 2 * TICK).toInt())
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