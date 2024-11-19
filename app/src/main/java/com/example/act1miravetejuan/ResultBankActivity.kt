package com.example.act1miravetejuan

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResultBankActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result_bank)
        val mainLayout = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recoger los datos enviados desde MainActivity
        val salarioBruto = intent.getDoubleExtra("salarioBruto", 0.0)
        val retencionIRPF = intent.getDoubleExtra("retencionIRPF", 0.0)
        val salarioNeto = intent.getDoubleExtra("salarioNeto", 0.0)

        // Referencias a los TextViews en el layout
        val salarioBrutoTV = findViewById<TextView>(R.id.salarioBrutoTextView)
        val retencionIRPFTV = findViewById<TextView>(R.id.retencionIRPFTextView)
        val salarioNetoTV = findViewById<TextView>(R.id.salarioNetoTextView)

        // Mostrar los resultados en los TextViews
        salarioBrutoTV.text = String.format("Salario Bruto: %.2f €", salarioBruto)
        retencionIRPFTV.text = String.format("Retención IRPF: %.2f%%", retencionIRPF * 100) // Convertir a porcentaje
        salarioNetoTV.text = String.format("Salario Neto: %.2f €", salarioNeto)
    }
}
