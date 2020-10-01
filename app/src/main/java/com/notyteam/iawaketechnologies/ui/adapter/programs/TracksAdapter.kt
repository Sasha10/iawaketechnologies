package com.notyteam.iawaketechnologies.ui.adapter.programs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.notyteam.iawaketechnologies.R
import com.notyteam.iawaketechnologies.api.responses.programs.TracksItem
import com.notyteam.iawaketechnologies.utils.splitToComponentTimes
import kotlinx.android.synthetic.main.item_program_tracks.view.*
import noty_team.com.masterovik.ui.adapters.items_adapter.ProgramsItemLocal

class TracksAdapter(val context: Context, private val item:  ProgramsItemLocal, private val data: ArrayList<TracksItem?>,
                    val onClick: (item: TracksItem) -> Unit) :
    RecyclerView.Adapter<TracksAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_program_tracks, parent, false)

        return ViewHolder(view).apply {
            view.setOnClickListener {
                data[adapterPosition]?.let {
                    onClick(it)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        data[p1].let {
            holder.tvNumberTrarck.text = (p1+1).toString()
            holder.tvTitleTrack.text = "${item.title} / ${it?.title}"
            holder.tvDurationTrack.text = splitToComponentTimes(it?.duration!!)
           }
        }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val tvNumberTrarck: TextView = view.number_track
        val tvTitleTrack: TextView = view.title_track
        val tvDurationTrack: TextView = view.duration_track


    }
}



