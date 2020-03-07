package com.realstatey.contactoskotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_nuevo.*

//Clase para seleccionar un contacto y ver la info selecciónada
class Detalle : AppCompatActivity() {

    var index:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        val toolbar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        var actionBar=supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        //Mapedo de los elementos, en el index
         index =intent.getStringExtra("ID").toInt()
        //val contacto= MainActivity.contactos?get()
        mapearDatos()

    }

    fun mapearDatos(){
        val contacto= MainActivity.obtenerContacto(index)

        val tvNombre=findViewById<TextView>(R.id.tvNombre)
        val perfilTrabajo=findViewById<TextView>(R.id.perfilTrabajo)
        val edad=findViewById<TextView>(R.id.edad)
        val peso=findViewById<TextView>(R.id.peso)
        val telefono=findViewById<TextView>(R.id.telefono)
        val correo=findViewById<TextView>(R.id.correo)
        val direccion=findViewById<TextView>(R.id.direccion)
        val apellido=findViewById<TextView>(R.id.apellido)
        val fotoperfil=findViewById<ImageView>(R.id.fotoperfil)

        tvNombre.text=contacto.nombre
        correo.text=contacto.email
        perfilTrabajo.text=contacto.empresas
        edad.text=contacto.edad.toString()+"años"
        peso.text=contacto.peso.toString()+"KG"
        telefono.text=contacto.telefono
        direccion.text=contacto.direccion
        apellido.text=contacto.apellidos
        fotoperfil.setImageResource(contacto.foto)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalle,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
         android.R.id.home->{
             finish()
             return true
         }
            R.id.editarCon->{
                val intent =Intent (this,Nuevo::class.java)
                intent.putExtra("ID",index.toString())
                startActivity(intent)
                return true

            }
            R.id.eliminarContacto->{
                MainActivity.eliminarContacto(index)
                finish()
                return true

            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapearDatos()
    }
}
