package fr.lorenzocacciato.hialib.persistence

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class DBAccessManager<S> (serviceClass: Class<S>) {

    // service instance
    private var service: S? = null

    init {
        // generate retrofit services
        service = Retrofit.Builder()
            .baseUrl(END_POINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<S>(serviceClass)

        // notify success
        Log.d(TAG, "HIA Retrofit services loaded !")
    }

    fun getService(): S?
    {
        return service
    }

    // generic call API method
    fun <T> Call<T>?.callAPI(callback: ResultCallback<T>) {
        this?.enqueue(object: Callback<T> {

            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onSuccess(response.body())
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onFailed()
            }

        })
    }

    // callback
    interface ResultCallback<T> {
        fun onSuccess(body: T?)
        fun onFailed()
    }

    // companion
    companion object {
        const val TAG = "ServiceGenerator"
        const val END_POINT = "http://hia.graven.yt/"
    }


}
