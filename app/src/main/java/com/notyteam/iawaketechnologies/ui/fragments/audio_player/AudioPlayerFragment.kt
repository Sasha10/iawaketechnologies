package com.notyteam.iawaketechnologies.ui.fragments.audio_player

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.notyteam.iawaketechnologies.R
import com.notyteam.iawaketechnologies.api.responses.programs.TracksItem
import com.notyteam.iawaketechnologies.base.BaseFragment
import com.notyteam.iawaketechnologies.base.mvvm.BaseViewModel
import com.notyteam.iawaketechnologies.ui.viewmodels.MainViewModel
import com.notyteam.iawaketechnologies.utils.injectViewModel
import com.notyteam.iawaketechnologies.utils.loadImage
import kotlinx.android.synthetic.main.fragment_audio_player.*
import noty_team.com.masterovik.ui.adapters.items_adapter.ProgramsItemLocal
import java.io.IOException


class AudioPlayerFragment : BaseFragment<BaseViewModel>() {

    var mp: MediaPlayer? = null
    var isStart:Boolean = true
    companion object {

        const val ITEM = "item"
        const val TRACK_ITEM = "tracksItem"
        fun newInstance(item: ProgramsItemLocal?, tracksItem: TracksItem): AudioPlayerFragment {
            return AudioPlayerFragment().apply {
                val data = Gson().toJson(item)
                val dataItem = Gson().toJson(tracksItem)
                arguments = Bundle().apply {
                    putString(ITEM, data)
                    putString(TRACK_ITEM, dataItem)
                }
            }
        }

        fun getTrackData(args: Bundle?): TracksItem {
            val dataString = args?.getString(TRACK_ITEM) ?: ""
            if (dataString.isNotEmpty()) {
                Gson().fromJson(dataString, TracksItem::class.java)?.let {
                    return it
                }
            }

            return TracksItem()
        }



        fun getProgramsData(args: Bundle?): ProgramsItemLocal {
            val dataString = args?.getString(ITEM) ?: ""
            if (dataString.isNotEmpty()) {
                Gson().fromJson(dataString, ProgramsItemLocal::class.java)?.let {
                    return it
                }
            }

            return ProgramsItemLocal()
        }


    }

    override fun layout() = R.layout.fragment_audio_player

    override fun provideViewModel(viewModelFactory: ViewModelProvider.Factory): MainViewModel {
        return injectViewModel(viewModelFactory)
    }

    override fun initialization(view: View, isFirstInit: Boolean) {

        if (isFirstInit){
            initViews()
            initClicks()
            createPlayer()
        }
    }

    private fun createPlayer() {

        val url = getTrackData(arguments).media?.mp3!!.url
        mp = MediaPlayer()
        try {
            mp!!.setDataSource(url)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mp!!.setOnCompletionListener {
            btnPlay.background = ContextCompat.getDrawable(baseContext, R.drawable.ic_baseline_play_arrow_24)
            mp!!.seekTo(0)
        }
    }



    private fun initClicks() {
        fr_close.getClick {
           onBackPressed()
       }
        btnPlay.getClick {
            if (baseActivity.audioPlayerImpl.isPlaying()) {
                btnPlay.background = ContextCompat.getDrawable(baseContext, R.drawable.ic_baseline_play_arrow_24)
                baseActivity.audioPlayerImpl.pause()
            }else{
                btnPlay.background = ContextCompat.getDrawable(baseContext, R.drawable.ic_baseline_pause_24)
                if (isStart){
                    baseActivity.audioPlayerImpl.play(mp!!, tvCurrentTime, tvMaxTime, timSeekbar, 1)
                    isStart = false
                }else{
                     mp!!.seekTo(mp!!.currentPosition)
                     mp!!.start()
                }
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        baseActivity.audioPlayerImpl.release()
    }

    private fun initViews() {

        title_programs.text = getProgramsData(arguments).title
        title_track.text = getTrackData(arguments).title
        cover.loadImage(getProgramsData(arguments).cover?.url!!, loading_spinner)

    }

}