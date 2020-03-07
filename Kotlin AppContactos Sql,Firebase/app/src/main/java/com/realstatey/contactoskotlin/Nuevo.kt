package com.realstatey.contactoskotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import java.util.ArrayList

class Nuevo : AppCompatActivity() {

    var fotIndez:Int=0
    var index:Int=-1
    val fotos= arrayOf(R.drawable.foto_01,R.drawable.foto_02,R.drawable.foto_03,R.drawable.foto_04,R.drawable.foto_05)
    var foto:ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo)


        //Activar FELECHA de regreso
        val toolbar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        //Boton atras
        var actionBar=supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        foto=findViewById<ImageView>(R.id.fotoperfil)
        foto?.setOnClickListener{
            seleccionarfoto()
        }
        //Reconocer accion entre editar y nuevo
        if(intent.hasExtra("ID")){
            index=intent.getStringExtra("ID").toInt()
            rellenarDatos(index)
        }
    }

    //Interaccion con los iconos TOOLBAR SUPERIOR
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nuevo,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home->{
                finish()
                return true
            }

            R.id.iCrearNuevo -> {
                //SE CREA UN ELELEMENTO DE TIPO CONTACTO

                val nombre=findViewById<EditText>(R.id.tvNombre)
                val apellidos=findViewById<EditText>(R.id.apellido)
                val empresa=findViewById<EditText>(R.id.perfilTrabajo)
                val edad=findViewById<EditText>(R.id.edad)
                val telefono=findViewById<EditText>(R.id.telefono)
                val peso=findViewById<EditText>(R.id.peso)
                val email=findViewById<EditText>(R.id.correo)
                val dirección=findViewById<EditText>(R.id.direccion)
                //validar Campos
                var campos=ArrayList<String>()
                campos.add(nombre.text.toString())
                campos.add(apellidos.text.toString())
                campos.add(empresa.text.toString())
                campos.add(edad.text.toString())
                campos.add(peso.text.toString())
                campos.add(dirección.text.toString())
                campos.add(telefono.text.toString())
                campos.add(email.text.toString())
                campos.add(telefono.text.toString())

                var flago=0
                for(campo in campos){
                    if (campo.isNullOrEmpty()){
                        flago++
                    }
                }
                if (flago>0){
                    Toast.makeText(this,"Rellena todos los campos",Toast.LENGTH_SHORT).show()
                }else {
                    if(index > -1){
                        MainActivity.actualizarContacto(index, Contacto(
                                campos.get(0),
                                campos.get(1),
                                campos.get(2),
                                campos.get(3).toInt(),
                                campos.get(4).toFloat(),
                                campos.get(5),
                                campos.get(6),
                                campos.get(7),
                                obtenerfotos(fotIndez)
                            ))
                    }else{
                        MainActivity.agregar_contacto(
                            Contacto(
                                campos.get(0),
                                campos.get(1),
                                campos.get(2),
                                campos.get(3).toInt(),
                                campos.get(4).toFloat(),
                                campos.get(5),
                                campos.get(6),
                                campos.get(7),
                                obtenerfotos(fotIndez)
                            )
                        )
                    }

                    finish()
                   // Log.d("No ELementos", MainActivity.contactos?.count().toString())
                }
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
    fun seleccionarfoto(){
        val builder=AlertDialog.Builder(this)
        builder.setTitle("Selecciona una Imagen de Perfil")
        //mapear foto
        var adaptadorDialogo=ArrayAdapter<String>(this,android.R.layout.simple_selectable_list_item)
        adaptadorDialogo.add("Foto01")
        adaptadorDialogo.add("Foto02")
        adaptadorDialogo.add("Foto03")
        adaptadorDialogo.add("Foto04")
        adaptadorDialogo.add("Foto05")


        builder.setAdapter(adaptadorDialogo){
            dialog, which ->
            //mapeo
            fotIndez=which
            //Obtienes la foto de tipo mutable con el metodo que creaste apuntando a index
            foto?.setImageResource(obtenerfotos(fotIndez))
        }

        builder.setNegativeButton("Cancelar"){
            dialog, which ->
            dialog.dismiss()

        }
        builder.show()
    }

    fun obtenerfotos(index:Int):Int{
        return fotos.get(index)
    }

    fun rellenarDatos(index:Int){
        val contacto= MainActivity.obtenerContacto(index)

        val tvNombre=findViewById<EditText>(R.id.tvNombre)
        val perfilTrabajo=findViewById<EditText>(R.id.perfilTrabajo)
        val edad=findViewById<EditText>(R.id.edad)
        val peso=findViewById<EditText>(R.id.peso)
        val telefono=findViewById<EditText>(R.id.telefono)
        val correo=findViewById<EditText>(R.id.correo)
        val direccion=findViewById<EditText>(R.id.direccion)
        val apellido=findViewById<EditText>(R.id.apellido)
        val fotoperfil=findViewById<ImageView>(R.id.fotoperfil)

        tvNombre.setText(contacto.nombre,TextView.BufferType.EDITABLE)
        correo.setText(contacto.email,TextView.BufferType.EDITABLE)
        perfilTrabajo.setText(contacto.empresas,TextView.BufferType.EDITABLE)
        edad.setText(contacto.edad.toString()+"años",TextView.BufferType.EDITABLE)
        peso.setText(contacto.peso.toString()+"KG",TextView.BufferType.EDITABLE)
        telefono.setText(contacto.telefono,TextView.BufferType.EDITABLE)
        direccion.setText(contacto.direccion,TextView.BufferType.EDITABLE)
        apellido.setText(contacto.apellidos,TextView.BufferType.EDITABLE)
        fotoperfil.setImageResource(contacto.foto)

        var posision=0
        for (foto in fotos){
            if(contacto.foto==foto){
                var fotoIndex = posision
            }
            posision++
        }
    }
}
