package com.notyteam.iawaketechnologies.utils.ciceron

import com.notyteam.iawaketechnologies.base.BaseFragment
import com.notyteam.iawaketechnologies.base.mvvm.BaseViewModel
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {
    class FragmentScreen<P : BaseViewModel, T : BaseFragment<P>>(var fragment: T) :
        SupportAppScreen() {
        override fun getFragment(): androidx.fragment.app.Fragment {
            return fragment
        }
    }
}