package mzx.taskmanager.di

import com.apollographql.apollo.ApolloClient
import mzx.taskmanager.BuildConfig
import mzx.taskmanager.api.TaskApi
import mzx.taskmanager.api.impl.TaskApiImp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module


val appModule = module {
    factory { setupApollo() }
    single<TaskApi> { TaskApiImp(get()) }
}

private fun setupApollo(): ApolloClient {
    val okHttp = OkHttpClient
        .Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder().method(
                original.method(),
                original.body()
            )
            builder.addHeader(
                "Authorization"
                , BuildConfig.AUTH_TOKEN
            )
            chain.proceed(builder.build())
        }.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY

        })
        .build()
    return ApolloClient.builder()
        .serverUrl(BuildConfig.SERVER_URL)
        .okHttpClient(okHttp)
        .build()
}