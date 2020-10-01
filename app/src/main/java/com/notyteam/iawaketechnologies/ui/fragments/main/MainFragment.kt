package com.notyteam.iawaketechnologies.ui.fragments.main

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.notyteam.iawaketechnologies.R
import com.notyteam.iawaketechnologies.base.BaseFragment
import com.notyteam.iawaketechnologies.ui.adapter.programs.ProgramsAdapter
import com.notyteam.iawaketechnologies.ui.fragments.detail.DetailProgramsFragment
import com.notyteam.iawaketechnologies.ui.viewmodels.MainViewModel
import com.notyteam.iawaketechnologies.utils.Failure
import com.notyteam.iawaketechnologies.utils.Success
import com.notyteam.iawaketechnologies.utils.injectViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment<MainViewModel>() {

    private lateinit var programsAdapter: ProgramsAdapter

    override fun layout() = R.layout.fragment_main

    override fun provideViewModel(viewModelFactory: ViewModelProvider.Factory): MainViewModel {
        return injectViewModel(viewModelFactory)
    }

    override fun initialization(view: View, isFirstInit: Boolean) {

        if (isFirstInit){
            getListPrograms()
            initProgramsAdapter()
        }
    }

    private fun getListPrograms() {
        isShowLoadingDialog(true)
        listenUpdates()
    }

    private fun listenUpdates() {

        viewModel.getPrograms().observe(this) { response ->
            isShowLoadingDialog(false)
            when (response) {
                is Success -> {
                    programsAdapter.clear()
                    programsAdapter.addAll(response.value.map())
                }
                is Failure -> {
                    showErrorSnack(response.errorMessage)
                }
            }
        }
    }

    private fun initProgramsAdapter() {
        recycler_programs.layoutManager = GridLayoutManager(baseActivity,2)
        programsAdapter = ProgramsAdapter(baseContext, arrayListOf()) {
            fragmentManager.addFragment(DetailProgramsFragment.newInstance(it))
        }
        recycler_programs.adapter = programsAdapter
    }
}