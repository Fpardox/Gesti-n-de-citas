package com.example.miprimeraapp

// Esta clase no es una tabla, solo un contenedor para el resultado de la consulta
data class AppointmentWithClientInfo(
    val date: String,
    val time: String,
    val description: String,
    val clientName: String
)