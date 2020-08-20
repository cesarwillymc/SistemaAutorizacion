package com.summit.sistemaautorizacion.ui.init.admin.personal

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.summit.sistemaautorizacion.R
import com.summit.sistemaautorizacion.base.BaseFragment
import com.summit.sistemaautorizacion.common.Constants
import com.summit.sistemaautorizacion.common.conexion.Resource
import com.summit.sistemaautorizacion.data.model.Usuario
import com.summit.sistemaautorizacion.ui.auth.AuthViewModel
import com.summit.sistemaautorizacion.ui.auth.AuthViewModelFactory
import com.summit.sistemaautorizacion.ui.camerax.ViewModelMain
import com.summit.sistemaautorizacion.ui.init.GlobalViewModel
import com.summit.sistemaautorizacion.ui.init.GlobalViewModelFactory
import kotlinx.android.synthetic.main.fragment_register_personal.*
import kotlinx.android.synthetic.main.fragment_register_personal.lbl_actualizar_personal
import kotlinx.android.synthetic.main.fragment_register_personal.lbl_celular_personal
import kotlinx.android.synthetic.main.fragment_register_personal.lbl_dni_personal
import kotlinx.android.synthetic.main.fragment_register_personal.lbl_image_profile
import kotlinx.android.synthetic.main.fragment_register_personal.lbl_name_personal
import kotlinx.android.synthetic.main.fragment_register_personal.lbl_password_personal
import kotlinx.android.synthetic.main.fragment_register_personal.spinner_tipe
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.File


class RegisterPersonalFragment : BaseFragment(),KodeinAware {


    override val kodein: Kodein by kodein()
    lateinit var viewModel: GlobalViewModel
    private val factory: GlobalViewModelFactory by instance()
    lateinit var authVM: AuthViewModel
    private val authFactory: AuthViewModelFactory by instance()
    var file: File? = null
    lateinit var viewodel : ViewModelMain
    lateinit var itemsAdapter: ArrayAdapter<String>
    override fun getLayout()=R.layout.fragment_register_personal

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = requireActivity().run {
            ViewModelProvider(this,factory).get(GlobalViewModel::class.java)
        }
        authVM = requireActivity().run {
            ViewModelProvider(this,authFactory).get(AuthViewModel::class.java)
        }
        viewodel= requireActivity().run {
            ViewModelProvider(this).get(ViewModelMain::class.java)
        }
        itemsAdapter=
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, Constants.tipe)
        spinner_tipe.adapter= itemsAdapter


        viewodel.imageSelect.observe(viewLifecycleOwner, Observer {
            file = if (it!=""){
                Glide.with(requireContext()).load(File(it)).into(lbl_image_profile)
                File(it)
            }else{
                Glide.with(requireContext()).load(  R.drawable.user).into(lbl_image_profile)
                null
            }
        })
        lbl_image_profile.setOnClickListener {
            findNavController().navigate(RegisterPersonalFragmentDirections.actionRegisterPersonalFragmentToGalleryFragment())
        }
        lbl_actualizar_personal.setOnClickListener {
            if (comprobarDatos()){
                val name= lbl_name_personal.textTrim()
                val lastname= lbl_lastname__personal.textTrim()
                val dni= lbl_dni_personal.textTrim()
                val pasword= lbl_password_personal.textTrim()
                val celular= lbl_celular_personal.textTrim()
                val tipe= spinner_tipe.selectedItem.toString()
                enviarDatos(name,lastname,dni,pasword,celular,tipe)
            }
        }
    }

    private fun enviarDatos(
        name: String,
        lastname: String,
        dni: String,
        pasword: String,
        celular: String,
        tipe: String
    ) {
        viewModel.createPersonal(Usuario(name, lastname, dni,celular,tipe,pasword),file!!,"imagen").observe(viewLifecycleOwner,
            Observer {
                when(it){
                    is Resource.Loading->{
                        snakBar("creando usuario")
                    }
                    is Resource.Success->{
                        snakBarDefinitivo("Se creo correctamente")
                        Handler().postDelayed({
                            findNavController().navigateUp()
                        },1000L)
                    }
                    is Resource.Failure->{
                        snakBar(it.exception.message!!)
                    }
                }
            })
    }

    private fun comprobarDatos():Boolean{
        if(!lbl_password_personal.lblVacio()){
            return false
        }
        if(!lbl_name_personal.lblVacio()){
            return false
        }
        if(!lbl_lastname__personal.lblVacio()){
            return false
        }
        if(!lbl_dni_personal.lblVacio()){
            return false
        }
        if(!lbl_celular_personal.lblVacio()){
            return false
        }
        if (lbl_celular_personal.textTrim().length!=9){
            lbl_celular_personal.error="Celular incorrecto"
            lbl_celular_personal.requestFocus()
            return false
        }
        if (lbl_dni_personal.textTrim().length!=8){
            lbl_dni_personal.error="DNI incorrecto"
            lbl_dni_personal.requestFocus()
            return false
        }
        if (file==null){
            snakBar("Foto no puede estar vacio")
            return false
        }
        return true
    }
}