package de.tuchemnitz.armadillogin.fido2api

class AddHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
                chain.request().newBuilder()
                        .header("X-Requested-With", "XMLHttpRequest")
                        .build()
        )
    }
}