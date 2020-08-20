package com.summit.sistemaautorizacion.common.conexion

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.summit.sistemaautorizacion.common.Constants.PREF_TOKEN
import com.summit.sistemaautorizacion.common.SharedPreferencsManager.Companion.setSomeStringValue
import com.summit.sistemaautorizacion.data.model.ErrorBody
import retrofit2.Response


abstract class SafeApiRequest {
    suspend fun <T:Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful){
            try { if (response.headers().get("access_token")!=null){
                val token =  response.headers().get("access_token").toString()
                Log.e("token",token)
                setSomeStringValue(PREF_TOKEN,token)
            } }catch (e:Exception){ }
            return response.body()!!
        }else{
            val error= response.errorBody().toString()

            val message= StringBuilder()
            error?.let {
                try {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorBody>() {}.type
                    val errorResponse: ErrorBody? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    Log.e("apisafe",errorResponse!!.message)
                    message.append(errorResponse.message)
                }catch (e: Exception){ }
                message.append("\n")
            }
            message.append("Error code: ${response.code()}")
            throw Exception(message.toString())
        }
    }
}