package com.realstatey.ubicacion_kotlin

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.tasks.OnSuccessListener

class MainActivity : AppCompatActivity() {

//VARIABLES PARA LA UBICACION
    private val permisoFineLocation=android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permisoCoarseLocation=android.Manifest.permission.ACCESS_FINE_LOCATION
    private val CODIGO_SOLICITUD_PERMISO=100 //Codigo de permiso
    var fusedLocationClient: FusedLocationProviderClient?=null//permiso para pedir la Latitud
    var locationRequest:LocationRequest?=null
    var callback:LocationCallback?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient= FusedLocationProviderClient(this)
        inicializarLocationRequest()

        callback=object :LocationCallback(){

            override fun onLocationResult(locationResoult: LocationResult?) {
                super.onLocationResult(locationResoult)//location resoult entregar un objeto que tiene un arreglo de las ubcicaciones mas recientes

                for (ubicacion in locationResoult?.locations!!){
                    Toast.makeText(applicationContext,ubicacion.latitude.toString()+","+ubicacion.longitude.toString(),Toast.LENGTH_LONG).show()
                }
            }
        }
    }


//Se tiene que configurtar los parametros de actualizacion
    private fun inicializarLocationRequest(){
        locationRequest= LocationRequest()
        locationRequest?.interval=1000//intervalo de actualizacion
        locationRequest?.fastestInterval=5000//rango maximo de tiempo
        locationRequest?.priority=LocationRequest.PRIORITY_HIGH_ACCURACY//10m de variacion
    }
    private fun validarPermisosUbuicacion(): Boolean {
        val hayUbicacionPrecisa=ActivityCompat.checkSelfPermission(this,permisoFineLocation)==PackageManager.PERMISSION_GRANTED//checkSelfPermission es para
        //verificar servicios entre si
        val hayubicacionOrdinaria=ActivityCompat.checkSelfPermission(this,permisoCoarseLocation)==PackageManager.PERMISSION_GRANTED
        //Se verifican los permisos de la app
        return hayUbicacionPrecisa && hayubicacionOrdinaria
    }

    private fun obtenerUbicacion() {
/*
        fusedLocationClient?.lastLocation?.addOnSuccessListener(this,object:OnSuccessListener<Location>{
            override fun onSuccess(location: Location?) {
        if(location!=null){
            Toast.makeText(applicationContext,location?.latitude.toString()+"_"+location?.longitude.toShort(),Toast.LENGTH_SHORT).show()
        }
            }

        })*/
        fusedLocationClient?.requestLocationUpdates(locationRequest,callback,null)
    }
    private fun pedirPermisos() {
        //shouldShowRequestPermissionRationale si el uduario nego los permisos, se le da un contexto a el usuario porque es enecesario
        //el permiso
        val deboProveerContexto=ActivityCompat.shouldShowRequestPermissionRationale(this,permisoFineLocation)
        if(deboProveerContexto){
            solicitudPermiso()
            }
        else{
            solicitudPermiso()
        }
    }
    private fun solicitudPermiso() {
        // Con esto se mapea mi codigo con los servicios
        requestPermissions(arrayOf(permisoFineLocation,permisoCoarseLocation),CODIGO_SOLICITUD_PERMISO)
    }
    private fun detenerActualizaciondeUbicacion() {
        fusedLocationClient?.removeLocationUpdates(callback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CODIGO_SOLICITUD_PERMISO->{
                if(grantResults.size>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //obtenerUbicación
                        obtenerUbicacion()
                }else{
                    Toast.makeText(this,"No diste permiso para acceder",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()

        if (validarPermisosUbuicacion()){
            obtenerUbicacion()
        }
        else{
            pedirPermisos()
        }
    }
    override fun onPause() {
        super.onPause()
        detenerActualizaciondeUbicacion()
    }




}
