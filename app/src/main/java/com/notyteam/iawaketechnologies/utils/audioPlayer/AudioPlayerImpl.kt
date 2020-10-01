package noty_team.com.afine.utils.audioPlayer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import com.notyteam.iawaketechnologies.utils.milliSecondsToTimer


class AudioPlayerImpl(private val context: Context) : AudioPlayer {

    private var player: MediaPlayer? = null
    private val handler = Handler()
    private var runnable: Runnable? = null
    private var res: Int = 0
    var isReleased: Boolean = false

    fun mediaPlayerBuilder(resourceId: Int): MediaPlayer {
        res = resourceId
        player = MediaPlayer.create(context, resourceId)
        return player!!
    }

    override fun getMediaPlayer(): MediaPlayer {
        return player!!
    }

    override fun play(
        resource: MediaPlayer,
        current: TextView,
        max: TextView,
        seekBar: SeekBar,
        i: Int

    ) {

        player = resource
        player?.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        player?.prepareAsync()


        if (player != null && !player!!.isPlaying) {
            player!!.setOnPreparedListener {
                player!!.start()
                seekBar.max = it.duration
                max.text = milliSecondsToTimer(it.duration.toLong())
            }

            isReleased = false

            changeSeekbar(seekBar, current, resource, i)

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    if (p2 && !isReleased)
                        player!!.seekTo(p1)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            })

            player!!.setOnErrorListener({ mp, what, extra ->
                Log.d("myLogs", "WHAT: $what EXTRA: $extra")
                true
            })

        }
    }

    override fun pause() {
        if (player != null && !isReleased) {
            if (player!!.isPlaying) {
                player!!.pause()
               // handler.removeCallbacks(runnable)
            }
        }
    }

    override fun nextTune(resource: Int) {
        releasePlayer(player)
        player = mediaPlayerBuilder(resource)
        player!!.isLooping = false
        player!!.start()

    }

    override fun release() {
        releasePlayer(player)
        handler.removeCallbacks(runnable)
    }

    override fun isPlaying(): Boolean {
        var isPlaying = false
        if (player != null && !isReleased && player!!.isPlaying) {
            isPlaying = true
        }
        return isPlaying
    }

    private fun releasePlayer(player: MediaPlayer?) {
        player?.release()
        isReleased = true
    }

    fun changeSeekbar(seekBar: SeekBar, tv: TextView, mediaPlayer: MediaPlayer, i: Int) {

        seekBar.progress = (mediaPlayer.currentPosition)
        seekBar.max = mediaPlayer.duration
        tv.text = milliSecondsToTimer(mediaPlayer.currentPosition.toLong())

            runnable = Runnable {
                changeSeekbar(seekBar, tv, mediaPlayer, i)
            }
            handler.postDelayed(runnable, 1000)

    }
}