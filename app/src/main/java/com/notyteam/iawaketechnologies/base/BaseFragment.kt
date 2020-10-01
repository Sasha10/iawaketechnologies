package com.notyteam.iawaketechnologies.base

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.notyteam.iawaketechnologies.R
import com.notyteam.iawaketechnologies.base.mvvm.BaseViewModel
import com.notyteam.iawaketechnologies.utils.ciceron.CiceroneFragmentManager
import com.notyteam.iawaketechnologies.utils.ciceron.OnBackClickListener
import io.reactivex.disposables.CompositeDisposable
import noty_team.com.afine.utils.audioPlayer.AudioPlayerImpl
import java.util.concurrent.TimeUnit
import javax.inject.Inject

abstract class BaseFragment<V : BaseViewModel> : Fragment(), OnBackClickListener, SnackBarListener {
    protected lateinit var viewModel: V

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    protected lateinit var baseContext: Context

    @Inject
    lateinit var baseActivity: BaseActivity<*>

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var fragmentManager: CiceroneFragmentManager

    @Inject
    lateinit var toast: Toast

    protected var rootView: View? = null
    var isVisible: (fragment: Fragment) -> Boolean = { true }


    @LayoutRes
    protected abstract fun layout(): Int
    protected abstract fun provideViewModel(viewModelFactory: ViewModelProvider.Factory): V
    protected abstract fun initialization(view: View, isFirstInit: Boolean)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            (context as BaseActivity<*>).fragmentComponent.inject(this as BaseFragment<BaseViewModel>)
        } catch (e: UninitializedPropertyAccessException) {
            (context as BaseActivity<*>).initFragmentComponent()
            context.fragmentComponent.inject(this as BaseFragment<BaseViewModel>)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = if (layout() != 0)
            addProgressView(inflater.inflate(layout(), container, false))
        else
            super.onCreateView(inflater, container, savedInstanceState)
        return if (rootView == null) view else rootView
    }

    private fun addProgressView(rootView: View): View {
        val rootLayout = FrameLayout(baseContext)
        val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER)
        rootLayout.layoutParams = layoutParams
        rootLayout.addView(rootView)
        return rootLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialization(view, rootView == null)
        rootView = view
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        viewModel.onClear()
        baseActivity.toggleKeyboard(false)
        super.onDestroyView()
    }

    override fun onDestroy() {
        baseActivity.toggleKeyboard(false)
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onBackPressed(): Boolean {
        baseActivity.toggleKeyboard(false)
        fragmentManager.exit()
        return true
    }

    fun View.getClick(durationMillis: Long = 500, onClick:(view: View)->Unit){
        RxView.clicks(this)
            .throttleFirst(durationMillis, TimeUnit.MILLISECONDS)
            .subscribe ({ _ ->
                onClick(this)
            }, {

            })
            .also { compositeDisposable.add(it) }
    }

    override fun showErrorSnack(message: String) {
        val snackbar = view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG)
        }
        context?.let {
            ContextCompat.getColor(it, R.color.colorRed)
        }?.let {
            snackbar?.view?.setBackgroundColor(it)
        }
        snackbar?.addCallback(object: Snackbar.Callback(){
            override fun onShown(sb: Snackbar?) {

            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {

            }
        })

        snackbar?.show()
    }

    fun isShowLoadingDialog(isShow: Boolean) = baseActivity.isShowLoadingDialog(isShow)

    override fun showSuccessSnack(message: String) {
        val snackbar = view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG)
        }
        context?.let {
            ContextCompat.getColor(it, R.color.colorGreen)
        }?.let {
            snackbar?.view?.setBackgroundColor(it)
        }
        snackbar?.show()
    }
}