package com.realstatey.appclima
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    var tvCiudad:TextView?=null
    var tvGrados:TextView?=null
    var tvClima:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCiudad=findViewById(R.id.txCiudad)
        tvGrados=findViewById(R.id.txGrados)
        tvClima=findViewById(R.id.txClima)

        val ciudad = intent.getStringExtra("com.realstatey.appclima.ciudades.CIUDAD")
        if(Network.HayRed(this)){
            //ejecutar solicitud http
            solicitudHTTPVolley("http://api.openweathermap.org/data/2.5/weather?id="+ciudad+"&appid=431e58fcd4a9b35d782b91478df09ea5&units=metric&lang=es")
            //431e58fcd4a9b35d782b91478df09ea5
            //mx 3530597 Gr 2950159
        }else {
            Toast.makeText(this,"NO hay RED",Toast.LENGTH_LONG).show();
        }
    }

    //METODO PARA VOLLEY
    private fun solicitudHTTPVolley(url:String){
        val queue= Volley.newRequestQueue(this)
        val solicitu= StringRequest(Request.Method.GET,url, Response.Listener<String>{
                response ->
            try {
                val gson= Gson()
                val ciudad=gson.fromJson(response,Ciudad::class.java)
                tvCiudad?.text=ciudad.name
                tvGrados?.text=ciudad.main?.temp.toString()+"°"
                tvClima?.text=ciudad.weather?.get(0)?.description

            }catch (e: Exception){

            }
        }, Response.ErrorListener {  })
        queue.add(solicitu)

    }
}
