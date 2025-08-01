package com.example.miprimeraapp

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "appointments_table",
    foreignKeys = [ForeignKey(
        entity = Client::class,
        parentColumns = ["id"],
        childColumns = ["client_owner_id"],
        onDelete = ForeignKey.CASCADE // Si se borra un cliente, se borran sus citas
    )]
)
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val client_owner_id: Int, // El ID del cliente al que pertenece la cita
    val date: String,
    val time: String,
    val description: String
)