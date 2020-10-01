package com.notyteam.iawaketechnologies.ui.fragments.detail

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.notyteam.iawaketechnologies.R
import com.notyteam.iawaketechnologies.utils.loadImage
import kotlinx.android.synthetic.main.description_dialog.*
import kotlinx.android.synthetic.main.description_dialog.cover
import kotlinx.android.synthetic.main.description_dialog.loading_spinner
import kotlinx.android.synthetic.main.description_dialog.title
import noty_team.com.masterovik.ui.adapters.items_adapter.ProgramsItemLocal

internal class DescriptionDialogFragment : DialogFragment() {

    companion object {

        const val ITEM = "item"
        fun newInstance(item: ProgramsItemLocal?): DescriptionDialogFragment {
            return DescriptionDialogFragment().apply {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ThemeOverlay_MaterialComponents_MaterialCalendar_Fullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.description_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initClicks()
    }

    private fun initClicks() {
        fr_close.setOnClickListener {
            dismiss()
        }
    }
    private fun initViews() {

        title.text = getProgramsData(arguments).title
        description.text = Html.fromHtml(getProgramsData(arguments).descriptionHTML)
        cover.loadImage(getProgramsData(arguments).cover?.url!!, loading_spinner)

    }
}