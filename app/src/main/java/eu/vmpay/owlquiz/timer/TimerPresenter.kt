package eu.vmpay.owlquiz.timer

import android.content.Context
import android.os.CountDownTimer
import android.preference.Preference
import android.preference.PreferenceManager
import android.util.Log
import eu.vmpay.owlquiz.R

/**
 * Created by Andrew on 24/03/2018.
 */
class TimerPresenter : TimerContract.Presenter {

    override var isTimerStarted: Boolean = false
        get() = isStarted

    private val TAG = "TimerPresenter"

    private var isStarted: Boolean = false
    private var isSoundOn: Boolean
    private var timerLength: Long = 60000
    private var currentSecondsUntilFinished: Long = 0
    private val TICK: Long = 10

    private lateinit var timer: CountDownTimer
    private lateinit var timerView: TimerContract.View

    constructor(appContext: Context) {
        isSoundOn = PreferenceManager.getDefaultSharedPreferences(appContext).getBoolean(
                appContext.getString(R.string.timer_sound_notification_key), false)
        Log.d(TAG, "constructor isSoundOn = $isSoundOn")
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

    override fun takeView(view: TimerContract.View) {
        timerView = view
    }

    override fun dropView() {
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        if (preference?.titleRes == R.string.timer_sound_notification_key)
            isSoundOn = newValue as Boolean
        Log.d(TAG, "onPreferenceChange ${preference?.title} = $newValue")
        return true
    }
}