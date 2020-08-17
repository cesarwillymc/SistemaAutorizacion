package com.summit.sistemaautorizacion.data.repository

import com.summit.sistemaautorizacion.common.conexion.SafeApiRequest
import com.summit.sistemaautorizacion.data.model.Comerciantes
import com.summit.sistemaautorizacion.data.model.Usuario
import com.summit.sistemaautorizacion.data.retrofit.ApiRetrofit
import com.summit.sistemaautorizacion.data.room.AppDB
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RepositoryGlobal(private val api:ApiRetrofit,private val db:AppDB):SafeApiRequest() {

    suspend fun  getComerciante(id:String)= apiRequest { api.getInfoComerciante(id)}
    suspend fun  putComerciante(comerciantes: Comerciantes,id:String) = apiRequest { api.putComerciante(id,comerciantes)}
    suspend fun deleteComerciante(id:String)= apiRequest { api.deletecomerciante(id) }
    suspend fun uploadExcel(file: MultipartBody.Part, name: RequestBody) = apiRequest { api.uploadExcel(file,name) }
    suspend fun getListComerciantes() = apiRequest { api.getListComerciante() }
    suspend fun getPdfQr(id: String) = apiRequest { api.getQRcomerciante(id) }


    //Personal
    suspend fun getListPersonal() = apiRequest { api.getListPersonal() }
    suspend fun putPersonal(id: String,usuario: Usuario) = apiRequest { api.putPersonal(id,usuario) }
    suspend fun updateFoto(file: MultipartBody.Part, name: RequestBody, key:String) = apiRequest { api.sendImageWithId(file,name,key) }
    suspend fun deletePersonal(id: String) = apiRequest { api.deletepersonal(id) }
    suspend fun getInfoPersonal (id:String)= apiRequest { api.getInfoPersonal(id) }
    suspend fun createPersonal(usuario: Usuario) = apiRequest { api.createPersonal(usuario) }

}