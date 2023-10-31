package com.example.namegenerator

import android.content.Context

import org.chromium.net.CronetEngine
import org.chromium.net.UrlRequest
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class NetworkRequest(context : Context) {

    private val cronetEngine: CronetEngine = CronetEngine.Builder(context).build()

    private val executor: Executor = Executors.newSingleThreadExecutor()

    private fun makeRequestBuilder(url : String, method : String, callback : UrlRequest.Callback): UrlRequest.Builder? {

        val requestBuilder = cronetEngine.newUrlRequestBuilder(url, callback, executor)
        requestBuilder.setHttpMethod(method)

        return requestBuilder
    }

    fun getRequest(url : String, callback : UrlRequest.Callback){
        val requestBuilder = makeRequestBuilder(url, "GET", callback)
        val request = requestBuilder?.build()
        request?.start()

    }
}