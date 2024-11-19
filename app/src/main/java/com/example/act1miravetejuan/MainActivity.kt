package com.example.act1miravetejuan

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.loginButton).setOnClickListener {
            val salario = findViewById<EditText>(R.id.TextNumberSalario)
            val pagas = findViewById<EditText>(R.id.textNumberPagas2)
            val edad = findViewById<EditText>(R.id.textNumberEdad)
            val grupoProfesional = findViewById<EditText>(R.id.editTextGrupoProfesional)
            val discapacidad = findViewById<EditText>(R.id.editDiscapacidad)
            val estado = findViewById<EditText>(R.id.estadoCivil)
            val hijos = findViewById<EditText>(R.id.textHijo)

            if (salario.text.isNotEmpty() && pagas.text.isNotEmpty() && edad.text.isNotEmpty() &&
                grupoProfesional.text.isNotEmpty() && discapacidad.text.isNotEmpty() && estado.text.isNotEmpty()
                && hijos.text.isNotEmpty()) {

                val salarioBruto = salario.text.toString().toDouble()
                val numeroPagas = pagas.text.toString().toInt()
                val edadUsuario = edad.text.toString().toInt()
                val grupoProfesionalText = grupoProfesional.text.toString()
                val discapacidadPorcentaje = discapacidad.text.toString().toDouble()
                val estadoCivil = estado.text.toString()
                val numHijos = hijos.text.toString().toInt()

                val retencionBase = when {
                    salarioBruto > 60000 -> 0.45
                    salarioBruto > 30000 -> 0.37
                    salarioBruto > 12000 -> 0.20
                    else -> 0.10
                }

                val numPagas = pagas.text.toString().toInt()
                val salarioBrutoAjustado =
                    if (numPagas == 14) {
                    salarioBruto * 14
                } else {
                    salarioBruto * 12
                }

                val reduccionTotal = (numHijos * 0.02) +
                        (if (discapacidadPorcentaje > 0) 0.05 else 0.0) +
                        (if (edadUsuario >= 65) 0.03 else 0.0) +
                        (if (estadoCivil.equals("casado", ignoreCase = true)) 0.02 else 0.0)

                val retencionIRPFAdjusted = maxOf(retencionBase - reduccionTotal, 0.0)
                val salarioNetoAjustado = salarioBrutoAjustado * (1 - retencionIRPFAdjusted)

                val intent = Intent(this, ResultBankActivity::class.java).apply {
                    putExtra("salarioBruto", salarioBrutoAjustado)
                    putExtra("retencionIRPF", retencionIRPFAdjusted)
                    putExtra("salarioNeto", salarioNetoAjustado)
                }

                startActivity(intent)

            } else {
                salario.setTextColor(Color.RED)
                salario.setText("Debes indicar el salario")

                pagas.setTextColor(Color.RED)
                pagas.setText("Debes rellenar cuantas pagas te dan")

                edad.setTextColor(Color.RED)
                edad.setText("Debes rellenar indicar tu edad")

                grupoProfesional.setTextColor(Color.RED)
                grupoProfesional.setText("Debes rellenar el grupo social al que perteneces")

                discapacidad.setTextColor(Color.RED)
                discapacidad.setText("Debes rellenar el grado de discapacidad")

                estado.setTextColor(Color.RED)
                estado.setText("Debes rellenar tu estado civil")

                hijos.setTextColor(Color.RED)
                hijos.setText("Debes indicar el número de hijos")
            }
        }
    }
}
