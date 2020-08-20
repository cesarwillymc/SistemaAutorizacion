package com.summit.sistemaautorizacion.base

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.summit.sistemaautorizacion.R
import java.io.File

abstract  class BaseActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
       // window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(getLayout())
    }
    fun hideKeyboard(){
        try{
            val view = this.currentFocus
            view!!.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }catch (e :Exception){

        }
    }
    fun navigateToActivity(intent:Intent) {
        intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
    @LayoutRes
    protected abstract fun getLayout():Int
    fun View.snackbar(message:String){
        Snackbar.make(this,message, Snackbar.LENGTH_LONG).also { snackbar ->
            snackbar.setAction("Ok"){
                snackbar.dismiss()
            }.show()
        }
    }
    fun View.show(){
        this.visibility=View.VISIBLE
    }
    fun View.hide(){
        this.visibility=View.GONE
    }
    fun showUpdateVersion(){
        val dialogBuilder= AlertDialog.Builder(this)
        dialogBuilder.setMessage("Existe una nueva actualizacion de esta aplicacion, por favor actualizala")
            .setCancelable(false)
            .setPositiveButton("Actualizar ahora",DialogInterface.OnClickListener { dialog, which ->
                openAppInPlayStore("")
                dialog.dismiss()
            })
        val alertDialog= dialogBuilder.create()
        alertDialog.setTitle("Actualizacion Disponible")
        alertDialog.show()
    }
    fun toast(mensaje:String){
        Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show()
    }

    private fun openAppInPlayStore(appPackageName:String ){
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        }catch (e:ActivityNotFoundException){
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }

    fun EditText.textTrim():String{
        return this.text.toString().trim()
    }
    fun EditText.lblVacio():Boolean{
        if (this.text.toString().trim().isEmpty()){
            this.error="Esta vacio"
            this.requestFocus()
            return false
        }
        return true
    }
    fun EditText.lblEmail():Boolean{
        if (Patterns.EMAIL_ADDRESS.matcher(this.text.toString().trim()).matches()){
            return true
        }
        this.error="Email incorrecto!"
        this.requestFocus()
        return false
    }
    fun EditText.lblPassword():Boolean{
        if (this.text.toString().trim().length>6){
            return true
        }
        this.error="Password incorrecto!"
        this.requestFocus()
        return false
    }
    companion object {

        /** Use external media if it is available, our app's file directory otherwise */
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }
}