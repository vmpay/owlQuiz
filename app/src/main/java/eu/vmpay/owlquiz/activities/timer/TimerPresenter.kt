package eu.vmpay.owlquiz.activities.timer

import android.os.CountDownTimer
import android.util.Log
import eu.vmpay.owlquiz.soundpool.SoundPlayerContract

/**
 * Created by Andrew on 24/03/2018.
 */
class TimerPresenter(private val soundPlayer: SoundPlayerContract) : TimerContract.Presenter {

    override var isTimerStarted: Boolean = false
        get() = isStarted

    private val TAG = "TimerPresenter"

    private var isStarted: Boolean = false
    private var timerLength: Long = 60000
    private var currentSecondsUntilFinished: Long = 0
    private val TICK: Long = 20
    private var tenSecondSoundPlayed = false

    private lateinit var timer: CountDownTimer
    private lateinit var timerView: TimerContract.View

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
                    if (millisUntilFinished / (2 * TICK) <= 1L) {
                        Log.d(TAG, "finished $millisUntilFinished")
                        currentSecondsUntilFinished = 0
                        isStarted = false
                        timerView.showTimerFinished()

                        // play sound when time is up
                        soundPlayer.playSound()
                    } else {
                        Log.d(TAG, "tick $millisUntilFinished")
                        currentSecondsUntilFinished = millisUntilFinished
                        timerView.showProgress((millisUntilFinished - TICK).toInt(), (timerLength - millisUntilFinished + 2 * TICK).toInt())

                        // play sound when 10 seconds left
                        if (timerLength > 30_000 && ifTenSecondsLeft(millisUntilFinished) && !tenSecondSoundPlayed) {
                            tenSecondSoundPlayed = true
                            soundPlayer.playSound()
                        }
                    }
                }

                override fun onFinish() {
                }
            }
            tenSecondSoundPlayed = false
            timer.start()

            // play starting sound
            if (currentSecondsUntilFinished == 0L) {
                soundPlayer.playSound()
            }
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

    fun ifTenSecondsLeft(millisUntilFinished: Long): Boolean {
        return millisUntilFinished / 1000 == 9L
    }
}