package com.summit.sistemaautorizacion.ui.init.admin.comerciante

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.summit.sistemaautorizacion.R
import com.summit.sistemaautorizacion.base.BaseFragment
import com.summit.sistemaautorizacion.common.conexion.Resource
import com.summit.sistemaautorizacion.data.model.Comerciantes
import com.summit.sistemaautorizacion.ui.auth.AuthViewModel
import com.summit.sistemaautorizacion.ui.auth.AuthViewModelFactory
import com.summit.sistemaautorizacion.ui.init.GlobalViewModel
import com.summit.sistemaautorizacion.ui.init.GlobalViewModelFactory
import kotlinx.android.synthetic.main.fragment_update_comerciantes.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class UpdateComerciantesFragment : BaseFragment(),KodeinAware {

    override val kodein: Kodein by kodein()
    lateinit var viewModel: GlobalViewModel
    private val factory: GlobalViewModelFactory by instance()
    lateinit var authVM: AuthViewModel
    private val authFactory: AuthViewModelFactory by instance()
    lateinit var id:String
    override fun getLayout()=R.layout.fragment_update_comerciantes

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id= UpdateComerciantesFragmentArgs.fromBundle(requireArguments()).identificador
        viewModel = requireActivity().run {
            ViewModelProvider(this,factory).get(GlobalViewModel::class.java)
        }
        authVM = requireActivity().run {
            ViewModelProvider(this,authFactory).get(AuthViewModel::class.java)
        }
        loadData()
        loadExcel.setOnClickListener {

            if(comprobarDatos()){
                val asociacion = lbl_name_asociacion.textTrim()
                val name= lbl_name.textTrim()
                val lastname= lbl_lastname.textTrim()
                val dni= lbl_dni.textTrim()
                val celular= lbl_celular.textTrim()
                updatearDatos(asociacion,name,lastname,dni,celular)
            }
        }
    }

    private fun loadData() {
        viewModel.getinfoComerciante(id).observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    snakBarDefinitivo("cargando")
                }
                is Resource.Success->{
                    bindingData(it.data)
                }
                is Resource.Failure->{
                    snakBar(it.exception.message!!)
                }
            }
        })
    }

    private fun bindingData(data: Comerciantes) {
        lbl_name_asociacion.setText(data.asociacion)
        lbl_name.setText(data.name)
        lbl_lastname.setText(data.lastname)
        lbl_dni.setText(data.dni)
        lbl_celular.setText(data.celular)
    }

    private fun updatearDatos(asociacion: String, name: String, lastname: String, dni: String, celular: String) {
        viewModel.putComerciante(id, Comerciantes(asociacion,name,lastname,dni,celular)).observe(viewLifecycleOwner,
            Observer {
                when(it){
                    is Resource.Loading->{
                        snakBarDefinitivo("Subiendo")
                    }
                    is Resource.Success->{
                        snakBarDefinitivo("Se actualizo correctamente")
                        Handler()
                            .postDelayed({
                                findNavController().navigateUp()
                            },1000L)                }
                    is Resource.Failure->{
                        snakBar(it.exception.message!!)
                    }
                }
            })
    }

    private fun comprobarDatos():Boolean{
        if(!lbl_name_asociacion.lblVacio()){
            return false
        }
        if(!lbl_name.lblVacio()){
            return false
        }
        if(!lbl_lastname.lblVacio()){
            return false
        }
        if(!lbl_dni.lblVacio()){
            return false
        }
        if(!lbl_celular.lblVacio()){
            return false
        }
        if (lbl_celular.textTrim().length!=9){
            lbl_celular.error="Celular incorrecto"
            lbl_celular.requestFocus()
            return false
        }
        if (lbl_dni.textTrim().length!=8){
            lbl_celular.error="DNI incorrecto"
            lbl_celular.requestFocus()
            return false
        }
      return true
    }
}