package com.summit.sistemaautorizacion.ui.init.admin.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.summit.sistemaautorizacion.R
import com.summit.sistemaautorizacion.base.BaseFragment
import com.summit.sistemaautorizacion.common.Constants.BASE_URL_AMAZON_IMG
import com.summit.sistemaautorizacion.common.SharedPreferencsManager.Companion.clearAllManagerShared
import com.summit.sistemaautorizacion.data.model.Usuario
import com.summit.sistemaautorizacion.ui.auth.AuthActivity
import com.summit.sistemaautorizacion.ui.auth.AuthViewModel
import com.summit.sistemaautorizacion.ui.auth.AuthViewModelFactory
import com.summit.sistemaautorizacion.ui.init.GlobalViewModel
import com.summit.sistemaautorizacion.ui.init.GlobalViewModelFactory
import kotlinx.android.synthetic.main.fragment_profile.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ProfileFragment : BaseFragment(),KodeinAware {


    override val kodein: Kodein by kodein()
    lateinit var viewModel: GlobalViewModel
    private val factory: GlobalViewModelFactory by instance()
    lateinit var authVM: AuthViewModel
    private val authFactory: AuthViewModelFactory by instance()

    override fun getLayout()=R.layout.fragment_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = requireActivity().run {
            ViewModelProvider(this,factory).get(GlobalViewModel::class.java)
        }
        authVM = requireActivity().run {
            ViewModelProvider(this,authFactory).get(AuthViewModel::class.java)
        }
        authVM.isLoggedUser.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                bindingData(it)
            }else{
                navigateToActivity(Intent(requireContext(),AuthActivity::class.java))
            }
        })
        logout.setOnClickListener {
            clearAllManagerShared()
            authVM.deleteUserDB()
        }
    }

    private fun bindingData(usuario: Usuario) {
        lbl_profile_created.text= "${usuario.createdBy}"
        lbl_profile_dni.text= "${usuario.dni}"
        lbl_profile_name.text= "${usuario.name} ${usuario.lastname}"
        lbl_profile_phone.text= "${usuario.phone}"
        Glide.with(requireContext()).load("$BASE_URL_AMAZON_IMG${usuario.image}").error(R.drawable.user).into(lbl_image_profile)

    }
}