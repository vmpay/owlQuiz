package eu.vmpay.owlquiz.timer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.vmpay.owlquiz.R

/**
 * Created by Andrew on 22/03/2018.
 */
class TimerFragment : Fragment() {
    private val TAG = "TimerFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_timer, container, false)
        Log.d(TAG, "onCreateView")
        return view
    }
}