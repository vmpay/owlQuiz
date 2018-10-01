package eu.vmpay.owlquiz

import android.app.Application
import android.util.Log

/**
 * Created by Andrew on 30/03/2018.
 */
class QuizApp : Application() {

    private val TAG = "QuizApp"

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "onCreate")

//        AppController.getInstance().setUp(applicationContext)

    }
}