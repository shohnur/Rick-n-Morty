package uz.task.utils

import android.content.Context
import uz.task.R
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object Errors {
    fun traceErrors(t: Throwable, context: Context): String {
        return when (t) {
            is UnknownHostException -> context.getString(R.string.check_connection)
            is ConnectException -> context.getString(R.string.check_connection)
            is SocketTimeoutException -> context.getString(R.string.slow_internet)
            else -> context.getString(R.string.smth_wrong)
        }
    }
}