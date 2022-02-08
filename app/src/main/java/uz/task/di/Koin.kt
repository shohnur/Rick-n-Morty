package uz.task.di

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import uz.task.base.AppViewModel

val networkModule = module {
    fun provideGson() = Gson()
    single { provideGson() }
}

val viewModelModule = module {
    fun provideMutableLiveData() = MutableLiveData<Any>()

    viewModel { AppViewModel(get(), get()) }

    single(named("errorLive")) { MutableLiveData<Any>() }

    single { provideMutableLiveData() }
}