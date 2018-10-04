package eu.vmpay.owlquiz

import android.os.CountDownTimer
import eu.vmpay.owlquiz.activities.timer.TimerViewModel
import eu.vmpay.owlquiz.tools.SoundPlayer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

class TimerViewModelTest {
    private val timer = Mockito.mock(CountDownTimer::class.java)
    private val soundPlayer = Mockito.mock(SoundPlayer::class.java)
    private val viewModel = TimerViewModel(soundPlayer)

    @Before
    fun setUp() {
        // Replaces mocked timer inside the presenter
        viewModel.timer = timer
    }

    @Test
    fun pauseTimerTest() {
        // Replace objects with mocks
        viewModel.isStarted.set(true)
        viewModel.timer = timer
        // Trigger action
        viewModel.startTimer()

        // Verify asserts
        verify(timer).cancel()
    }

    @Test
    fun setTimerTest() {
        var timerLength = 20_000
        // Trigger action
        viewModel.setTimerLength(timerLength)
        // Verify asserts
        assertEquals(0, viewModel.currentProgress.get())
        assertEquals(timerLength, viewModel.maxProgress.get())
        assertEquals(timerLength, viewModel.millisUntilFinish.get())

        timerLength = 30_000
        // Trigger action
        viewModel.setTimerLength(timerLength)
        // Verify asserts
        assertEquals(0, viewModel.currentProgress.get())
        assertEquals(timerLength, viewModel.maxProgress.get())
        assertEquals(timerLength, viewModel.millisUntilFinish.get())

        timerLength = 60_000
        // Trigger action
        viewModel.setTimerLength(timerLength)
        // Verify asserts
        assertEquals(0, viewModel.currentProgress.get())
        assertEquals(timerLength, viewModel.maxProgress.get())
        assertEquals(timerLength, viewModel.millisUntilFinish.get())
    }

    @Test
    fun resetTimerTest() {
        // Replace objects with mocks
        viewModel.timer = timer
        // Trigger action
        viewModel.resetTimer()

        // Verify asserts
        assertEquals(viewModel.maxProgress.get(), viewModel.millisUntilFinish.get())
        assertEquals(0, viewModel.currentProgress.get())
        assertFalse(viewModel.isStarted.get())
        assertTrue(viewModel.areRadioButtonsEnabled.get())
        verify(timer).cancel()
    }

    @Test
    fun ifTenSecondsLeftTest() {
        // Verify asserts
        assertFalse(viewModel.ifTenSecondsLeft(10_000))
        assertTrue(viewModel.ifTenSecondsLeft(9_000))
    }

}