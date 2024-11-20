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

    // Declaración de variables privadas para los elementos visuales
    private lateinit var salario: EditText
    private lateinit var pagas: EditText
    private lateinit var edad: EditText
    private lateinit var grupoProfesional: EditText
    private lateinit var discapacidad: EditText
    private lateinit var estado: EditText
    private lateinit var hijos: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initListeners()
    }

    private fun initComponents() {
        // Inicialización de componentes visuales
        salario = findViewById(R.id.TextNumberSalario)
        pagas = findViewById(R.id.textNumberPagas2)
        edad = findViewById(R.id.textNumberEdad)
        grupoProfesional = findViewById(R.id.editTextGrupoProfesional)
        discapacidad = findViewById(R.id.editDiscapacidad)
        estado = findViewById(R.id.estadoCivil)
        hijos = findViewById(R.id.textHijo)
    }

    private fun initListeners() {
        // Configuración de eventos
        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.loginButton).setOnClickListener {
            if (validateInputs()) {
                calculateAndNavigate()
            } else {
                displayErrors()
            }
        }
    }

    private fun validateInputs(): Boolean {
        // Validación de campos
        return salario.text.isNotEmpty() && pagas.text.isNotEmpty() && edad.text.isNotEmpty() &&
                grupoProfesional.text.isNotEmpty() && discapacidad.text.isNotEmpty() &&
                estado.text.isNotEmpty() && hijos.text.isNotEmpty()
    }

    private fun calculateAndNavigate() {
        // Recogida de datos
        val salarioBruto = salario.text.toString().toDouble()
        val numeroPagas = pagas.text.toString().toInt()
        val edadUsuario = edad.text.toString().toInt()
        val grupoProfesionalText = grupoProfesional.text.toString()
        val discapacidadPorcentaje = discapacidad.text.toString().toDouble()
        val estadoCivil = estado.text.toString()
        val numHijos = hijos.text.toString().toInt()

        // Cálculo de retenciones
        val retencionBase = when {
            salarioBruto > 60000 -> 0.45
            salarioBruto > 30000 -> 0.37
            salarioBruto > 12000 -> 0.20
            else -> 0.10
        }

        val salarioBrutoAjustado = salarioBruto * if (numeroPagas == 14) 14 else 12

        val reduccionTotal = (numHijos * 0.02) +
                (if (discapacidadPorcentaje > 0) 0.05 else 0.0) +
                (if (edadUsuario >= 65) 0.03 else 0.0) +
                (if (estadoCivil.equals("casado", ignoreCase = true)) 0.02 else 0.0)

        val retencionIRPFAdjusted = maxOf(retencionBase - reduccionTotal, 0.0)
        val salarioNetoAjustado = salarioBrutoAjustado * (1 - retencionIRPFAdjusted)

        // Llamada al método de navegación
        navigateToResult(salarioBrutoAjustado, retencionIRPFAdjusted, salarioNetoAjustado)
    }

    private fun navigateToResult(salarioBruto: Double, retencionIRPF: Double, salarioNeto: Double) {
        // Creamos el objeto intent
        val intent = Intent(this, ResultBankActivity::class.java)

        // Añadimos los extras para pasar los valores
        intent.putExtra("SALARIO_BRUTO_KEY", salarioBruto)
        intent.putExtra("RETENCION_IRPF_KEY", retencionIRPF)
        intent.putExtra("SALARIO_NETO_KEY", salarioNeto)

        // Iniciamos la actividad
        this.startActivity(intent)
    }

    private fun displayErrors() {
        // Mostrar errores en los campos vacíos
        salario.setErrorIfEmpty("Debes indicar el salario")
        pagas.setErrorIfEmpty("Debes rellenar cuántas pagas te dan")
        edad.setErrorIfEmpty("Debes indicar tu edad")
        grupoProfesional.setErrorIfEmpty("Debes rellenar el grupo profesional")
        discapacidad.setErrorIfEmpty("Debes indicar el grado de discapacidad")
        estado.setErrorIfEmpty("Debes rellenar tu estado civil")
        hijos.setErrorIfEmpty("Debes indicar el número de hijos")
    }

    // Extensión para simplificar la configuración de errores
    private fun EditText.setErrorIfEmpty(message: String) {
        if (this.text.isEmpty()) {
            this.setTextColor(Color.RED)
            this.setText(message)
        }
    }
}
