package eu.vmpay.owlquiz.timer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.vmpay.owlquiz.R
import eu.vmpay.owlquiz.R.string.*
import kotlinx.android.synthetic.main.fragment_timer.*
import kotlinx.android.synthetic.main.fragment_timer.view.*

/**
 * Created by Andrew on 22/03/2018.
 */
class TimerFragment : Fragment(), TimerContract.View, View.OnClickListener {

    private val TAG = "TimerFragment"

    override lateinit var presenter: TimerContract.Presenter

    companion object {
        fun newInstance() = TimerFragment()
    }

    override var isActive: Boolean = false
        get() = isAdded

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_timer, container, false)
        Log.d(TAG, "onCreateView")

        view.rb20.setOnClickListener(this)
        view.rb30.setOnClickListener(this)
        view.rb60.setOnClickListener(this)
        view.btnStart.setOnClickListener(this)
        view.btnReset.setOnClickListener(this)

        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onPause() {
        btnStart.text = if (presenter.isTimerStarted) getString(btn_start) else getString(btn_pause)
        if (presenter.isTimerStarted) presenter.startTimer()
        super.onPause()
    }

    override fun onClick(view: View) {
        when (view) {
            rb60 -> {
                tvTimer.text = getString(timer_60)
                presenter.setTimer(60000)
                progressBar.progress = 0
                progressBar.max = 60000
            }
            rb30 -> {
                tvTimer.text = getString(timer_30)
                presenter.setTimer(30000)
                progressBar.progress = 0
                progressBar.max = 30000
            }
            rb20 -> {
                tvTimer.text = getString(timer_20)
                presenter.setTimer(20000)
                progressBar.progress = 0
                progressBar.max = 20000
            }
            btnStart -> {
                btnStart.text = if (presenter.isTimerStarted) getString(btn_start) else getString(btn_pause)
                presenter.startTimer()
                setEnableRadioButtons(false)
            }
            btnReset -> {
                presenter.resetTimer()
                progressBar.progress = 0
                tvTimer.text = "${progressBar.max / 1000} ${getString(units_seconds)}"
                btnStart.text = getString(btn_start)
                setEnableRadioButtons(true)
            }
        }
    }

    override fun showProgress(secondsUntilFinished: Int, currentProgress: Int) {
        progressBar.progress = currentProgress
        tvTimer.text = "${(secondsUntilFinished / 100) / 10F} ${getString(units_seconds)}"
    }

    override fun showTimerFinished() {
        progressBar.progress = progressBar.max
        tvTimer.text = "0 ${getString(units_seconds)}"
        btnStart.text = getString(btn_start)
        setEnableRadioButtons(true)
    }

    private fun setEnableRadioButtons(enable: Boolean) {
        rb60.isEnabled = enable
        rb30.isEnabled = enable
        rb20.isEnabled = enable
    }
}