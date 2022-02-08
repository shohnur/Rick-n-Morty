package uz.task.di

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import uz.task.base.AppViewModel
import uz.task.db.DB

val networkModule = module {
    fun provideGson() = Gson()
    single { provideGson() }
}

val viewModelModule = module {
    fun provideMutableLiveData() = MutableLiveData<Any>()

    viewModel { AppViewModel(get(), get(),get()) }

    single(named("errorLive")) { MutableLiveData<Any>() }

    single { provideMutableLiveData() }
}

val dbModule = module {
    fun provideDatabase(application: Application): DB {
        return Room.databaseBuilder(application, DB::class.java, "data")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    fun provideDao(db: DB) = db.dao()

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
}