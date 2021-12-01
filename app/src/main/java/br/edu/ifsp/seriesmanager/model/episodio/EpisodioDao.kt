package br.edu.ifsp.seriesmanager.model.episodio

import br.edu.ifsp.seriesmanager.model.serie.Serie

interface EpisodioDao {

    fun salvarEpisodio(episodio: Episodio): Long
    fun recuperarEpisodios(serie: String, temporada: Int): MutableList<Episodio>
    fun atualizarEpisodio(episodio: Episodio): Int
    fun removerEpisodio(serie: String, temporada: Int, numero: Int): Int
}