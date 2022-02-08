package uz.task.network

import android.content.Context
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import uz.task.BuildConfig
import uz.task.utils.UnsafeOkHttpClient

object RetrofitClient {

    fun getRetrofit(
        baseUrl: String,
        context: Context,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getClient(context))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun getClient(context: Context): OkHttpClient {
        val builder = UnsafeOkHttpClient.unsafeOkHttpClientBuilder
        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag("TTT").i(message)
            }
        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        builder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
            chain.proceed(request.build())
        })
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor)
            builder.addInterceptor(ChuckInterceptor(context))
        }
        return builder.build()
    }

}