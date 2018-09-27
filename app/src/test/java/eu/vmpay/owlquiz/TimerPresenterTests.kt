package eu.vmpay.owlquiz

import android.os.CountDownTimer
import eu.vmpay.owlquiz.activities.timer.TimerContract
import eu.vmpay.owlquiz.activities.timer.TimerPresenter
import eu.vmpay.owlquiz.soundpool.SoundPlayer
import org.junit.Assert.*
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
        // Replaces mocked timer inside the presenter
        presenter.timer = Mockito.mock(CountDownTimer::class.java)
        // Required for late init view
        presenter.takeView(view)
    }

    @Test
    fun takeViewTest() {
        // Trigger actions
        presenter.takeView(view)
        presenter.dropView()

        // Verify asserts
        assertEquals(view, presenter.timerView)
    }

    @Test
    fun setTimerTest() {
        var timerLength: Long = 20_000
        // Trigger action
        presenter.setTimer(timerLength)
        // Verify asserts
        assertEquals(timerLength, presenter.timerLength)

        timerLength = 30_000
        // Trigger action
        presenter.setTimer(timerLength)
        // Verify asserts
        assertEquals(timerLength, presenter.timerLength)

        timerLength = 60_000
        // Trigger action
        presenter.setTimer(timerLength)
        // Verify asserts
        assertEquals(timerLength, presenter.timerLength)
    }

    @Test
    fun pauseTimerTest() {
        // Pre-configure presenter
        presenter.isStarted = true
        // Trigger action
        presenter.startTimer()

        // Verify asserts
        verify(presenter.timer).cancel()
        assertFalse(presenter.isTimerStarted)
    }

    @Test
    fun resetTimerTest() {
        // Trigger action
        presenter.resetTimer()

        // Verify asserts
        assertEquals(0, presenter.currentSecondsUntilFinished)
        assertFalse(presenter.isTimerStarted)
        verify(presenter.timer).cancel()
        verify(view).showProgress(anyInt(), eq(0))
    }

    @Test
    fun ifTenSecondsLeftTest() {
        // Verify asserts
        assertFalse(presenter.ifTenSecondsLeft(10_000))
        assertTrue(presenter.ifTenSecondsLeft(9_000))
    }
}