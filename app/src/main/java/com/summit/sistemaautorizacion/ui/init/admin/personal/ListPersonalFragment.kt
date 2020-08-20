package com.summit.sistemaautorizacion.ui.init.admin.personal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.summit.sistemaautorizacion.R
import com.summit.sistemaautorizacion.base.BaseFragment
import com.summit.sistemaautorizacion.common.conexion.Resource
import com.summit.sistemaautorizacion.data.model.PersonalList
import com.summit.sistemaautorizacion.ui.auth.AuthViewModel
import com.summit.sistemaautorizacion.ui.auth.AuthViewModelFactory
import com.summit.sistemaautorizacion.ui.init.GlobalViewModel
import com.summit.sistemaautorizacion.ui.init.GlobalViewModelFactory
import kotlinx.android.synthetic.main.fragment_list_personal.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ListPersonalFragment : BaseFragment(),KodeinAware,PersonalAdapter.clickPersonal {



    override val kodein: Kodein by kodein()
    lateinit var viewModel: GlobalViewModel
    private val factory: GlobalViewModelFactory by instance()
    lateinit var authVM: AuthViewModel
    private val authFactory: AuthViewModelFactory by instance()
    lateinit var adaptador:PersonalAdapter
    override fun getLayout()=R.layout.fragment_list_personal

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = requireActivity().run {
            ViewModelProvider(this,factory).get(GlobalViewModel::class.java)
        }
        authVM = requireActivity().run {
            ViewModelProvider(this,authFactory).get(AuthViewModel::class.java)
        }

        adaptador= PersonalAdapter(this)
        lbl_rv_personal.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adaptador
        }
        getListPost()

        lbl_add_personal.setOnClickListener {
            findNavController().navigate(ListPersonalFragmentDirections.actionListPersonalFragmentToRegisterPersonalFragment())
        }
    }
    private fun getListPost() {
        viewModel.loadListPersonal().observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                  //  snakBar("Cargando")
                }
                is Resource.Success->{
                    if(it.data!=null){
                        Log.e("datos",it.toString())
                        adaptador.updateData(it.data as MutableList<PersonalList>)

                    }
                }
                is Resource.Failure->{
                    snakBar("Errot")
                }
            }

        })


    }
    override fun listenerClick(postList: PersonalList, position: Int) {
        findNavController().navigate(ListPersonalFragmentDirections.actionListPersonalFragmentToUpdatePersonalFragment(postList._id,postList))
    }



    override fun deleteclick(postList: PersonalList, position: Int) {
        deleteItem(postList._id,position)
    }
    private fun deleteItem(id:String,position: Int){
        viewModel.deletePersonal(id).observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    snakBar("borrando")
                }
                is Resource.Success->{
                    snakBar("Se borro correctamente")
                    adaptador.remove(position)
                }
                is Resource.Failure->{
                    snakBar(it.exception.message!!)
                }
            }

        })
    }
}