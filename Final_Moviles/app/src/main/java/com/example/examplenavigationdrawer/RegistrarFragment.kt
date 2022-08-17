package com.example.examplenavigationdrawer

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
private val firebaseDatabase = FirebaseFirestore.getInstance()

    private fun saveTienda(tienda: HashMap<String, String>) {
        firebaseDatabase.collection("tiendas").add(tienda).addOnSuccessListener {
            documentReference-> Toast.makeText(context, "Tienda guardada exitosamente",Toast.LENGTH_LONG).show()

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registrar, container, false)
        val buttonRegistrarTienda= view.findViewById<View>(R.id.buttonRegistrarTienda)

        buttonRegistrarTienda.setOnClickListener{
            val sucursal : String?  = view.findViewById<EditText>(R.id.sucursal).text.toString()
            val direccion : String? = view.findViewById<EditText>(R.id.Direccion).text.toString()
            val telefono : String? = view.findViewById<EditText>(R.id.Telefono).text.toString()
            val foro : String? = view.findViewById<EditText>(R.id.Foro).text.toString()
            val latitud : String? = view.findViewById<EditText>(R.id.Latitud).text.toString()
            val longitud  : String?= view.findViewById<EditText>(R.id.Longitud).text.toString()

            if(sucursal!!.isEmpty() || direccion!!.isEmpty() || telefono!!.isEmpty() || foro!!.isEmpty() || latitud!!.isEmpty() || longitud!!.isEmpty() )
            {
                Toast.makeText(context, "Ingrese los datos solicitados",Toast.LENGTH_LONG).show()
            }
else {

    val tienda = hashMapOf(
        "sucursal" to sucursal,
    "direccion" to direccion,
        "telefono" to telefono,
        "foro" to foro,
        "latitud" to latitud,
        "longitud" to longitud
        )
                saveTienda(tienda)
}

        }

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistrarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistrarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}