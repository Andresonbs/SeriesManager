package br.edu.ifsp.seriesmanager.controller

import br.edu.ifsp.seriesmanager.view.serie.MainActivity
import br.edu.ifsp.seriesmanager.model.serie.Serie
import br.edu.ifsp.seriesmanager.model.serie.SerieDao
import br.edu.ifsp.seriesmanager.model.serie.SerieSqlite

class SerieController(mainActivity: MainActivity) {
    private val serieDao: SerieDao = SerieSqlite(mainActivity)

    fun inserirSerie(serie: Serie) = serieDao.salvarSerie(serie)
    fun buscarSerie(nome: String) = serieDao.recuperarSerie(nome)
    fun buscarSeries() = serieDao.recuperarSeries()
    fun modificarSerie(serie: Serie) = serieDao.atualizarSerie(serie)
    fun removerSerie(nome: String) = serieDao.removerSerie(nome)
}