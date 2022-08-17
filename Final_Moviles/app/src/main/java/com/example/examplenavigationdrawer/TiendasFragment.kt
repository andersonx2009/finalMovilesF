package com.example.examplenavigationdrawer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TiendasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class TiendaViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)

class TiendasFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val firebaseDatabase = FirebaseFirestore.getInstance()

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tiendas, container, false)
         val recyclerViewTienda = view.findViewById<RecyclerView>(R.id.recycler_view_tiendas)

        val mAdapter= TiendaAdapter()
        val query = firebaseDatabase.collection("tiendas").orderBy("sucursal")
val options = FirestoreRecyclerOptions.Builder<TiendaModel>().setQuery(query,TiendaModel::class.java).setLifecycleOwner(this).build()


        val adapter = object :FirestoreRecyclerAdapter<TiendaModel,TiendaViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiendaViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)


                return  TiendaViewHolder(layoutInflater.inflate(R.layout.product_item, parent, false))
            }

            override fun onBindViewHolder(
                holder: TiendaViewHolder,
                position: Int,
                model: TiendaModel
            ) {
                val tvSucursal : TextView = holder.itemView.findViewById(R.id.tiendaNombre)
                tvSucursal.text=model.sucursal
                val tvDireccion : TextView = holder.itemView.findViewById(R.id.tiendaDireccion2)
                tvDireccion.text=model.direccion
                val tvAforo : TextView = holder.itemView.findViewById(R.id.tiendaForo2)
                tvAforo.text=model.foro
                val tvTelefono : TextView = holder.itemView.findViewById(R.id.tiendaTelefono2)
                tvTelefono.text=model.telefono
            }


        }

        recyclerViewTienda.adapter=adapter
        recyclerViewTienda.layoutManager=LinearLayoutManager(context)


        return view

    }



private fun getTiendaData() :ArrayList<TiendaModel> {
var list= ArrayList<TiendaModel>()


    firebaseDatabase.collection("tiendas").get().addOnSuccessListener { results->
                    for(document in results){
                        if(document!=null){

                        val tienda = TiendaModel(document.getString("sucursal").toString(),
                            document.getString("direccion").toString(),
                            document.getString("telefono").toString(),
                            document.getString("foro").toString(),
                            document.getString("latitud").toString(),
                            document.getString("longitud").toString()
                            )
list.add(tienda)


                        }

                    }
                     Toast.makeText(context, list.toString(),Toast.LENGTH_LONG).show()
        Toast.makeText(context, list.size.toString(),Toast.LENGTH_LONG).show()
                }

   return list;



}




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OffersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TiendasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}