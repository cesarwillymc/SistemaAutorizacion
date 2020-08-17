package com.summit.sistemaautorizacion.ui.init

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.summit.sistemaautorizacion.common.conexion.Resource
import com.summit.sistemaautorizacion.common.detectar_formato
import com.summit.sistemaautorizacion.data.model.*
import com.summit.sistemaautorizacion.data.repository.RepositoryAuth
import com.summit.sistemaautorizacion.data.repository.RepositoryGlobal
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class GlobalViewModel(
    private val auth: RepositoryAuth,
    private val repo: RepositoryGlobal
) :ViewModel(){
    fun loadListPersonal(): LiveData<Resource<List<PersonalList>>> = liveData {
        emit(Resource.Loading())
        try {
            val response: List<PersonalList> = repo.getListPersonal()
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
    fun putPersonal(id:String,usuario: Usuario): LiveData<Resource<responseGeneral>> = liveData {
        emit(Resource.Loading())
        try {
            val response = repo.putPersonal(id,usuario)
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
    fun deletePersonal(id:String): LiveData<Resource<responseGeneral>> = liveData {
        emit(Resource.Loading())
        try {
            val response = repo.deletePersonal(id)
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
    fun updateImage(id:String, file: File, name:String): LiveData<Resource<responseGeneral>> = liveData {
        emit(Resource.Loading())
        try {
            val nameFile = RequestBody.create(MediaType.parse("text/plain"),name )
            val archivo=guardarFotoEnArchivo(name,file)
            val response = repo.updateFoto(archivo!!,nameFile,id)
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
    fun getinfoPersonal(id:String): LiveData<Resource<Usuario>> = liveData {
        emit(Resource.Loading())
        try {
            val response = repo.getInfoPersonal(id)
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
    fun createPersonal(dato:Usuario, file: File, name:String): LiveData<Resource<responseGeneral>> = liveData {
        emit(Resource.Loading())
        try {
            val respuesta=  repo.createPersonal(dato)
            val nameFile = RequestBody.create(MediaType.parse("text/plain"),name )
            val archivo=guardarFotoEnArchivo(name,file)
            val response = repo.updateFoto(archivo!!,nameFile,respuesta.message)
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }

    /** COMERCIANTE**/
    fun loadListComerciante(): LiveData<Resource<List<ComercianteList>>> = liveData {
        emit(Resource.Loading())
        try {
            val response = repo.getListComerciantes()
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
    fun putComerciante(id:String,usuario: Comerciantes): LiveData<Resource<responseGeneral>> = liveData {
        emit(Resource.Loading())
        try {
            val response = repo.putComerciante(usuario,id)
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
    fun deleteComerciante(id:String): LiveData<Resource<responseGeneral>> = liveData {
        emit(Resource.Loading())
        try {
            val response = repo.deleteComerciante(id)
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }

    fun getinfoComerciante(id:String): LiveData<Resource<Comerciantes>> = liveData {
        emit(Resource.Loading())
        try {
            val response = repo.getComerciante(id)
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }


    fun getPdfQr(id:String): LiveData<Resource<responseGeneral>> = liveData {
        emit(Resource.Loading())
        try {
            val response = repo.getPdfQr(id)
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }

    fun uploadExcel(file: File, name:String,tipe:String): LiveData<Resource<responseGeneral>> = liveData {
        emit(Resource.Loading())
        try {
            val nameFile = RequestBody.create(MediaType.parse("text/plain"),name )
            val archivo=guardarFotoEnArchivo(name,file)
            val response = repo.uploadExcel(archivo!!,nameFile)
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }














    private fun guardarFotoEnArchivo(name: String, file: File): MultipartBody.Part? {
        var body: MultipartBody.Part? = null
        if (detectar_formato(file.path) == "ninguno") {
            throw Exception("Formato no valido de imagen")
        } else {
            val requestFile: RequestBody = RequestBody.create(
                MediaType.parse("image/" + detectar_formato(file.path)),
                file
            )
            body = MultipartBody.Part.createFormData(name,file.name, requestFile)
        }
        return body
    }
    private fun guardarExcelArchivo(name: String, file: File,tipe: String): MultipartBody.Part? {
        var body: MultipartBody.Part? = null
            val requestFile: RequestBody = RequestBody.create(
                MediaType.parse(tipe),
                file
            )
            body = MultipartBody.Part.createFormData(name,file.name, requestFile)

        return body
    }
}