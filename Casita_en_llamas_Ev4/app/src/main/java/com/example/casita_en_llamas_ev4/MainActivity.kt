package com.example.casita_en_llamas_ev4

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.casita_en_llamas_ev4.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Activar FirebaseDatabase
    private lateinit var database: DatabaseReference

    // Definir el valor máximo del sensor
    private val maxSensorValue: Float = 1023f // Cambia esto a 4095f si usas un ADC de 12 bits

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar la referencia a Firebase
        database = FirebaseDatabase.getInstance().getReference("data/datos/datos")

        // Leer los datos de Firebase
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                // Obtener el valor crudo del sensor desde Firebase
                val datos = snapshot.value.toString()

                // Convertir el valor a flotante, si no es válido, se usa 0f
                val valorSensor = datos.toFloatOrNull() ?: 0f

                // Invertir el cálculo del porcentaje
                val porcentaje = 100 - ((valorSensor / maxSensorValue) * 100)

                // Mostrar el porcentaje invertido en el TextView
                binding.textViewHumedad.text = "Humedad: ${porcentaje.toInt()}%"
            }

            override fun onCancelled(error: DatabaseError) {
                // Si ocurre un error al leer los datos
                binding.textViewHumedad.text = "Error al leer los datos"
            }
        })
    }
}
