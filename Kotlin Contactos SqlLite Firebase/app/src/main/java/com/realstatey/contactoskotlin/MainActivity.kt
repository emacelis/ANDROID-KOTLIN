package com.realstatey.contactoskotlin

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.GridView
import android.widget.ListView
import android.widget.Switch
import android.widget.ViewSwitcher
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar


class MainActivity : AppCompatActivity() {

    var lista:ListView?=null
    var grid:GridView?=null

    var viewSwitcher:ViewSwitcher?=null//cambiar de vista
//Con companion object kotlin identidficara como objeto estatico
companion object{

    var contactos:ArrayList<Contacto>?=null
    var adaptador:adaptador?=null//el adaptado va a funguir como el viculo con la vista
    var adaptadorGrid:adaptador_custom_grid?=null//el adaptado va a funguir como el viculo con la vista


    fun agregar_contacto(contacto: Contacto){
        adaptador?.addItemms(contacto)
    }
    fun obtenerContacto(index :Int):Contacto{
        return adaptador?.getItem(index) as Contacto
    }
    fun eliminarContacto(index: Int){
        adaptador?.removeItemms(index)
    }

    fun actualizarContacto(index:Int,nuevoContacto: Contacto){
        adaptador?.updateItem(index,nuevoContacto)
    }
}



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Activar FELECHA de regreso
        val toolbar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //
        contactos= ArrayList()
        contactos?.add(Contacto("Marcos"," Rivas","Contoso",25,70.0F,"Hidalgo","75570367","marco@pontos.com",R.drawable.foto_01))
        contactos?.add(Contacto("Fernando"," Rivas","Contoso",25,70.0F,"Hidalgo","75570367","marco@pontos.com",R.drawable.foto_02))
        contactos?.add(Contacto("Maria"," Rivas","Contoso",25,70.0F,"Hidalgo","75570367","marco@pontos.com",R.drawable.foto_04))



         lista=findViewById(R.id.lista)
         grid=findViewById<GridView>(R.id.grid)
         adaptador=adaptador(this,contactos!!)
         adaptadorGrid = adaptador_custom_grid(this, contactos!!)
         viewSwitcher=findViewById(R.id.viewSwitcher)//cambio de vista


        lista?.adapter=adaptador
        grid?.adapter= adaptadorGrid
//MAPEO DEL INDEX DE EL ELEMENTO CONTACTO EN EL LIST
        lista?.setOnItemClickListener{parent,view,position,id ->
            val intent=Intent(this,Detalle::class.java)
            intent.putExtra("ID",position.toString())
            startActivity(intent)
        }
    }

//Interaccion con los iconos TOOLBAR SUPERIOR
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)

        val searchManager=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val itemBusqueda=menu?.findItem(R.id.serachview)
        val searchView=itemBusqueda?.actionView as SearchView

        val itemSitch=menu?.findItem(R.id.switch_view)
        itemSitch?.setActionView(R.layout.switch_item)
        val switchView=itemSitch?.actionView?.findViewById<Switch>(R.id.sCambiaVista)


        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint="Buscar contacto.."
        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->

        }
searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
    override fun onQueryTextChange(newText: String?): Boolean {
        //Filtrar
          adaptador?.filtrar(newText!!)//el adsptador es el que tomara las actividades en luagr de la lista

        return true
    }
    override fun onQueryTextSubmit(newText: String?): Boolean {

        return true
    }
})
    switchView?.setOnCheckedChangeListener { buttonView, isChecked ->
        viewSwitcher?.showNext()
    }


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.Nicon -> {
                val intent = Intent(this, Nuevo::class.java)
                startActivity(intent)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adaptador?.notifyDataSetChanged()
    }

}
