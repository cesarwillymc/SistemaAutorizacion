package com.summit.sistemaautorizacion.ui.init.admin.comerciante

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.summit.sistemaautorizacion.R
import com.summit.sistemaautorizacion.base.BaseFragment
import com.summit.sistemaautorizacion.common.conexion.Resource
import com.summit.sistemaautorizacion.data.model.ComercianteList
import com.summit.sistemaautorizacion.ui.auth.AuthViewModel
import com.summit.sistemaautorizacion.ui.auth.AuthViewModelFactory
import com.summit.sistemaautorizacion.ui.init.GlobalViewModel
import com.summit.sistemaautorizacion.ui.init.GlobalViewModelFactory
import kotlinx.android.synthetic.main.fragment_list_comerciantes.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ListComerciantesFragment : BaseFragment(),KodeinAware, ComercianteAdapter.postClick{



    override val kodein: Kodein by kodein()
    lateinit var viewModel: GlobalViewModel
    private val factory: GlobalViewModelFactory by instance()
    lateinit var authVM: AuthViewModel
    private val authFactory: AuthViewModelFactory by instance()

    override fun getLayout()=R.layout.fragment_list_comerciantes


    lateinit var adaptador:ComercianteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = requireActivity().run {
            ViewModelProvider(this,factory).get(GlobalViewModel::class.java)
        }
        authVM = requireActivity().run {
            ViewModelProvider(this,authFactory).get(AuthViewModel::class.java)
        }
        adaptador= ComercianteAdapter(this)
        lbl_list_comerciantes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adaptador
        }
        getListPost()

        lbl_create_comerciantes.setOnClickListener {
            findNavController().navigate(ListComerciantesFragmentDirections.actionListComerciantesFragmentToPutComerciantesFragment())
        }
    }
    private fun getListPost() {
        viewModel.loadListComerciante().observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    snakBar("Cargando")
                }
                is Resource.Success->{
                    if(it.data!=null){
                        Log.e("datos",it.toString())
                        adaptador.updateData(it.data as MutableList<ComercianteList>)

                    }
                }
                is Resource.Failure->{
                    snakBar("Errot")
                }
            }

        })


    }
    private fun deleteItem(id:String,position: Int){
        viewModel.deleteComerciante(id).observe(viewLifecycleOwner, Observer {
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
    override fun listenerClick(postList: ComercianteList, position: Int) {
        findNavController().navigate(ListComerciantesFragmentDirections.actionListComerciantesFragmentToUpdateComerciantesFragment(postList._id))
    }

    override fun viewClick(postList: ComercianteList) {
        findNavController().navigate(ListComerciantesFragmentDirections.actionListComerciantesFragmentToComercianteDetailFragment(postList._id))
    }

    override fun deleteclick(postList: ComercianteList, position: Int) {
        deleteItem(postList._id,position)
    }
}