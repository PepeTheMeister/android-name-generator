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

class RequestHandler(callBackInfo: OnFinishRequest) : UrlRequest.Callback(){

    private val delegate : OnFinishRequest = callBackInfo


    override fun onRedirectReceived(request: UrlRequest?, info: UrlResponseInfo?, newLocationUrl: String?) {
        Log.i(TAG, "onRedirectReceived method called.")
        // You should call the request.followRedirect() method to continue
        // processing the request.
        request?.followRedirect()
    }

    override fun onResponseStarted(request: UrlRequest?, info: UrlResponseInfo?) {
        Log.i(TAG, "onResponseStarted method called.")
        // You should call the request.read() method before the request can be
        // further processed. The following instruction provides a ByteBuffer object
        // with a capacity of 102400 bytes for the read() method. The same buffer
        // with data is passed to the onReadCompleted() method.
        request?.read(ByteBuffer.allocateDirect(102400))
    }

    override fun onReadCompleted(request: UrlRequest?, info: UrlResponseInfo?, byteBuffer: ByteBuffer?) {
        Log.i(TAG, "onReadCompleted method called.")
        // You should keep reading the request until there's no more data.
        request?.read(byteBuffer)

        val bytesReceived = ByteArrayOutputStream()
        val receiveChannel = Channels.newChannel(bytesReceived)

        byteBuffer?.flip()
        receiveChannel.write(byteBuffer)

        val byteArray = bytesReceived.toByteArray()
        /*if (byteBuffer != null) {
            if(byteBuffer.hasArray()){
                byteArray = byteBuffer.array()
            }
            else{
                val remainingBytes = byteBuffer.remaining()
                val tempByteArray = ByteArray(remainingBytes)
                byteBuffer.get(tempByteArray)
            }
        }*/

        val responsebody = String(byteArray).trim()
        responsebody.replace("\n", "")
        responsebody.replace(" ", "")



        var results = JSONObject()
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

    interface  OnFinishRequest{
        fun onFinishRequest(result : JSONObject)
    }
}