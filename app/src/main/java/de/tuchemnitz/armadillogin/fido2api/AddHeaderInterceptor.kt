package de.tuchemnitz.armadillogin.fido2api

import okhttp3.Interceptor
import okhttp3.Response

class AddHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
                chain.request().newBuilder()
                        .header("X-Requested-With", "XMLHttpRequest")
                        .build()
        )
    }
}