package br.edu.ifsp.seriesmanager.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.seriesmanager.view.serie.OnSerieClickListener
import br.edu.ifsp.seriesmanager.R
import br.edu.ifsp.seriesmanager.databinding.LayoutSerieBinding
import br.edu.ifsp.seriesmanager.model.serie.Serie

class SeriesRvAdapter(
    private val onSerieClickListener: OnSerieClickListener,
    private val seriesList: MutableList<Serie>
): RecyclerView.Adapter<SeriesRvAdapter.SerieLayoutHolder>() {

    var posicao: Int = -1

    inner class SerieLayoutHolder(layoutSerieBinding: LayoutSerieBinding): RecyclerView.ViewHolder(layoutSerieBinding.root), View.OnCreateContextMenuListener {
        val nomeTv: TextView = layoutSerieBinding.nomeTv
        val anoTv: TextView = layoutSerieBinding.anoTv
        val generoTv: TextView = layoutSerieBinding.generoTv

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerieLayoutHolder {
        //criar uma nova celula
        val layoutSerieBinding = LayoutSerieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //criar um viewholder associado a nova celula
        val viewHolder: SerieLayoutHolder = SerieLayoutHolder(layoutSerieBinding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: SerieLayoutHolder, position: Int) {
        //busca serie
        val serie = seriesList[position]

        with(holder){
            nomeTv.text = serie.nome
            anoTv.text = serie.anoLancamento.toString()
            generoTv.text = serie.genero
            itemView.setOnClickListener {
                onSerieClickListener.onSerieClick(position)
            }
            itemView.setOnLongClickListener {
                posicao = position
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return seriesList.size
    }
}