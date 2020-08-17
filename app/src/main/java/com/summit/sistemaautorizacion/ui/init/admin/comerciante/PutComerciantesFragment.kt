package com.summit.sistemaautorizacion.ui.init.admin.comerciante

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.summit.sistemaautorizacion.R
import com.summit.sistemaautorizacion.base.BaseFragment
import com.summit.sistemaautorizacion.common.conexion.Resource
import com.summit.sistemaautorizacion.common.getPath
import com.summit.sistemaautorizacion.ui.auth.AuthViewModel
import com.summit.sistemaautorizacion.ui.auth.AuthViewModelFactory
import com.summit.sistemaautorizacion.ui.init.GlobalViewModel
import com.summit.sistemaautorizacion.ui.init.GlobalViewModelFactory
import kotlinx.android.synthetic.main.fragment_put_comerciantes.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.File


class PutComerciantesFragment : BaseFragment(),KodeinAware {

    override val kodein: Kodein by kodein()
    lateinit var viewModel: GlobalViewModel
    private val factory: GlobalViewModelFactory by instance()
    lateinit var authVM: AuthViewModel
    private val authFactory: AuthViewModelFactory by instance()
    private val PICTURE_REQUEST_CODE= 20
    private var pdfUri: Uri?=null
    override fun getLayout()=R.layout.fragment_put_comerciantes

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = requireActivity().run {
            ViewModelProvider(this,factory).get(GlobalViewModel::class.java)
        }
        authVM = requireActivity().run {
            ViewModelProvider(this,authFactory).get(AuthViewModel::class.java)
        }
        loadExcel.setOnClickListener {
            val mRequestFileIntent = Intent(Intent.ACTION_GET_CONTENT)
            mRequestFileIntent.type = "*/*"
            startActivityForResult(Intent.createChooser(mRequestFileIntent,"Seleccione una app para elegir el pdf"), 20)
        }
        send_excel.setOnClickListener {
            if (pdfUri!=null){
                sendPdfRetrofit()
            }else{
                snakBar("Selecciona un excel por favor")
            }
        }
    }

    private fun sendPdfRetrofit() {
        val dato = File(requireActivity().getPath(pdfUri!!))
        val name=requireContext().contentResolver.getType(pdfUri!!)
        viewModel.uploadExcel(dato,"pdf",name!!).observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    snakBarDefinitivo("Subiendo")
                }
                is Resource.Success->{
                    snakBarDefinitivo("Se subio correctamente")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                20-> try{
                    pdfUri= data!!.data
                    lbl_name_excel.text = "PDF: ${pdfUri!!.path}"
                }catch (e:Exception){
                    Log.e("errorPdf", e.message!!)
                }
            }
        }

    }
    //
}