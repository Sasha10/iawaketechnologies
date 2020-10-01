package com.notyteam.iawaketechnologies.utils.ciceron

import com.notyteam.iawaketechnologies.base.BaseFragment
import com.notyteam.iawaketechnologies.base.mvvm.BaseViewModel

interface CiceroneFragmentManager {
    fun <P : BaseViewModel> replaceFragment(fragment: BaseFragment<P>)

    fun <P : BaseViewModel> addFragment(fragment: BaseFragment<P>)

    fun <P : BaseViewModel> newRootFragment(fragment: BaseFragment<P>)

    fun <P : BaseViewModel> backTo(fragment: BaseFragment<P>)

    fun exit()
}