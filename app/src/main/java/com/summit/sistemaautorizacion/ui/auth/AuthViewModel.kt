package com.summit.sistemaautorizacion.ui.auth

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.summit.sistemaautorizacion.common.conexion.Resource
import com.summit.sistemaautorizacion.common.detectar_formato
import com.summit.sistemaautorizacion.data.model.responseGeneral
import com.summit.sistemaautorizacion.data.repository.RepositoryAuth
import com.thesummitdev.daloo.rider.data.remote.model.request.requestSignIn
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AuthViewModel(private val repoAuth: RepositoryAuth) : ViewModel() {

    val isLoggedUser = repoAuth.selectProfile()

    fun deleteUserDB() = repoAuth.deleteProfile()

    fun signIn(pass: String,usuario:String): LiveData<Resource<responseGeneral>> = liveData {
        emit(Resource.Loading())
        try {
            val response= repoAuth.signIn(requestSignIn(usuario,pass))
            val information = repoAuth.getInformationProfile()
            repoAuth.insertProfile(information)
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }


    //COmprobaciones
    fun IsValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    fun IsValidPass(pass: String): Boolean = pass.length >= 6
    fun IsValidNumber(phone: String): Boolean = phone.length >= 9
    fun IsValidRuc(ruc: String): Boolean = ruc.length == 11
    fun IsValidNumberDoc(dni: String): Boolean = dni.length == 8


    /**Crea una informacion de usuario**/
    private fun guardarFotoEnArchivo(name: String, file: File): MultipartBody.Part? {
        var body: MultipartBody.Part? = null
        if (detectar_formato(file.path) == "ninguno") {
            throw Exception("Formato no valido de imagen")
        } else {
            val requestFile: RequestBody = RequestBody.create(
                MediaType.parse("image/" + detectar_formato(file.path)),
                file
            )
            body = MultipartBody.Part.createFormData(name, file.name, requestFile)
        }
        return body
    }
}