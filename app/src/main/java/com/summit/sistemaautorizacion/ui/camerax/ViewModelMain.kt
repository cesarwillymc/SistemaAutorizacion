package com.summit.sistemaautorizacion.ui.camerax

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.Display
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.summit.sistemaautorizacion.common.ConvertImage
import com.summit.sistemaautorizacion.common.conexion.Resource
import java.io.File
import java.util.*
import kotlin.Comparator

class ViewModelMain : ViewModel(){

    fun getImageFile(context: Context, path:String): LiveData<Resource<String>> = liveData{
        emit(Resource.Loading())
        try {
            val response = ConvertImage(
                context
            ).compressImage(path)!!
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }

    val imageSelect= MutableLiveData<String>("")

    val cargoImagen= MutableLiveData<Uri>(null)

    var displayInt=0
    var displayValue:Display?=null
    fun listImages(dato:String) = liveData{
        Log.e("dato",dato)
        val imagesDir= File(dato)
        val dato= imagesDir.listFiles { file ->
            EXTENSION_WHITELIST.contains(file.extension.toUpperCase(Locale.ROOT))
        }?.toMutableList() ?: mutableListOf()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dato.sortWith(Comparator.comparing(File::lastModified).reversed())
        }else{
            val constantLastModifiedTimes: MutableMap<File, Long> = HashMap()
            for (f in dato) {
                constantLastModifiedTimes[f] = f.lastModified()
            }
            dato.sortWith(  Comparator { f1, f2 -> constantLastModifiedTimes[f2]!!.compareTo(constantLastModifiedTimes[f1]!!) })
        }
        emit(dato)

    }
}