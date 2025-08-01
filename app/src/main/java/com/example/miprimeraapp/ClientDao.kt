package com.example.miprimeraapp

import androidx.room.*

@Dao
interface ClientDao {
    @Insert
    suspend fun insert(client: Client)

    // CAMBIO: Ahora la consulta puede filtrar por un texto
    @Query("SELECT * FROM clients_table WHERE client_name LIKE :searchQuery ORDER BY client_name ASC")
    suspend fun getAllClients(searchQuery: String): List<Client>

    @Delete
    suspend fun delete(client: Client)

    @Update
    suspend fun update(client: Client)

    @Query("SELECT * FROM clients_table WHERE id = :clientId")
    suspend fun getClientById(clientId: Int): Client?
}