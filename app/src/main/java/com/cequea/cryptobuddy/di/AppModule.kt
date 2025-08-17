package com.cequea.cryptobuddy.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.cequea.cryptobuddy.data.network.CryptoApi
import com.cequea.cryptobuddy.data.network.CryptoRepositoryImpl
import com.cequea.cryptobuddy.domain.CryptoRepository
import com.cequea.cryptobuddy.domain.SystemTimeProvider
import com.cequea.cryptobuddy.domain.TimeProvider
import com.cequea.cryptobuddy.ui.screens.cryptoDetail.CryptoDetailViewModel
import com.cequea.cryptobuddy.ui.screens.cryptoList.CryptoListViewModel
import com.cequea.cryptobuddy.utils.dataStore
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

val appModule = module {
    single {
        Interceptor { chain ->
            val request = chain.request().newBuilder()
//                .addHeader("X-CMC_PRO_API_KEY", "SET-HERE-YOUR-COINMARKET-KEY")
                .build()
            chain.proceed(request)
        }
    }

    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(get<Interceptor>())
            .addInterceptor(logging)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://pro-api.coinmarketcap.com")
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoApi::class.java)
    }

    single<DataStore<Preferences>> {
        androidContext().dataStore
    }

    single<CryptoRepository> {
        CryptoRepositoryImpl(
            api = get(),
            timeProvider = get(),
            cryptoDao = get(),
            dataStore = get()
        )
    }

    viewModel {
        CryptoListViewModel(get())
    }

    viewModel {
        CryptoDetailViewModel(get())
    }

    single<TimeProvider> { SystemTimeProvider() }
}
