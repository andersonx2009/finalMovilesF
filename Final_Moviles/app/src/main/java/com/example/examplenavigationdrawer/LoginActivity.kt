package com.example.examplenavigationdrawer

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    val firebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val botonIniciarSesion = findViewById<Button>(R.id.buttonIniciarSesion)
        val botonIrRegistrar = findViewById<Button>(R.id.botonIrRegistrar)
        val correoTextBox= findViewById<EditText>(R.id.editTextTextPersonName)
        val passwordTextBox= findViewById<EditText>(R.id.editTextTextPassword2)

        botonIniciarSesion.setOnClickListener{

            val email = correoTextBox.text.toString()
            val password= passwordTextBox.text.toString()
         if(email.isEmpty() || password.isEmpty()){
Toast.makeText(this, "Ingrese el correo y contraseña",Toast.LENGTH_LONG).show()
} else {
    signIn(email,password)

}
        }
        botonIrRegistrar.setOnClickListener{


            startActivity(Intent(this,RegistroActivity::class.java))

        }


    }

    private fun signIn(email: String, password: String){

        firebaseAuth.signInWithEmailAndPassword(email,password).
        addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    startActivity(Intent(this,MainActivity::class.java))
                }
else {
    Log.e("Error",task.exception.toString())

    Toast.makeText(this, "Error de autenticación",Toast.LENGTH_LONG).show()}

            }

    }


}