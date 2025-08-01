package com.example.miprimeraapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class EditClientActivity : AppCompatActivity() {

    private lateinit var clientDao: ClientDao
    private var clientId: Int = -1
    private var currentClient: Client? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_client)
        title = "Editar Cliente"

        clientDao = AppDatabase.getDatabase(application).clientDao()
        clientId = intent.getIntExtra("CLIENT_ID", -1)

        val nameEditText = findViewById<EditText>(R.id.edit_text_name_edit)
        val phoneEditText = findViewById<EditText>(R.id.edit_text_phone_edit)
        val emailEditText = findViewById<EditText>(R.id.edit_text_email_edit)
        val updateButton = findViewById<Button>(R.id.button_update)

        // Buscamos el cliente y llenamos los campos
        lifecycleScope.launch {
            currentClient = clientDao.getClientById(clientId)
            currentClient?.let {
                nameEditText.setText(it.name)
                phoneEditText.setText(it.phone)
                emailEditText.setText(it.email)
            }
        }

        updateButton.setOnClickListener {
            val newName = nameEditText.text.toString()
            val newPhone = phoneEditText.text.toString()
            val newEmail = emailEditText.text.toString()

            if (newName.isBlank() || newPhone.isBlank()) {
                Toast.makeText(this, "Nombre y teléfono son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Creamos el cliente actualizado y lo guardamos
            val updatedClient = Client(id = clientId, name = newName, phone = newPhone, email = newEmail)
            lifecycleScope.launch {
                clientDao.update(updatedClient)
                Toast.makeText(this@EditClientActivity, "Cliente actualizado", Toast.LENGTH_SHORT).show()
                finish() // Cierra la pantalla de edición y vuelve a la lista
            }
        }
    }
}