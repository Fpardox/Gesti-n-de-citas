package com.example.miprimeraapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var db: AppDatabase
    private lateinit var clientDao: ClientDao

    @Before
    fun createDb() {
        // Se crea una base de datos temporal en la memoria solo para esta prueba
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        clientDao = db.clientDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndReadClient() = runBlocking {
        // 1. PREPARAR: Creamos un cliente de prueba
        val client = Client(id = 1, name = "Juan Perez", phone = "3001112233", email = "juan@perez.com")

        // 2. ACTUAR: Insertamos el cliente en la base de datos
        clientDao.insert(client)

        // 3. VERIFICAR: Leemos todos los clientes y comprobamos que el que guardamos está ahí
        val clients = clientDao.getAllClients("%Juan Perez%")
        assertEquals(clients[0].name, client.name)
    }
}