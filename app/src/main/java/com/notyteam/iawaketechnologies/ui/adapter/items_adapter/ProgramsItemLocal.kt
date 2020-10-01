package noty_team.com.masterovik.ui.adapters.items_adapter

import com.notyteam.iawaketechnologies.api.responses.programs.Cover
import com.notyteam.iawaketechnologies.api.responses.programs.TracksItem


data class ProgramsItemLocal(var id: String = "",
                             var title: String = "",
                             var descriptionHTML: String = "",
                             var cover: Cover? = null,
                             val tracks: ArrayList<TracksItem?>? = null)