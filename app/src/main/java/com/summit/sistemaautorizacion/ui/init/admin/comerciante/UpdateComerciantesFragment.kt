package com.summit.sistemaautorizacion.ui.init.admin.comerciante

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.summit.sistemaautorizacion.R
import com.summit.sistemaautorizacion.base.BaseFragment
import com.summit.sistemaautorizacion.ui.auth.AuthViewModel
import com.summit.sistemaautorizacion.ui.auth.AuthViewModelFactory
import com.summit.sistemaautorizacion.ui.init.GlobalViewModel
import com.summit.sistemaautorizacion.ui.init.GlobalViewModelFactory
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
    }
}