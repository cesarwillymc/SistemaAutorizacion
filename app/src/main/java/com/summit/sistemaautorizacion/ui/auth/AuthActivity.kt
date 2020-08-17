package com.summit.sistemaautorizacion.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.BaseAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.summit.sistemaautorizacion.R
import com.summit.sistemaautorizacion.base.BaseActivity
import com.summit.sistemaautorizacion.common.Constants
import com.summit.sistemaautorizacion.common.conexion.Resource
import com.summit.sistemaautorizacion.ui.init.GlobalViewModel
import com.summit.sistemaautorizacion.ui.init.GlobalViewModelFactory
import com.summit.sistemaautorizacion.ui.init.admin.AdminActivity
import com.summit.sistemaautorizacion.ui.init.supervisor.SupervisorActivity
import kotlinx.android.synthetic.main.activity_auth.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class AuthActivity : BaseActivity(),KodeinAware {
    override val kodein: Kodein by kodein()
    lateinit var authVM:AuthViewModel
    private val authFactory:AuthViewModelFactory by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authVM = run {
            ViewModelProvider(this,authFactory).get(AuthViewModel::class.java)
        }
        signin_btn.setOnClickListener {
            val user= signin_lbl_user.text.toString().trim()
            val pass= signin_lbl_pass.text.toString().trim()
            if(comprobarDatos(user,pass)){
                signInDatos(user,pass)
            }
        }
        checketIsLoggedUser()
    }
    private fun checketIsLoggedUser(){
        authVM.isLoggedUser.observe(this, Observer {
            if (it!=null){
                when (it.role) {
                    Constants.TIPE_SUPERVISOR -> {
                        navigateToActivity(Intent(this, SupervisorActivity::class.java))
                    }
                    Constants.TIPE_ADMIN -> {
                        navigateToActivity(Intent(this, AdminActivity::class.java))
                    }
                    else -> {
                        toast("Contactate con un administrador, tu cuenta no es valida")
                    }
                }
            }
        })
    }
    //Llamada a retrofit para inicializar los datos
    private fun signInDatos(user: String, pass: String) {
        authVM.signIn(pass,user).observe(this, Observer {
            when(it){
                is Resource.Loading->{
                    progress.show()
                }
                is Resource.Success->{
                    progress.hide()
                    toast("Logeo exitoso")
                }
                is Resource.Failure->{
                    progress.hide()
                    toast(it.exception.message!!)
                }
            }
        })
    }
    //Compruba datos de acceso
    private fun comprobarDatos(usuario:String,pass:String):Boolean{
        if (!signin_lbl_user.lblVacio()){
            return false
        }
        if (!authVM.IsValidNumberDoc(usuario)){
            signin_lbl_user.error="Dni no valido"
            signin_lbl_user.requestFocus()
            return false
        }
        if (!signin_lbl_pass.lblVacio()){
            return false
        }
        if (!authVM.IsValidPass(pass)){
            signin_lbl_pass.error="contraseña incorrecta"
            signin_lbl_pass.requestFocus()
            return false
        }
        return true
    }
    override fun getLayout()= R.layout.activity_auth
}