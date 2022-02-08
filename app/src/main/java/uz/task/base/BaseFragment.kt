package uz.task.base

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import uz.task.R
import uz.task.ui.activities.MainActivity


abstract class BaseFragment(@LayoutRes val layoutId: Int) : Fragment(layoutId) {

    protected lateinit var mainActivity: MainActivity
    protected lateinit var viewModel: AppViewModel
    protected var enableCustomBackPress = false

    override fun onAttach(context: Context) {
        mainActivity = requireActivity() as MainActivity
        viewModel = mainActivity.viewModel
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialize()
        setFocus(view)
        observe()
        observeError()
    }

    fun addFragment(
        fragment: Fragment,
        addBackStack: Boolean = true, @IdRes id: Int = parentLayoutId(),
        tag: String = fragment.hashCode().toString(),
        setTopBottomAnim: Boolean = true
    ) {
        hideKeyboard()
        activity?.supportFragmentManager?.commit(allowStateLoss = true) {
            if (!fragment.isAdded) {
                setReorderingAllowed(true)
                if (addBackStack && !fragment.isAdded) addToBackStack(tag)
                if (setTopBottomAnim) setTopBottomAnimation()
                else setLeftRightAnimation()
                add(id, fragment)
            }
        }
    }

    private fun FragmentTransaction.setLeftRightAnimation() {
        setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
    }

    private fun FragmentTransaction.setTopBottomAnimation() {
        setCustomAnimations(
            R.anim.enter_from_bottom,
            R.anim.exit_to_top,
            R.anim.enter_from_top,
            R.anim.exit_to_bottom
        )
    }

    fun finishFragment() {
        hideKeyboard()
        activity?.supportFragmentManager?.popBackStackImmediate()
    }

    fun popInclusive(name: String? = null, flags: Int = FragmentManager.POP_BACK_STACK_INCLUSIVE) {
        hideKeyboard()
        activity?.supportFragmentManager?.popBackStackImmediate(name, flags)
    }

    protected fun showProgress(show: Boolean) {
        mainActivity.showProgress(show)
    }

    protected fun hideKeyboard() {
        view?.let {
            val imm =
                it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun observeError() {
        viewModel.error.observe(viewLifecycleOwner, Observer {
            showProgress(false)
        })
    }

    protected open fun onFragmentBackButtonPressed() {
    }

    protected open fun observe() {
    }

    private fun setFocus(view: View) {
        view.apply {
            isFocusableInTouchMode = true
            requestFocus()
            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (enableCustomBackPress) onFragmentBackButtonPressed()
                    else activity?.onBackPressed()
                }
                enableCustomBackPress = false
                true
            }
        }
    }

    abstract fun initialize()

}

fun FragmentActivity.initialFragment(fragment: BaseFragment, showAnim: Boolean = false) {
    supportFragmentManager.commit(allowStateLoss = true) {
        if (showAnim)
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
        replace(MainActivity.parentLayoutId, fragment)
    }
}


fun FragmentActivity.finishFragment() {
    supportFragmentManager.popBackStack()
}

fun BaseFragment.parentLayoutId() = MainActivity.parentLayoutId
