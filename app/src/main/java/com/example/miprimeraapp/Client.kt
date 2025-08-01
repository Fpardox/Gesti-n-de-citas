package com.example.miprimeraapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients_table")
data class Client(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "client_name")
    val name: String,

    @ColumnInfo(name = "client_phone")
    val phone: String,

    @ColumnInfo(name = "client_email")
    val email: String? // El email puede ser opcional
)