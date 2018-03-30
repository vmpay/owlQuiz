package eu.vmpay.owlquiz.pref

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import eu.vmpay.owlquiz.R
import eu.vmpay.owlquiz.utils.replaceFragmentInActivity

class PrefActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pref)

        val prefFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
                as PrefFragment? ?: PrefFragment.newInstance().also {
            replaceFragmentInActivity(it, R.id.contentFrame)
        }

        var timerPresenter = PrefPresenter(prefFragment)
    }
}
