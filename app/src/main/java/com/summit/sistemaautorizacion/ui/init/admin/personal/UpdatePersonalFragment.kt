package com.summit.sistemaautorizacion.ui.init.admin.personal

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.summit.sistemaautorizacion.R
import com.summit.sistemaautorizacion.base.BaseFragment
import com.summit.sistemaautorizacion.common.Constants
import com.summit.sistemaautorizacion.common.Constants.BASE_URL_AMAZON_IMG
import com.summit.sistemaautorizacion.common.Constants.tipe
import com.summit.sistemaautorizacion.common.conexion.Resource
import com.summit.sistemaautorizacion.data.model.PersonalList
import com.summit.sistemaautorizacion.data.model.Usuario
import com.summit.sistemaautorizacion.ui.auth.AuthViewModel
import com.summit.sistemaautorizacion.ui.auth.AuthViewModelFactory
import com.summit.sistemaautorizacion.ui.camerax.ViewModelMain
import com.summit.sistemaautorizacion.ui.init.GlobalViewModel
import com.summit.sistemaautorizacion.ui.init.GlobalViewModelFactory
import kotlinx.android.synthetic.main.fragment_update_personal.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.File


class UpdatePersonalFragment : BaseFragment(),KodeinAware {


    override val kodein: Kodein by kodein()
    lateinit var viewModel: GlobalViewModel
    private val factory: GlobalViewModelFactory by instance()
    lateinit var authVM: AuthViewModel
    private val authFactory: AuthViewModelFactory by instance()
    lateinit var id:String
    override fun getLayout()=R.layout.fragment_update_personal
    lateinit var viewodel : ViewModelMain
    lateinit var itemsAdapter: ArrayAdapter<String>
    lateinit var datos:PersonalList
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id= UpdatePersonalFragmentArgs.fromBundle(requireArguments()).identificador
        datos= UpdatePersonalFragmentArgs.fromBundle(requireArguments()).datos
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
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, tipe)
        spinner_tipe.adapter= itemsAdapter
        loadData()
        viewodel.imageSelect.observe(viewLifecycleOwner, Observer {
            if (it!=""){
                updateFoto(File(it))
            }else{
                Glide.with(requireContext()).load(  R.drawable.user).into(lbl_image_profile)
                null
            }
        })
        lbl_image_profile.setOnClickListener {
            findNavController().navigate(UpdatePersonalFragmentDirections.actionUpdatePersonalFragmentToGalleryFragment())
        }
        lbl_actualizar_personal.setOnClickListener {


            if (comprobarDatos()){
                val name= lbl_name_personal.textTrim()
                val lastname= lbl_lastname_personal.textTrim()
                val dni= lbl_dni_personal.textTrim()
                val celular= lbl_celular_personal.textTrim()
                val tipe= spinner_tipe.selectedItem.toString()

                sendEnviarDatos(name,lastname,dni,celular,tipe)
            }
        }
        change_password.setOnClickListener {
            if (lbl_password_personal.lblVacio()){
                val pasword= lbl_password_personal.text.toString().trim()
                cambiarContraseña(pasword)
            }
        }
    }

    private fun cambiarContraseña(pasword: String) {
        viewModel.changePasswordPersonal(id,pasword).observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    snakBar("cambiando contraseña")
                }
                is Resource.Success->{
                    snakBarDefinitivo("Se  cambio correctamente")
                }
                is Resource.Failure->{
                    snakBar(it.exception.message!!)
                }
            }
        })
    }

    private fun loadData() {
        bindingData(datos)
        /*viewModel.getinfoPersonal(id).observe(viewLifecycleOwner, Observer {
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
        })*/
    }

    private fun bindingData(data: PersonalList) {
        lbl_password_personal.setText("Contraseña cifrada")
        lbl_name_personal.setText(data.name)
        lbl_lastname_personal.setText(data.lastname)
        lbl_dni_personal.setText(data.dni)
        lbl_celular_personal.setText(data.phone)
        if (data.role== Constants.TIPE_SUPERVISOR){
            spinner_tipe.setSelection(1)
            spinner_tipe.isSelected=true
        }
    }



    private fun updateFoto(file: File) {
        viewModel.updateImage(id,file,"imagen").observe(viewLifecycleOwner,
            Observer {
                when(it){
                    is Resource.Loading->{
                        snakBar("cargando")
                    }
                    is Resource.Success->{
                        Glide.with(requireContext()).load( BASE_URL_AMAZON_IMG+it.data.message).into(lbl_image_profile)

                    }
                    is Resource.Failure->{
                        snakBar(it.exception.message!!)
                    }
                }
            })

    }
    private fun sendEnviarDatos(
        name: String,
        lastname: String,
        dni: String,
        celular: String,
        tipe: String
    ) {
        viewModel.putPersonal(id, Usuario(name,lastname,dni,celular,tipe)).observe(viewLifecycleOwner,
            Observer {
                when(it){
                    is Resource.Loading->{
                        snakBar("modificando")
                    }
                    is Resource.Success->{
                        snakBarDefinitivo("Se modifico correctamente")

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
        if(!lbl_lastname_personal.lblVacio()){
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

        return true
    }
}