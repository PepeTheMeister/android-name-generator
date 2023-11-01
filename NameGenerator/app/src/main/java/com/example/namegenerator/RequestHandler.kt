package com.example.namegenerator

import android.util.Log
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.channels.Channels

private const val TAG = "MyUrlRequestCallback"

class RequestHandler(callBackInfo: OnFinishRequest) : UrlRequest.Callback() {

    private val delegate: OnFinishRequest = callBackInfo


    override fun onRedirectReceived(
        request: UrlRequest?,
        info: UrlResponseInfo?,
        newLocationUrl: String?
    ) {
        Log.i(TAG, "onRedirectReceived method called.")

        request?.followRedirect()
    }

    override fun onResponseStarted(request: UrlRequest?, info: UrlResponseInfo?) {
        Log.i(TAG, "onResponseStarted method called.")

        request?.read(ByteBuffer.allocateDirect(102400))
    }

    override fun onReadCompleted(
        request: UrlRequest?,
        info: UrlResponseInfo?,
        byteBuffer: ByteBuffer?
    ) {
        Log.i(TAG, "onReadCompleted method called.")

        request?.read(byteBuffer)

        val bytesReceived = ByteArrayOutputStream()
        val receiveChannel = Channels.newChannel(bytesReceived)

        byteBuffer?.flip()
        receiveChannel.write(byteBuffer)

        val byteArray = bytesReceived.toByteArray()

        val responsebody = String(byteArray).trim()
        responsebody.replace("\n", "")
        responsebody.replace("(\r\n|\n\r|\r|\n|\r0|\n0)", "")

        val results = JSONObject()
        results.put("body", responsebody)

        byteBuffer?.clear()

        delegate.onFinishRequest(results)

    }

    override fun onSucceeded(request: UrlRequest?, info: UrlResponseInfo?) {
        Log.i(TAG, "onSucceeded method called.")
    }

    override fun onFailed(request: UrlRequest?, info: UrlResponseInfo?, error: CronetException?) {
        Log.i(TAG, "onFailed method called.")
    }

    interface OnFinishRequest {
        fun onFinishRequest(result: JSONObject)
    }
}