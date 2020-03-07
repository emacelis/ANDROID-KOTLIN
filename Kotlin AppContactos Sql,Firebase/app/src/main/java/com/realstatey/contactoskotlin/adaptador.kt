package com.realstatey.contactoskotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList as ArrayList1

class adaptador(var contexto: Context,items:ArrayList<Contacto>):BaseAdapter() {
    //Alamacenar los elementos que se van a mostrar en el list View

    var items:ArrayList<Contacto>?=null
    //NEcesito crear una copia para no afectar la
    // información de los elementos guardados anteriormente
    var copiaItems:ArrayList<Contacto>?=null


//NEcesito crear una copia
    init {
        this.items=ArrayList(items)//DEEP COOPY O COPIA EXACTA PARA SER MODIFICADA
        this.copiaItems=items

    }
//ASOCIAR EL RENDERIMIENTO DE LOS ELEMENTOS CON LOS OBJETOS EN LA LISTA
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var viewHolder:ViewHolder?=null
        var vista:View?=convertView
        if(vista==null){
        vista =LayoutInflater.from(contexto).inflate(R.layout.templete_contacto,null)
        viewHolder= ViewHolder(vista)
            vista.tag=viewHolder
        }else{
        viewHolder=vista.tag as? ViewHolder
        }
    //Asignacion de VALORES A ELEMENTOS GRAFICOS
    val item=getItem(position) as Contacto
    viewHolder?.nombre?.text=item.nombre+""+item.apellidos
    viewHolder?.empresa?.text=item.empresas
    viewHolder?.foto?.setImageResource(item.foto)
    return  vista!!

}
    override fun getItem(position: Int): Any {
        return this.items?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
//Regresamos l numero de elementos de la LISTA
    return this.items?.count()!!
 }

    fun addItemms(item:Contacto){
        copiaItems?.add(item)
        items = copiaItems?.let { ArrayList(it) }//Carga la lista de nuevo una vez mostrada la busqueda
        notifyDataSetChanged()
    }
    fun removeItemms(index:Int){
        copiaItems?.removeAt(index)
        items = copiaItems?.let { ArrayList(it) }//Carga la lista de nuevo una vez mostrada la busqueda
        notifyDataSetChanged()
    }
    fun updateItem(index:Int,newItem: Contacto){
        copiaItems?.set(index, newItem)
        items = copiaItems?.let { ArrayList(it) }//Carga la lista de nuevo una vez mostrada la busqueda
        notifyDataSetChanged()
    }

    fun filtrar(str: String){
        items?.clear()
        if(str.isEmpty()){
           items = copiaItems?.let { ArrayList(it) }//Carga la lista de nuevo una vez mostrada la busqueda
            notifyDataSetChanged()
            return
        }
        var  busqueda=str
        busqueda=busqueda.toLowerCase()

        for(item in copiaItems!!){
            val nombre=item.nombre.toLowerCase()//Busqueda solo en minusculas

            if(nombre.contains(busqueda)){
                items?.add(item)
            }
        }
            notifyDataSetChanged()
    }


    //Clase para definir los elementos graficos para la memoria
    private class ViewHolder(vista: View) {
        var nombre:TextView?=null
        var foto:ImageView?=null
        var empresa:TextView?=null
        init {
            nombre=vista.findViewById(R.id.nombre)
            empresa=vista.findViewById(R.id.empresa)
            foto=vista.findViewById(R.id.imagefoto)
        }
    }



}