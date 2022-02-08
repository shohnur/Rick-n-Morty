package uz.task.ui.fragments

import android.annotation.SuppressLint
import kotlinx.android.synthetic.main.screen_web_view.*
import uz.task.R
import uz.task.base.BaseFragment

class WebViewScreen : BaseFragment(R.layout.screen_web_view) {

    var uri = "https://rickandmortyapi.com/"

    @SuppressLint("SetJavaScriptEnabled")
    override fun initialize() {
        webView.apply {
            settings.javaScriptEnabled = true
            loadUrl(uri)
        }
    }
}