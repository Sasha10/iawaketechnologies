package com.notyteam.iawaketechnologies.ui.adapter.programs

import android.content.Context
import android.view.View
import com.notyteam.iawaketechnologies.R
import com.notyteam.iawaketechnologies.ui.adapter.BaseAdapter
import com.notyteam.iawaketechnologies.utils.loadImage
import kotlinx.android.synthetic.main.item_programs.*
import noty_team.com.masterovik.ui.adapters.items_adapter.ProgramsItemLocal
import java.lang.Exception

class ProgramsAdapter(val context: Context, list: ArrayList<ProgramsItemLocal>,
                      val onClick: (item: ProgramsItemLocal) -> Unit) :
        BaseAdapter<ProgramsItemLocal, ProgramsAdapter.ViewHolder>(list) {

    override fun createViewHolder(view: View, layoutId: Int) = ViewHolder(view)

    override fun getItemViewType(position: Int) = R.layout.item_programs

    inner class ViewHolder(view: View) : BaseAdapter.BaseViewHolder(view) {

        init {
            try {
                itemView.getClick {
                    onClick(list[adapterPosition])
                }
            } catch (e: Exception) {
            }
        }

        override fun bind(pos: Int) {

            title.text = list[pos].title
            cover.loadImage(list[pos].cover?.url!!,loading_spinner)

        }
    }
}
