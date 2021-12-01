package br.edu.ifsp.seriesmanager.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.seriesmanager.R
import br.edu.ifsp.seriesmanager.databinding.LayoutTemporadaBinding
import br.edu.ifsp.seriesmanager.model.temporada.Temporada
import br.edu.ifsp.seriesmanager.view.temporada.OnTemporadaClickListener

class TemporadaRvAdapter(
    private val onTemporadaClickListener: OnTemporadaClickListener,
    private val temporadasList: MutableList<Temporada>
): RecyclerView.Adapter<TemporadaRvAdapter.TemporadaLayoutHolder>() {

    var posicao: Int = -1

    inner class TemporadaLayoutHolder(layoutTemporadaBinding: LayoutTemporadaBinding): RecyclerView.ViewHolder(layoutTemporadaBinding.root), View.OnCreateContextMenuListener {
        val numeroTv: TextView = layoutTemporadaBinding.numeroTv
        val anoTv: TextView = layoutTemporadaBinding.anoTv

        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            MenuInflater(view?.context).inflate(R.menu.context_menu_main, menu)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemporadaLayoutHolder {
        val layoutTemporadaBinding = LayoutTemporadaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = TemporadaLayoutHolder(layoutTemporadaBinding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: TemporadaLayoutHolder, position: Int) {
        val temporada = temporadasList[position]

        with(holder) {
            numeroTv.text = temporada.numeroSequencial.toString()
            anoTv.text = temporada.anoLancamento.toString()
            itemView.setOnClickListener {
                onTemporadaClickListener.onTemporadaClick(position)
            }
            itemView.setOnLongClickListener {
                posicao = position
                false
            }

        }
    }

    override fun getItemCount(): Int {
        return temporadasList.size
    }
}