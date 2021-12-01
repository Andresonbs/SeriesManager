package br.edu.ifsp.seriesmanager.controller

import br.edu.ifsp.seriesmanager.model.episodio.Episodio
import br.edu.ifsp.seriesmanager.model.episodio.EpisodioDao
import br.edu.ifsp.seriesmanager.model.episodio.EpisodioSqlite
import br.edu.ifsp.seriesmanager.view.episodios.EpisodiosActivity

class EpisodioController(episodiosActivity: EpisodiosActivity) {
    private val episodioDao: EpisodioDao = EpisodioSqlite(episodiosActivity)

    fun inserirEpisodio(episodio: Episodio) = episodioDao.salvarEpisodio(episodio)
    fun buscarEpisodios(serie: String, temporada: Int) = episodioDao.recuperarEpisodios(serie, temporada)
    fun modificarEpisodio(episodio: Episodio) = episodioDao.atualizarEpisodio(episodio)
    fun removerEpisodio(serie: String, temporada: Int, numero: Int) = episodioDao.removerEpisodio(serie, temporada, numero)
}