package noty_team.com.afine.utils.audioPlayer

import android.media.MediaPlayer
import android.widget.SeekBar
import android.widget.TextView

interface AudioPlayer {
    fun play(resource: MediaPlayer, current: TextView, max: TextView, seekBar: SeekBar,i:Int)

    fun pause()

    fun nextTune(resource: Int)

    fun release()

    fun getMediaPlayer(): MediaPlayer

    fun isPlaying(): Boolean
}