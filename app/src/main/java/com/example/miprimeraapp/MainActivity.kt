package com.example.miprimeraapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var clientDao: ClientDao
    private lateinit var appointmentDao: AppointmentDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Gestor de Citas"

        val database = AppDatabase.getDatabase(application)
        clientDao = database.clientDao()
        appointmentDao = database.appointmentDao()

        val showFormButton = findViewById<Button>(R.id.button_show_form)
        val formContainer = findViewById<LinearLayout>(R.id.form_container)
        val nameEditText = findViewById<EditText>(R.id.edit_text_name)
        val phoneEditText = findViewById<EditText>(R.id.edit_text_phone)
        val emailEditText = findViewById<EditText>(R.id.edit_text_email)
        val saveButton = findViewById<Button>(R.id.button_save)
        val viewListButton = findViewById<Button>(R.id.button_view_list)

        showFormButton.setOnClickListener {
            if (formContainer.visibility == View.GONE) {
                formContainer.visibility = View.VISIBLE
                showFormButton.text = "Ocultar Formulario"
            } else {
                formContainer.visibility = View.GONE
                showFormButton.text = "Añadir Nuevo Cliente"
            }
        }

        saveButton.setOnClickListener {
            saveClient(
                name = nameEditText.text.toString(),
                phone = phoneEditText.text.toString(),
                email = emailEditText.text.toString()
            )
        }

        viewListButton.setOnClickListener {
            val intent = Intent(this, ClientListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadUpcomingAppointments()
    }

    private fun loadUpcomingAppointments() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_upcoming)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            val appointments = appointmentDao.getUpcomingAppointments()
            recyclerView.adapter = UpcomingAppointmentAdapter(appointments)
        }
    }

    private fun saveClient(name: String, phone: String, email: String) {
        if (name.isBlank() || phone.isBlank()) {
            Toast.makeText(this, "Nombre y teléfono son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }
        val client = Client(name = name, phone = phone, email = email)
        lifecycleScope.launch {
            clientDao.insert(client)
            Toast.makeText(this@MainActivity, "Cliente guardado", Toast.LENGTH_SHORT).show()
            findViewById<EditText>(R.id.edit_text_name).text.clear()
            findViewById<EditText>(R.id.edit_text_phone).text.clear()
            findViewById<EditText>(R.id.edit_text_email).text.clear()
            findViewById<LinearLayout>(R.id.form_container).visibility = View.GONE
            findViewById<Button>(R.id.button_show_form).text = "Añadir Nuevo Cliente"
            // Refrescamos la lista de citas por si el nuevo cliente tiene una
            loadUpcomingAppointments()
        }
    }
}