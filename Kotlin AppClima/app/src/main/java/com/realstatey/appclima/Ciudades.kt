package com.realstatey.appclima

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class Ciudades : AppCompatActivity() {


    val TAG ="com.realstatey.appclima.ciudades.CIUDAD"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ciudades)

            val bMExico=findViewById<Button>(R.id.butMX)
            val bMBErlin=findViewById<Button>(R.id.butGR)

        bMExico.setOnClickListener(View.OnClickListener {
            val intent =Intent(this,MainActivity::class.java)
            intent.putExtra(TAG,"3530597")
            startActivity(intent)
        })

        bMBErlin.setOnClickListener(View.OnClickListener {
            val intent =Intent(this,MainActivity::class.java)
            intent.putExtra(TAG,"2950159")
            startActivity(intent)
        })
    }
}
