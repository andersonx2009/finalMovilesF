package com.example.examplenavigationdrawer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegistroActivity : AppCompatActivity() {
    val firebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val botonRegresarIniciarSesion = findViewById<ImageButton>(R.id.ButtonBackToLogin)

        val  buttonIniciarSesion = findViewById<Button>(R.id.buttonIniciarSesion)

        val correoTextBox= findViewById<EditText>(R.id.editTextTextPersonName)
        val passwordTextBox= findViewById<EditText>(R.id.editTextTextPassword2)

        botonRegresarIniciarSesion.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }

        buttonIniciarSesion.setOnClickListener{
            val email = correoTextBox.text.toString()
            val password= passwordTextBox.text.toString()
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Ingrese el correo y contraseña",Toast.LENGTH_LONG).show()
            } else {
                signUp(email,password)

            }
        }

    }

    private fun signUp(email: String, password: String){

        firebaseAuth.createUserWithEmailAndPassword(email,password).
        addOnCompleteListener(this){ task ->
            if(task.isSuccessful){
                val user = firebaseAuth.currentUser
                startActivity(Intent(this,LoginActivity::class.java))
            }
            else {
                Log.e("Error",task.exception.toString())

                Toast.makeText(this, "Error de registro", Toast.LENGTH_LONG).show()}

        }

    }

}