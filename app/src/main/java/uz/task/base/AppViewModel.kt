package uz.task.base

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import retrofit2.HttpException
import uz.task.R
import uz.task.loge
import uz.task.network.ApiInterface
import uz.task.network.RetrofitClient
import uz.task.toast
import uz.task.utils.Errors
import uz.task.utils.baseUrl

open class AppViewModel(
    private val gson: Gson, private val context: Context
) : ViewModel(), KoinComponent {

    val data: MutableLiveData<Any> by inject()
    val error: MutableLiveData<Any> by inject(named("errorLive"))

    private val compositeDisposable = CompositeDisposable()

    private lateinit var api: ApiInterface
    fun refreshApi() {
        api = RetrofitClient.getRetrofit(
            baseUrl,
            context,
            gson
        ).create(ApiInterface::class.java)
    }

    fun fetchData() {
        refreshApi()
    }


    fun getCharacters(page: Int) = compositeDisposable.add(
        api.getAllCharacters(page).observeAndSubscribe().subscribe(
            {
                data.value = it
            }, { parseError(it) }
        )
    )


    private fun parseError(e: Throwable?) {
        var message = context.resources.getString(R.string.smth_wrong)
        if (e != null && e.localizedMessage != null) {
            e.localizedMessage?.let { loge(it) }
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                errorBody?.let {
                    try {
                        loge(it)
                        val errors = it.split(":")
                            .filter { it.contains("[") }
                        val errorsString = if (errors.isNotEmpty()) {
                            errors.toString()
                                .replace("[", "")
                                .replace(",", "\n")
                                .replace("]", "")
                                .replace("{", "")
                                .replace("}", "")
                                .replace("\"", "")
                        } else {
                            val resp = it.split(":")
                            if (resp.size >= 2) resp[1].replace("{", "")
                                .replace("}", "")
                                .replace("\"", "")
                            else it
                        }

                        message = if (errorsString.isEmpty())
                            context.resources.getString(R.string.smth_wrong)
                        else errorsString

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else message = Errors.traceErrors(e, context)
        }
        toast(context, message)
        error.value = message
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

fun <T> Single<T>.observeAndSubscribe() =
    subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())