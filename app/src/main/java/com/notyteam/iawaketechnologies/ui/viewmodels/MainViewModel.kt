package com.notyteam.iawaketechnologies.ui.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.notyteam.iawaketechnologies.api.responses.programs.ListProgramsResponse
import com.notyteam.iawaketechnologies.base.mvvm.BaseViewModel
import javax.inject.Inject
import com.notyteam.iawaketechnologies.utils.Result

class MainViewModel @Inject constructor(app: Application) : BaseViewModel(app) {

    fun getPrograms(): LiveData<Result<ListProgramsResponse>> {
        val livaData = MutableLiveData<Result<ListProgramsResponse>>()

        api.programsApi.getPrograms {
            livaData.postValue(it)
        }.call()


        return livaData
    }

    override fun onClear() {}

}
