package com.example.miprimeraapp

import android.os.Bundle
import android.widget.TextView // <-- 1. LÍNEA AÑADIDA
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // 1. Buscamos el TextView de nuestra nueva pantalla
        val textViewGreeting: TextView = findViewById(R.id.text_view_greeting)

        // 2. Obtenemos los datos que nos enviaron en la intención
        val userName = intent.getStringExtra("USER_NAME")

        // 3. Mostramos el saludo personalizado
        textViewGreeting.text = "Bienvenido, $userName"
    }
} // <-- 2. SE ELIMINÓ LA LLAVE EXTRA QUE ESTABA AQUÍ