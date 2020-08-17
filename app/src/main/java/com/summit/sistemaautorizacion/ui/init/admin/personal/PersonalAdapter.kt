package com.summit.sistemaautorizacion.ui.init.admin.personal

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.summit.sistemaautorizacion.R
import com.summit.sistemaautorizacion.data.model.PersonalList

class PersonalAdapter(private val listener: clickPersonal): RecyclerView.Adapter<PersonalAdapter.ViewHolder>() {

    var precios:MutableList<PersonalList> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_list_personal_item,parent,false))
    override fun getItemCount() = if (precios!=null) precios!!.size else 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(precios?.get(position)!!,position)
    }

    fun updateData(data: MutableList<PersonalList>){
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

        val name = itemView.findViewById<TextView>(R.id.lbl_list_name_personal)
        val phone = itemView.findViewById<TextView>(R.id.lbl_list_phone_personal)
        val asociacion = itemView.findViewById<TextView>(R.id.lbl_list_role_personal)
        val delete = itemView.findViewById<ImageView>(R.id.lbl_list_delete_personal)
        fun bind(get: PersonalList, position: Int) {

            name.text= get.name
            phone.text= get.phone
            asociacion.text= get.role

            itemView.setOnClickListener {
                listener.listenerClick(get,position)
            }
            delete.setOnClickListener {
                val alertOpciones =
                    AlertDialog.Builder(itemView.context)
                alertOpciones.setTitle("¿Desea eliminar este personal?:")
                alertOpciones.setPositiveButton("Borrar") { dialogInterface, i ->
                    listener.deleteclick(get,position)
                }
                alertOpciones.setNegativeButton("Ahora no") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                alertOpciones.show()
            }

        }
    }
    interface clickPersonal{
        fun listenerClick(postList: PersonalList,position: Int)
        fun deleteclick(
            postList: PersonalList,
            position: Int
        )
    }

}