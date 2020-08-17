package com.summit.sistemaautorizacion.ui.init.admin.comerciante

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.summit.sistemaautorizacion.R
import com.summit.sistemaautorizacion.data.model.ComercianteList

class ComercianteAdapter(private val listener: postClick): RecyclerView.Adapter<ComercianteAdapter.ViewHolder>() {

    var precios:MutableList<ComercianteList> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_list_comerciantes_item,parent,false))
    override fun getItemCount() = if (precios!=null) precios!!.size else 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(precios?.get(position)!!,position)
    }

    fun updateData(data: MutableList<ComercianteList>){
        precios.clear()
        Log.e("adapter",data.toString())
        precios =data
        notifyDataSetChanged()
    }
    fun remove(position: Int){
        precios.removeAt(position)
        notifyDataSetChanged()
    }
    fun refressh()= notifyDataSetChanged()
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val name = itemView.findViewById<TextView>(R.id.lbl_list_name_comerciantes)
        val phone = itemView.findViewById<TextView>(R.id.lbl_list_phone_comerciantes)
        val asociacion = itemView.findViewById<TextView>(R.id.lbl_list_asociacion_comerciantes)
        val delete = itemView.findViewById<ImageView>(R.id.lbl_list_delete_comerciantes)
        val vista = itemView.findViewById<ImageView>(R.id.lbl_list_view_comerciantes)
        fun bind(get: ComercianteList, position: Int) {

            name.text= get.name
            phone.text= get.phone
            asociacion.text= get.asociacion

            itemView.setOnClickListener {
                listener.listenerClick(get,position)
            }
            delete.setOnClickListener {
                val alertOpciones =
                    AlertDialog.Builder(itemView.context)
                alertOpciones.setTitle("¿Desea eliminar este comerciante?:")
                alertOpciones.setPositiveButton("Borrar") { dialogInterface, i ->
                    listener.deleteclick(get,position)
                }
                alertOpciones.setNegativeButton("Ahora no") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                alertOpciones.show()
            }
            vista.setOnClickListener {
                listener.viewClick(get)
            }
        }
    }
    interface postClick{
        fun listenerClick(postList: ComercianteList,position: Int)
        fun viewClick(postList: ComercianteList)
        fun deleteclick(
            postList: ComercianteList,
            position: Int
        )
    }

}