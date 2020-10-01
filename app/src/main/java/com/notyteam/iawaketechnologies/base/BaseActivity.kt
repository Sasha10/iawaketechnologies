package com.notyteam.iawaketechnologies.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding2.view.RxView
import com.notyteam.iawaketechnologies.App
import com.notyteam.iawaketechnologies.R
import com.notyteam.iawaketechnologies.base.mvvm.BaseViewModel
import com.notyteam.iawaketechnologies.dagger.components.FragmentComponent
import com.notyteam.iawaketechnologies.dagger.modules.FragmentModule
import com.notyteam.iawaketechnologies.utils.ciceron.CiceroneFragmentManager
import com.notyteam.iawaketechnologies.utils.ciceron.OnBackClickListener
import io.reactivex.disposables.CompositeDisposable
import noty_team.com.afine.utils.audioPlayer.AudioPlayerImpl
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import java.util.concurrent.TimeUnit
import javax.inject.Inject

abstract class BaseActivity<V : BaseViewModel> : AppCompatActivity() {
    lateinit var viewModel: V

    lateinit var fragmentComponent: FragmentComponent

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var cicerone: Cicerone<Router>

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var ciceroneNavigator: SupportAppNavigator

    @Inject
    lateinit var fragmentManager: CiceroneFragmentManager

    private lateinit var loadingDialog: Dialog

    @Inject
    lateinit var audioPlayerImpl: AudioPlayerImpl

    @LayoutRes
    abstract fun layout(): Int

    abstract fun initialization()
    abstract fun provideViewModel(viewModelFactory: ViewModelProvider.Factory): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFragmentComponent()

        intiLoadingDialog()

        viewModel = provideViewModel(viewModelFactory)

        if (layout() != 0) {
            setContentView(layout())
            initialization()
        }
    }

    private fun intiLoadingDialog() {
        loadingDialog = Dialog(this)

        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDialog.setContentView(R.layout.base_loading_dialog)


        loadingDialog.setOnCancelListener {
            onBackPressed()
            isShowLoadingDialog(false)
        }
        loadingDialog.setCanceledOnTouchOutside(false)
    }


    fun initFragmentComponent() {
        if (!::fragmentComponent.isInitialized) {
            fragmentComponent = App[this].appComponent.plus(
                // TODO: 30.08.2020
                FragmentModule(R.id.fragment_container, this as BaseActivity<BaseViewModel>)
            )
            fragmentComponent.inject(this)
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(ciceroneNavigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    fun showProgressBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun hideProgressBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        var fragment: Fragment? = null
        val fragments = fm.fragments
        for (f in fragments) {
            if (f.isVisible) {
                fragment = f
                break
            }
        }

        if (fragment != null
            && fragment is OnBackClickListener
            && (fragment as OnBackClickListener).onBackPressed()) {
        } else {
            fragmentManager.exit()
        }
    }

    fun toggleKeyboard(show: Boolean) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!show)
            inputMethodManager.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        else
            inputMethodManager.toggleSoftInputFromWindow(window.decorView.windowToken,
                InputMethodManager.SHOW_FORCED, 0)
    }

    fun freeMemory() {
        System.runFinalization()
        Runtime.getRuntime().gc()
        System.gc()
    }

    fun View.getClick(durationMillis: Long = 500, onClick: (view: View) -> Unit) {
        RxView.clicks(this)
            .throttleFirst(durationMillis, TimeUnit.MILLISECONDS)
            .subscribe({ _ ->
                onClick(this)
            }, {

            })
            .also { compositeDisposable.add(it) }
    }

    @SuppressLint("Range")
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val v = currentFocus
        if (v != null &&
            (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) &&
            v is EditText &&
            !v.javaClass.name.startsWith("android.webkit.")) {
            val scrcoords = IntArray(2) { i -> 2 }
            v.getLocationOnScreen(scrcoords)
            val x = ev.rawX + v.getLeft() - scrcoords[0]
            val y = ev.rawY + v.getTop() - scrcoords[1]
            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) hideKeyboard(this)
        }
        return super.dispatchTouchEvent(ev)
    }

    open fun hideKeyboard(activity: Activity?) {
        if (activity != null && activity.window != null && activity.window.decorView != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
        }
    }

    fun isShowLoadingDialog(isShow: Boolean) {
        if (isShow) {
            loadingDialog.show()
        } else {
            loadingDialog.dismiss()
        }
        val window = loadingDialog.getWindow()
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        window?.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        loadingDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
    }

}