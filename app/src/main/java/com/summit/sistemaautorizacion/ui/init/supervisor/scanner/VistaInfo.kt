package com.summit.sistemaautorizacion.ui.init.supervisor.scanner

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.summit.sistemaautorizacion.R
import com.summit.sistemaautorizacion.common.conexion.Resource
import com.summit.sistemaautorizacion.data.model.Comerciantes
import com.summit.sistemaautorizacion.ui.init.GlobalViewModel
import com.summit.sistemaautorizacion.ui.init.GlobalViewModelFactory
import kotlinx.android.synthetic.main.fragment_vista_info.*
import org.jetbrains.anko.support.v4.toast
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class VistaInfo : DialogFragment(),KodeinAware {
    override val kodein: Kodein by kodein()
    lateinit var viewModel: GlobalViewModel
    private val factory: GlobalViewModelFactory by instance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = requireActivity().run {
            ViewModelProvider(this,factory).get(GlobalViewModel::class.java)
        }
        return inflater.inflate(R.layout.fragment_vista_info, container, false)
    }
    private lateinit var dialogPrincipal: Dialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        dialogPrincipal= super.onCreateDialog(savedInstanceState)
        return dialogPrincipal
    }
    override fun onStart() {
        super.onStart()
        dialogPrincipal.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
    }
    lateinit var id:String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cargarDatos()
        dialog_close.setOnClickListener {
            dialogPrincipal.dismiss()
        }
    }

    private fun cargarDatos() {
        viewModel.getinfoComerciante(id).observe(this, Observer {
            when(it){
                is Resource.Loading->{
                    progressBar.visibility=View.VISIBLE
                }
                is Resource.Success->{
                    progressBar.visibility=View.GONE
                    bindingData(it.data)
                }
                is Resource.Failure->{
                    progressBar.visibility=View.GONE
                    toast(it.exception.message!!)
                }
            }
        })
    }

    private fun bindingData(data: Comerciantes) {
        dialog_name.text= "${data.name} ${data.lastname}"
        dialog_dni.text= data.dni
        dialog_phone.text= data.celular
        dialog_asociacion.text= data.asociacion
    }

}