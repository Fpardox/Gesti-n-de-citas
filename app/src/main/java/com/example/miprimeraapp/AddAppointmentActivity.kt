package com.example.miprimeraapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Calendar

class AddAppointmentActivity : AppCompatActivity() {

    private lateinit var clientDao: ClientDao
    private lateinit var appointmentDao: AppointmentDao
    private var clientId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_appointment)
        title = "Agendar Cita"

        // Obtenemos los DAOs
        val database = AppDatabase.getDatabase(application)
        clientDao = database.clientDao()
        appointmentDao = database.appointmentDao()

        clientId = intent.getIntExtra("CLIENT_ID", -1)

        val clientNameHeader = findViewById<TextView>(R.id.text_view_client_name_header)
        val dateEditText = findViewById<EditText>(R.id.edit_text_date)
        val timeEditText = findViewById<EditText>(R.id.edit_text_time)
        val descriptionEditText = findViewById<EditText>(R.id.edit_text_description)
        val saveButton = findViewById<Button>(R.id.button_save_appointment)

        // Mostramos el nombre del cliente
        lifecycleScope.launch {
            val client = clientDao.getClientById(clientId)
            client?.let {
                clientNameHeader.text = "Agendando cita para:\n${it.name}"
            }
        }

        // --- LÓGICA DEL DATE PICKER ---
        dateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // Se guarda como "2025-07-31" para que se ordene bien
                val formattedDate = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                dateEditText.setText(formattedDate)
            }, year, month, day).show()
        }

        // --- LÓGICA DEL TIME PICKER (MODIFICADA) ---
        timeEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            // El último parámetro 'false' activa el modo 12 horas
            TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                // Lógica para convertir formato 24h a 12h con AM/PM
                val amPm = if (selectedHour < 12) "AM" else "PM"
                val hour12 = when {
                    selectedHour == 0 -> 12 // Medianoche
                    selectedHour > 12 -> selectedHour - 12
                    else -> selectedHour
                }
                val formattedTime = String.format("%d:%02d %s", hour12, selectedMinute, amPm)
                timeEditText.setText(formattedTime)
            }, hour, minute, false).show() // Se cambió a 'false'
        }

        saveButton.setOnClickListener {
            val date = dateEditText.text.toString()
            val time = timeEditText.text.toString()
            val description = descriptionEditText.text.toString()

            if (date.isBlank() || time.isBlank() || description.isBlank()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val appointment = Appointment(client_owner_id = clientId, date = date, time = time, description = description)
            lifecycleScope.launch {
                appointmentDao.insert(appointment)
                Toast.makeText(this@AddAppointmentActivity, "Cita guardada", Toast.LENGTH_SHORT).show()
                finish() // Cierra la pantalla y vuelve a la lista
            }
        }
    }
}