package com.example.movies_n_chill

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.*


interface ServerCallback {
    fun onSuccess(result: Bundle?)
    fun onFail(message: String)
    fun onError(message: String)
}

class MovieApi {
    val BASE_URL = "https://imdb-api.com/en/API/Title/k_0luvwxcs/"
    fun getTitleById( queue: RequestQueue, context:Context,  callback: ServerCallback, titleId:String) {
        val bundleResponse = Bundle()
        val request = object : StringRequest(Request.Method.GET, BASE_URL+titleId,
            object : Response.Listener<String> {
                override fun onResponse(response: String) {
                    Log.i("response", response)
                    try {
                        val jsonObject = JSONObject(response)
                        if ( jsonObject.getString("title") !== "null") {
                            bundleResponse.putString("movie", jsonObject.toString())
                            callback.onSuccess(bundleResponse)
                        } else callback.onError("No Title")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                       callback.onError("No Internet")
                    }
                }
            }, Response.ErrorListener { callback.onError("")}) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                return super.getParams()
            }
        }
        request.setRetryPolicy(DefaultRetryPolicy(35000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        queue.add(request)
    }
}