package com.summit.sistemaautorizacion.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar


abstract class BaseFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    fun View.show(){
        this.visibility=View.VISIBLE
    }
    fun View.hide(){
        this.visibility=View.GONE
    }
    fun hideKeyboard(){
        try{
            requireView().clearFocus()
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        }catch (e :Exception){

        }
    }
    fun EditText.showKeyboard(){
        try{
            if(this.requestFocus()){
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
            }
        }catch (e :Exception){
            Log.e("error",e.message!!)
        }
    }
    fun forceShowKeyboard(){
        try{
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        }catch (e :Exception){
            Log.e("error",e.message!!)
        }
    }

    fun navigateToActivity(intent: Intent) {
        intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }
    fun toast(mensaje:String){
        Toast.makeText(requireContext(),mensaje, Toast.LENGTH_LONG).show()
    }
    fun snakBar(mensaje: String){
        Snackbar.make(requireView(),mensaje, Snackbar.LENGTH_LONG).also { snackbar ->
            snackbar.setAction("Ok"){
                snackbar.dismiss()
            }.show()
        }
    }
    fun snakBarDefinitivo(mensaje: String){
        Snackbar.make(requireView(),mensaje, Snackbar.LENGTH_INDEFINITE).also { snackbar ->
            snackbar.setAction("Ok"){
                snackbar.dismiss()
            }.show()
        }
    }
    fun EditText.textTrim():String{
        return ""+ text.toString().trim()
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
    @LayoutRes
    protected abstract fun getLayout():Int

}