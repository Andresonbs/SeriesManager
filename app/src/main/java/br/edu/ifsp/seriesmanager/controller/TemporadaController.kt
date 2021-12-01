package br.edu.ifsp.seriesmanager.controller

import br.edu.ifsp.seriesmanager.model.temporada.Temporada
import br.edu.ifsp.seriesmanager.model.temporada.TemporadaDao
import br.edu.ifsp.seriesmanager.model.temporada.TemporadaSqlite
import br.edu.ifsp.seriesmanager.view.temporada.TemporadaActivity

class TemporadaController(temporadaActivity: TemporadaActivity) {
    private val temporadaDao: TemporadaDao = TemporadaSqlite(temporadaActivity)

    fun inserirTemporada(temporada: Temporada) = temporadaDao.salvarTemporada(temporada)
    fun buscarTemporada(num: Int) = temporadaDao.recuperarTemporada(num)
    fun buscarTemporadas(nome: String) = temporadaDao.recuperarTemporadas(nome)
    fun modificarTemporada(temporada: Temporada) = temporadaDao.atualizarTemporada(temporada)
    fun removerTemporada(num: Int, serie: String) = temporadaDao.removerTemporada(num, serie)
}