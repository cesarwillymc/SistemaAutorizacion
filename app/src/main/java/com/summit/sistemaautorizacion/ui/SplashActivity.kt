package com.summit.sistemaautorizacion.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.summit.sistemaautorizacion.R
import com.summit.sistemaautorizacion.base.BaseActivity
import com.summit.sistemaautorizacion.common.Constants.TIPE_ADMIN
import com.summit.sistemaautorizacion.common.Constants.TIPE_SUPERVISOR
import com.summit.sistemaautorizacion.ui.auth.AuthActivity
import com.summit.sistemaautorizacion.ui.auth.AuthViewModel
import com.summit.sistemaautorizacion.ui.auth.AuthViewModelFactory
import com.summit.sistemaautorizacion.ui.init.admin.AdminActivity
import com.summit.sistemaautorizacion.ui.init.supervisor.SupervisorActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SplashActivity : BaseActivity(),KodeinAware {
    override val kodein: Kodein by kodein()
    lateinit var authVM: AuthViewModel
    private val authFactory: AuthViewModelFactory by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authVM = run {
            ViewModelProvider(this,authFactory).get(AuthViewModel::class.java)
        }
        Handler().postDelayed({
            navActivity()
        },1500L)
    }

    private fun navActivity() {
        authVM.isLoggedUser.observe(this, Observer {
            if (it!=null){
                when (it.role) {
                    TIPE_SUPERVISOR -> {
                        navigateToActivity(Intent(this,SupervisorActivity::class.java))
                    }
                    TIPE_ADMIN -> {
                        navigateToActivity(Intent(this,AdminActivity::class.java))
                    }
                    else -> {
                        navigateToActivity(Intent(this,AuthActivity::class.java))
                    }
                }
            }else{
                navigateToActivity(Intent(this,AuthActivity::class.java))
            }
        })
    }

    override fun getLayout()= R.layout.activity_splash
}