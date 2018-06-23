package eu.vmpay.owlquiz.soundpool

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.preference.Preference
import android.preference.PreferenceManager
import android.util.Log
import eu.vmpay.owlquiz.R


/**
 * Created by Andrew on 02/04/2018.
 */
class SoundPlayer(private val applicationContext: Context) : SoundPool.OnLoadCompleteListener, SoundPlayerContract {

    private val TAG = "SoundPlayer"
    private val MAX_NUMBER_STREAMS = 1
    private val SOURCE_QUALITY = 0

    private val LEFT_VOLUME = 0.5F  // left volume value (range = 0.0 to 1.0)
    private val RIGHT_VOLUME = 0.5F // right volume value (range = 0.0 to 1.0)
    private val PRIORITY = 0        // stream priority (0 = lowest priority)
    private val LOOP = 0            // loop mode (0 = no loop, -1 = loop forever)
    private val RATE = 1F           // playback rate (1.0 = normal playback, range 0.5 to 2.0)

    private val soundPoolPlayer: SoundPool
    private var streamId: Int = -1
    private var syncFinishedSoundId: Int = -1

    private var isPlaying = false
    private var isSoundOn: Boolean

    init {
        Log.d(TAG, "init MAX_NUMBER_STREAMS $MAX_NUMBER_STREAMS STREAM_NOTIFICATION ${AudioManager.STREAM_NOTIFICATION} SOURCE_QUALITY $SOURCE_QUALITY")
        soundPoolPlayer = SoundPool(MAX_NUMBER_STREAMS, AudioManager.STREAM_NOTIFICATION, SOURCE_QUALITY)
        soundPoolPlayer.setOnLoadCompleteListener(this)
        isSoundOn = PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean(
                applicationContext.getString(R.string.timer_sound_notification_key), false)
        Log.d(TAG, "constructor isSoundOn = $isSoundOn")
    }

    override fun playSound(resId: Int) {
        if (isSoundOn) {
            Log.d(TAG, "loading sound ${applicationContext.resources.getResourceEntryName(resId)}")
            syncFinishedSoundId = soundPoolPlayer.load(applicationContext, resId, 1)
        } else {
            Log.d(TAG, "sound notification is disabled")
        }
    }

    override fun playSound() {
        playSound(R.raw.beep_call)
    }

    override fun stopSound() {
        if (isPlaying && streamId != -1) {
            Log.d(TAG, "onLoadComplete failed streamId $streamId}")
            soundPoolPlayer.stop(streamId)
            isPlaying = false
        }
    }

    override fun onLoadComplete(soundPool: SoundPool?, sampleId: Int, status: Int) {
        if (status == 0) {
            if (sampleId == syncFinishedSoundId) {
                Log.d(TAG, "playing sound sampleId $sampleId}")
                streamId = soundPoolPlayer.play(sampleId, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, LOOP, RATE)
                isPlaying = true
            }
        } else {
            Log.d(TAG, "onLoadComplete failed sampleId $sampleId}")
        }
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        if (preference?.key.equals(applicationContext.getString(R.string.timer_sound_notification_key)))
            isSoundOn = newValue as Boolean
        Log.d(TAG, "onPreferenceChange ${preference?.title} = $newValue")
        return true
    }
}