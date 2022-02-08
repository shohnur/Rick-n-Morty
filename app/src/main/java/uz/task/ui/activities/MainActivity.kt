package uz.task.ui.activities

import android.view.KeyEvent
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import uz.task.R
import uz.task.base.AppViewModel
import uz.task.base.BaseActivity
import uz.task.base.initialFragment
import uz.task.ui.fragments.MainScreen
import uz.task.utils.ConnectionLiveData
import uz.task.utils.showGone

class MainActivity : BaseActivity(R.layout.activity_main) {

    val viewModel by viewModel<AppViewModel>()

    companion object {
        val parentLayoutId = R.id.fragmentContainer
    }

    override fun onActivityCreated() {
        init()
    }

    private fun init() {
        viewModel.fetchData()

        observeNetwork()

        startFragment()
    }

    private fun observeNetwork() {
        ConnectionLiveData(this).observe(this, Observer {
            val hasNetwork = it?.isConnected ?: false
            if (hasNetwork) viewModel.fetchData()
        })
    }


    private fun startFragment() {
        initialFragment(
            MainScreen(), true
        )
    }

    fun showProgress(show: Boolean) {
        progressBar.showGone(show)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> return false
                KeyEvent.KEYCODE_VOLUME_DOWN -> return false
            }
        }
        return super.dispatchKeyEvent(event)
    }

}
