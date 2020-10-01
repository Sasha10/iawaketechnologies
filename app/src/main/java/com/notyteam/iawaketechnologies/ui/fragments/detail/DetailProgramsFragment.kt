package com.notyteam.iawaketechnologies.ui.fragments.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.notyteam.iawaketechnologies.R
import com.notyteam.iawaketechnologies.api.responses.programs.TracksItem
import com.notyteam.iawaketechnologies.base.BaseFragment
import com.notyteam.iawaketechnologies.base.mvvm.BaseViewModel
import com.notyteam.iawaketechnologies.ui.adapter.programs.TracksAdapter
import com.notyteam.iawaketechnologies.ui.fragments.audio_player.AudioPlayerFragment
import com.notyteam.iawaketechnologies.ui.viewmodels.MainViewModel
import com.notyteam.iawaketechnologies.utils.injectViewModel
import com.notyteam.iawaketechnologies.utils.loadImage
import kotlinx.android.synthetic.main.fragment_detail_programs.*
import noty_team.com.masterovik.ui.adapters.items_adapter.ProgramsItemLocal

class DetailProgramsFragment : BaseFragment<BaseViewModel>() {



    private lateinit var tracksAdapter: TracksAdapter


    companion object {

        const val ITEM = "item"
        fun newInstance(item: ProgramsItemLocal?): DetailProgramsFragment {
            return DetailProgramsFragment().apply {
                val data = Gson().toJson(item)
                arguments = Bundle().apply {
                    putString(ITEM, data)
                }
            }
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

    override fun layout() = R.layout.fragment_detail_programs

    override fun provideViewModel(viewModelFactory: ViewModelProvider.Factory): MainViewModel {
        return injectViewModel(viewModelFactory)
    }

    override fun initialization(view: View, isFirstInit: Boolean) {

        if (isFirstInit){
            initViews()
            initClicks()
            initTracksAdapter(getProgramsData(arguments), getProgramsData(arguments).tracks!!)
        }
    }
    private fun initTracksAdapter(item: ProgramsItemLocal, tracks: ArrayList<TracksItem?>){

        recycler_tracks.layoutManager = LinearLayoutManager(baseActivity)
        tracksAdapter = TracksAdapter(baseContext, item, tracks) {
            fragmentManager.addFragment(AudioPlayerFragment.newInstance(getProgramsData(arguments),it))
        }
        recycler_tracks.adapter = tracksAdapter

    }
    private fun initClicks() {
       close.getClick {
           onBackPressed()
       }

        btn_details.getClick {
            val dialog = DescriptionDialogFragment.newInstance(getProgramsData(arguments))
            dialog.show(baseActivity.getSupportFragmentManager(), "TAG")
        }
    }
    private fun initViews() {

        if(getProgramsData(arguments).descriptionHTML == ""){
            btn_details.visibility = View.GONE
        }
        title.text = getProgramsData(arguments).title
        size_programs.text = baseContext.resources.getQuantityString(
            R.plurals.track_plurals, getProgramsData(
                arguments
            ).tracks?.size!!, getProgramsData(arguments).tracks?.size!!
        )
        cover.loadImage(getProgramsData(arguments).cover?.url!!, loading_spinner)

    }
}