package com.example.miprimeraapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ClientListActivity : AppCompatActivity() {

    private lateinit var clientDao: ClientDao
    private lateinit var recyclerView: RecyclerView
    private var searchView: SearchView? = null // La hacemos nullable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_list)
        title = "Lista de Clientes"

        clientDao = AppDatabase.getDatabase(application).clientDao()
        recyclerView = findViewById(R.id.recycler_view_clients)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        // Usamos la consulta actual de la barra de búsqueda, si existe
        loadClients(searchView?.query?.toString())
    }

    // --- MÉTODO NUEVO PARA CREAR EL MENÚ ---
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as? SearchView

        searchView?.isIconified = false
        searchView?.queryHint = "Buscar cliente por nombre..."

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadClients(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadClients(newText)
                return true
            }
        })
        return true
    }

    private fun loadClients(query: String?) {
        val searchQuery = "%${query ?: ""}%"
        lifecycleScope.launch {
            val clientList = clientDao.getAllClients(searchQuery)
            recyclerView.adapter = ClientAdapter(
                clients = clientList,
                onItemClicked = { client ->
                    val intent = Intent(this@ClientListActivity, ClientDetailActivity::class.java)
                    intent.putExtra("CLIENT_ID", client.id)
                    startActivity(intent)
                },
                onDeleteClicked = { client ->
                    deleteClient(client)
                },
                onAddAppointmentClicked = { client ->
                    val intent = Intent(this@ClientListActivity, AddAppointmentActivity::class.java)
                    intent.putExtra("CLIENT_ID", client.id)
                    startActivity(intent)
                }
            )
        }
    }

    private fun deleteClient(client: Client) {
        lifecycleScope.launch {
            clientDao.delete(client)
            Toast.makeText(this@ClientListActivity, "Cliente eliminado", Toast.LENGTH_SHORT).show()
            loadClients(searchView?.query?.toString())
        }
    }
}