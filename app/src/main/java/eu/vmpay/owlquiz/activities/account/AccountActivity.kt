package eu.vmpay.owlquiz.activities.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import eu.vmpay.owlquiz.R
import kotlinx.android.synthetic.main.activity_account.*

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
