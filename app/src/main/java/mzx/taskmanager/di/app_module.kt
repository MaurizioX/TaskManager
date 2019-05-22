package mzx.taskmanager.di

import android.util.Log
import com.apollographql.apollo.ApolloClient
import mzx.taskmanager.BuildConfig
import mzx.taskmanager.api.TaskApi
import mzx.taskmanager.api.impl.TaskApiImp
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import org.koin.dsl.module
import timber.log.Timber
import java.io.IOException


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

class LoggingInterceptor : Interceptor {

    @Throws(IOException::class)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val t1 = System.nanoTime()
        Log.d(
            "OkHttp",
            String.format("--> Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers())
        )

        val requestBuffer = Buffer()
        request.body()?.writeTo(requestBuffer)
        Log.d("OkHttp", requestBuffer.readUtf8())

        val response = chain.proceed(request)

        val t2 = System.nanoTime()
        Log.d(
            "OkHttp",
            String.format(
                "<-- Received response for %s in %.1fms%n%s",
                response.request().url(),
                (t2 - t1) / 1e6,
                response.headers()
            )
        )

        val contentType = response.body()?.contentType()
        val content = response.body()?.string()
        Timber.d("OkHttp $content")

        val wrappedBody = ResponseBody.create(contentType, content!!)
        return response.newBuilder().body(wrappedBody).build()
    }
}