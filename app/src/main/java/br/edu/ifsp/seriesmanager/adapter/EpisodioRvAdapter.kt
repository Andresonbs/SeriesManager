package br.edu.ifsp.seriesmanager.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.seriesmanager.R
import br.edu.ifsp.seriesmanager.databinding.LayoutEpisodioBinding
import br.edu.ifsp.seriesmanager.model.episodio.Episodio
import br.edu.ifsp.seriesmanager.view.episodios.OnEpisodeClickListener

class EpisodioRvAdapter(
    private val onEpisodeClickListener: OnEpisodeClickListener,
    private val episodiosList: MutableList<Episodio>
): RecyclerView.Adapter<EpisodioRvAdapter.EpisodioLayoutHolder>() {
    var posicao = -1

    inner class EpisodioLayoutHolder(layoutEpisodioBinding: LayoutEpisodioBinding): RecyclerView.ViewHolder(layoutEpisodioBinding.root) {
        val nomeTv: TextView = layoutEpisodioBinding.nomeTv
        val numeroTv: TextView = layoutEpisodioBinding.numeroTv
        val assistidoTv: TextView = layoutEpisodioBinding.assistidoTv

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodioLayoutHolder {
        val layoutEpisodioBinding = LayoutEpisodioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = EpisodioLayoutHolder(layoutEpisodioBinding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: EpisodioLayoutHolder, position: Int) {
        val episodio = episodiosList[position]
        val assistido = "Assistido"
        val naoAssistido = "Nao assistido"

        with(holder) {
            nomeTv.text = episodio.nomeEpisodio
            numeroTv.text = episodio.numEpisodio.toString()
            if (episodio.assitido){
                assistidoTv.text = assistido
            } else {
                assistidoTv.text = naoAssistido
            }
            itemView.setOnClickListener {
                onEpisodeClickListener.onEpisodeClick(position)
            }
            itemView.setOnLongClickListener {
                posicao = position
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return episodiosList.size
    }
}