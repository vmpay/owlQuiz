package eu.vmpay.owlquiz

import android.os.CountDownTimer
import eu.vmpay.owlquiz.activities.timer.TimerContract
import eu.vmpay.owlquiz.activities.timer.TimerPresenter
import eu.vmpay.owlquiz.soundpool.SoundPlayer
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito
import org.mockito.Mockito.verify

class TimerPresenterTests {

    private val soundPlayer = Mockito.mock(SoundPlayer::class.java)
    private val view: TimerContract.View = Mockito.mock(TimerContract.View::class.java)
    private val presenter: TimerPresenter = TimerPresenter(soundPlayer)

    @Before
    fun setUp() {
        presenter.takeView(view)
        presenter.timer = Mockito.mock(CountDownTimer::class.java)
    }

    @Test
    fun takeViewTest() {
        presenter.takeView(view)
        presenter.dropView()

        assertEquals(view, presenter.timerView)
    }

    @Test
    fun setTimerTest() {
        var timerLength: Long = 20_000
        presenter.setTimer(timerLength)
        assertEquals(timerLength, presenter.timerLength)

        timerLength = 30_000
        presenter.setTimer(timerLength)
        assertEquals(timerLength, presenter.timerLength)

        timerLength = 60_000
        presenter.setTimer(timerLength)
        assertEquals(timerLength, presenter.timerLength)
    }

    @Test
    fun pauseTimerTest() {
        presenter.isStarted = true
        presenter.startTimer()

        verify(presenter.timer).cancel()
        assertFalse(presenter.isStarted)
    }

    @Test
    fun resetTimerTest() {
        presenter.resetTimer()

        assertEquals(0, presenter.currentSecondsUntilFinished)
        assertFalse(presenter.isStarted)
        verify(presenter.timer).cancel()
        verify(view).showProgress(anyInt(), eq(0))
    }

    @After
    fun tearDown() {
    }
}