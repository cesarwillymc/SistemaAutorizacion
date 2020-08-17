package com.summit.sistemaautorizacion.common

import android.app.Activity
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.*


fun Activity.getPath(uri: Uri): String {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = managedQuery(uri, projection, null, null, null)
    startManagingCursor(cursor)
    val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    return cursor.getString(column_index)
}
fun detectar_formato(texto: String): String {
    return if (texto.contains("jpg")) {
        "jpg"
    } else if (texto.contains("jpeg")) {
        "jpeg"
    }  else
        "ninguno"
}

fun<T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>>{
    return lazy {
        GlobalScope.async (start = CoroutineStart.LAZY){
            block.invoke(this)
        }
    }
}