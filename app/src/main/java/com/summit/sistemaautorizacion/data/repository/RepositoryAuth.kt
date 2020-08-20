package com.summit.sistemaautorizacion.data.repository

import android.util.Log
import com.summit.sistemaautorizacion.common.Constants
import com.summit.sistemaautorizacion.common.Constants.PREF_ID_USER
import com.summit.sistemaautorizacion.common.SharedPreferencsManager.Companion.setSomeStringValue
import com.summit.sistemaautorizacion.common.conexion.SafeApiRequest
import com.summit.sistemaautorizacion.data.model.Usuario
import com.summit.sistemaautorizacion.data.model.responseGeneral
import com.summit.sistemaautorizacion.data.retrofit.ApiRetrofit
import com.summit.sistemaautorizacion.data.room.AppDB
import com.thesummitdev.daloo.rider.data.remote.model.request.requestSignIn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RepositoryAuth(private val api: ApiRetrofit,private val db: AppDB):SafeApiRequest() {

    //Acceso a datos desde la db
    /**Selecciona la informacion de usuario de la base de datos room**/
    fun selectProfile()= db.profileDao.selectProfile()

    /**Borra la tabla de usuario de la base de datos room**/
    fun deleteProfile()= db.profileDao.deleteProfile()

    /**Actualiza la tabla de usuario de la base de datos room**/
    fun updateProfile(profile: Usuario)= db.profileDao.updateProfile(profile)

    /**Inserta informacion de usuario de la base de datos room**/
    fun insertProfile(profile: Usuario)= db.profileDao.insertProfile(profile)


    //Acceso a retrofit

    /**Acceso a login mediante correo y contraseña**/
    suspend fun signIn(requestSignIn: requestSignIn): responseGeneral {
        val response= apiRequest { api.loginUser(requestSignIn) }
       // setSomeStringValue(Constants.PREF_TOKEN,response.token)
        Log.e("token",response.token)
        return response
    }


    /**Obtiene la informacion de la cuenta**/
    suspend fun getInformationProfile(): Usuario {
        val dato =apiRequest { api.getUserInfo()  }
        setSomeStringValue(PREF_ID_USER,dato._id)
        return dato
    }

    /**Registro de Docente**/





    /**Subida de imagenes por id**/
    suspend fun signUpImage(file: MultipartBody.Part, name: RequestBody, key:String?=null)= apiRequest { api.sendImageWithId(file,name,key) }

}