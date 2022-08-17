package com.example.examplenavigationdrawer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TiendaAdapter : RecyclerView.Adapter<TiendaAdapter.ViewHolder>() {
    var tiendas: ArrayList<TiendaModel>  = ArrayList()
    lateinit var context: Context


    fun TiendaAdapter(tiendas : ArrayList<TiendaModel>, context: Context){
        this.tiendas = tiendas
        this.context = context
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tiendas[position]
        holder.bind(item)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.product_item, parent, false))
    }
    override fun getItemCount(): Int {
        return tiendas.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sucursal = view.findViewById(R.id.tiendaNombre) as TextView

        fun bind(tienda:TiendaModel){
            sucursal.text = tienda.sucursal

        }

    }

}