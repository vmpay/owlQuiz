package eu.vmpay.owlquiz.activities.timer

import android.os.CountDownTimer
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import eu.vmpay.owlquiz.tools.SoundPlayer

class TimerViewModel(private val soundPlayer: SoundPlayer) : ViewModel() {
    private val TAG = "TimerViewModel"

    var areRadioButtonsEnabled = ObservableBoolean(true)
    var millisUntilFinish = ObservableInt(60_000)
    var currentProgress = ObservableInt(0)
    var maxProgress = ObservableInt(60_000) // timer length
    var isStarted = ObservableBoolean(false)

    private val TICK: Long = 20
    var tenSecondSoundPlayed = false
    lateinit var timer: CountDownTimer

    fun setTimerLength(millis: Int) {
        currentProgress.set(0)
        maxProgress.set(millis)
        millisUntilFinish.set(millis)
    }

    fun startTimer() {
        if (isStarted.get()) {
            timer.cancel()
        } else {
            timer = resetTimerObject()
            tenSecondSoundPlayed = false
            timer.start()
            areRadioButtonsEnabled.set(false)

            // play starting sound
            if (millisUntilFinish.get() == maxProgress.get()) {
                soundPlayer.playSound()
            }
        }
        isStarted.set(!isStarted.get())
    }

    fun resetTimer() {
        millisUntilFinish.set(maxProgress.get())
        currentProgress.set(0)
        isStarted.set(false)
        timer.cancel()
        areRadioButtonsEnabled.set(true)
    }

    fun ifTenSecondsLeft(millisUntilFinished: Long): Boolean {
        return millisUntilFinished / 1000 == 9L
    }

    private fun resetTimerObject(): CountDownTimer {
        return object : CountDownTimer(
                if (millisUntilFinish.get() == 0) maxProgress.get() + TICK
                else millisUntilFinish.get().toLong(), TICK) {
            override fun onTick(millisUntilTimerFinished: Long) {
                if (millisUntilTimerFinished / (2 * TICK) <= 1L) {
                    Log.d(TAG, "finished $millisUntilTimerFinished")
                    millisUntilFinish.set(0)
                    isStarted.set(false)
                    currentProgress.set(maxProgress.get())
                    areRadioButtonsEnabled.set(true)
                    // btnStart.text = getString(btn_start)

                    // play sound when time is up
                    soundPlayer.playSound()
                } else {
                    Log.d(TAG, "tick $millisUntilTimerFinished")
                    millisUntilFinish.set(millisUntilTimerFinished.toInt())
                    currentProgress.set((maxProgress.get() - millisUntilTimerFinished + 2 * TICK).toInt())

                    // play sound when 10 seconds left
                    if (maxProgress.get() > 30_000 && ifTenSecondsLeft(millisUntilTimerFinished) && !tenSecondSoundPlayed) {
                        tenSecondSoundPlayed = true
                        soundPlayer.playSound()
                    }
                }
            }

            override fun onFinish() {
            }
        }
    }
}