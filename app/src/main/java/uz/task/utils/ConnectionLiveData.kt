package uz.task.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData


class ConnectionLiveData(private val context: Context) : LiveData<Connection>() {

    override fun onActive() {
        super.onActive()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkReceiver, filter)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(networkReceiver)
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.extras != null) {
                val connectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork = connectivityManager.activeNetworkInfo

                if (activeNetwork == null) {
                    postValue(Connection(0, false))
                    return
                }

                val isConnected = activeNetwork.isConnected

                if (isConnected) {
                    postValue(Connection(0, true))
                } else {
                    postValue(Connection(0, false))
                }
            }
        }
    }
}