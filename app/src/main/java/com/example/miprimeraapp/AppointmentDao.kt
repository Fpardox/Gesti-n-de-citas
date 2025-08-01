package com.example.miprimeraapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AppointmentDao {
    @Insert
    suspend fun insert(appointment: Appointment)

    @Query("SELECT * FROM appointments_table WHERE client_owner_id = :clientId ORDER BY date ASC, time ASC")
    suspend fun getAppointmentsForClient(clientId: Int): List<Appointment>

    // --- FUNCIÓN NUEVA ---
    @Query("""
        SELECT date, time, description, clients_table.client_name as clientName
        FROM appointments_table
        INNER JOIN clients_table ON appointments_table.client_owner_id = clients_table.id
        ORDER BY date ASC, time ASC
    """)
    suspend fun getUpcomingAppointments(): List<AppointmentWithClientInfo>
}