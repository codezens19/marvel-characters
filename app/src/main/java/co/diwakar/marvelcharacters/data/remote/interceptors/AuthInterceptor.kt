package co.diwakar.marvelcharacters.data.remote.interceptors

import co.diwakar.marvelcharacters.BuildConfig
import co.diwakar.marvelcharacters.util.HashUtils
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * It uses to append required query parameters for each request
 * [apiKey], [hash] and [timeStamp] with the original request
 * */
@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url
        val url = originalHttpUrl.newBuilder().apply {
            val timeStamp = System.currentTimeMillis()
            addQueryParameter("apikey", BuildConfig.MARVEL_API_PUBLIC_KEY)
            addQueryParameter("hash", HashUtils.hash(timeStamp))
            addQueryParameter("ts", "$timeStamp")
        }.build()
        val request = original.newBuilder().url(url).build()

        return chain.proceed(request)
    }
}