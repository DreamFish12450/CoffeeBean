package com.example.coffeebean.util

import android.content.Context
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File
import java.io.IOException


object Requests {
    //    private const val SERVER = "https://192.168.123.109:5001"
    private const val SERVER = "https://192.168.43.41:5001"
    var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    const val API_TEST = "$SERVER/hello"
    const val API_FOOD_DEADLINE = "$SERVER/foods/deadline"
    const val API_FOOD_RECENT = "$SERVER/foods/recent"
    const val API_MENU_GET_FAVOURITE = "$SERVER/menu/select/favourite"
    const val API_MENU_RECOMMEND = "$SERVER/menu/recommend"
    const val API_SPEAK_MESSAGE = "$SERVER/speak/message/get?key=message"
    const val API_LOGIN = "$SERVER/api/userInfo/"
    const val API_GET_ALL_PHONE = "$SERVER/api/PhoneRecord/"
    const val API_GET_CONTACT_INFO = "$SERVER/api/ContactInfo/"

    @JvmStatic
    fun post(
            url: String,
            params: HashMap<String, String>,
            callback: (Int, String?) -> Unit = { _, _ -> }
    ) {
        val okHttpClient = OkHttpClient()
        val formBody =
                FormBody.Builder().apply { params.entries.forEach { add(it.key, it.value) } }.build()
        val request = Request.Builder().post(formBody).url(url).build()
        try {
            val response = okHttpClient.newCall(request).execute()
            response.body?.string().let { callback.invoke(response.code, it) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        callback.invoke(-1, null)
    }

    @JvmStatic
    fun post(
            url: String,
            params: HashMap<String, String> = hashMapOf()
    ): Pair<Int, String?> {
        val okHttpClient = OkHttpClient()
        val formBody =
                FormBody.Builder().apply { params.entries.forEach { add(it.key, it.value) } }.build()
        val request = Request.Builder().post(formBody).url(url).build()
        try {
            val response = okHttpClient.newCall(request).execute()
            response.body?.string().let { return response.code to it }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return -1 to null
    }

    @JvmStatic
    fun get(
            url: String
    ): Pair<Int, String?> {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder().get().url(url).build()
        try {
            val response = okHttpClient.newCall(request).execute()
            response.body?.string().let { return response.code to it }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return -1 to null
    }

    @JvmStatic
    fun get(
            url: String,
            params: HashMap<String, String> = hashMapOf()
    ): Pair<Int, String?> {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder().get().url(url).build()
        try {
            val response = okHttpClient.newCall(request).execute()
            response.body?.string().let { return response.code to it }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return -1 to null
    }
}

