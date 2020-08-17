package com.summit.sistemaautorizacion.ui.init.admin.comerciante

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.summit.sistemaautorizacion.R
import com.summit.sistemaautorizacion.base.BaseFragment
import com.summit.sistemaautorizacion.common.Constants.BASE_URL_AMAZON_IMG
import com.summit.sistemaautorizacion.common.conexion.Resource
import com.summit.sistemaautorizacion.data.model.ComercianteList
import com.summit.sistemaautorizacion.ui.auth.AuthViewModel
import com.summit.sistemaautorizacion.ui.auth.AuthViewModelFactory
import com.summit.sistemaautorizacion.ui.init.GlobalViewModel
import com.summit.sistemaautorizacion.ui.init.GlobalViewModelFactory
import kotlinx.android.synthetic.main.fragment_comerciante_detail.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ComercianteDetailFragment : BaseFragment(),KodeinAware {
    override val kodein: Kodein by kodein()
    lateinit var viewModel:GlobalViewModel
    private val factory:GlobalViewModelFactory by instance()
    lateinit var authVM:AuthViewModel
    private val authFactory:AuthViewModelFactory by instance()


    lateinit var id:String
    private var enlaceUrl:String?=null
    override fun getLayout()= R.layout.fragment_comerciante_detail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        id = ComercianteDetailFragmentArgs.fromBundle(requireArguments()).identificador
        viewModel = requireActivity().run {
            ViewModelProvider(this,factory).get(GlobalViewModel::class.java)
        }
        authVM = requireActivity().run {
            ViewModelProvider(this,authFactory).get(AuthViewModel::class.java)
        }
        lbl_comerciante_share.setOnClickListener {
            if (enlaceUrl!=null){
                shareTextToapps()
            }else{
                snakBar("Generando pdf")
            }
        }
        lbl_comerciante_copy.setOnClickListener {
            if (enlaceUrl!=null){
                copyToClipBoard(enlaceUrl!!)
            }else{
                snakBar("Generando pdf")
            }
        }
        lbl_comerciante_download.setOnClickListener {
            if (enlaceUrl!=null){
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL_AMAZON_IMG+enlaceUrl))
                startActivity(browserIntent)
            }else{
                snakBar("Generando pdf")
            }
        }
        getPdfRead()
    }

    private fun getPdfRead() {
        viewModel.getPdfQr(id).observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    snakBar("Cargando")
                }
                is Resource.Success->{
                    enlaceUrl= it.data.message
                   cargarPdfOffline()
                }
                is Resource.Failure->{
                    snakBar("Errot")
                }
            }
        })
    }

    private fun shareTextToapps() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, enlaceUrl!!);
        startActivity(Intent.createChooser(shareIntent,"Compartir enlace con: "))
    }
    private fun cargarPdfOffline() {
        lbl_comerciante_pdf.settings.javaScriptEnabled = true
        lbl_comerciante_pdf.settings.allowFileAccess = true
        lbl_comerciante_pdf.loadUrl(BASE_URL_AMAZON_IMG+enlaceUrl)
    }
    private fun copyToClipBoard( message: String) {

        val clipBoard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("label",message)
        clipBoard.setPrimaryClip(clipData)
        snakBar("Copiado correctamente")
    }
}