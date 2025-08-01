package com.example.miprimeraapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ClientDetailActivity : AppCompatActivity() {

    private lateinit var clientDao: ClientDao
    private lateinit var appointmentDao: AppointmentDao
    private var clientId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_detail)
        title = "Detalle del Cliente"

        val database = AppDatabase.getDatabase(application)
        clientDao = database.clientDao()
        appointmentDao = database.appointmentDao()

        clientId = intent.getIntExtra("CLIENT_ID", -1)

        // --- CÓDIGO NUEVO PARA EL BOTÓN DE EDITAR ---
        val editButton = findViewById<ImageButton>(R.id.button_edit_client)
        editButton.setOnClickListener {
            val intent = Intent(this, EditClientActivity::class.java)
            intent.putExtra("CLIENT_ID", clientId)
            startActivity(intent)
        }
        // --- FIN DEL CÓDIGO NUEVO ---
    }

    // Se necesita onResume para refrescar los datos si se editan
    override fun onResume() {
        super.onResume()
        loadClientDetails()
        loadAppointments()
    }

    private fun loadClientDetails() {
        lifecycleScope.launch {
            val client = clientDao.getClientById(clientId)
            client?.let {
                findViewById<TextView>(R.id.detail_client_name).text = it.name
                findViewById<TextView>(R.id.detail_client_phone).text = it.phone
                findViewById<TextView>(R.id.detail_client_email).text = it.email
            }
        }
    }

    private fun loadAppointments() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_appointments)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            val appointments = appointmentDao.getAppointmentsForClient(clientId)
            recyclerView.adapter = AppointmentAdapter(appointments)
        }
    }
}