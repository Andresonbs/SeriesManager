package br.edu.ifsp.seriesmanager.model.temporada

import br.edu.ifsp.seriesmanager.model.serie.Serie

interface TemporadaDao {
    fun salvarTemporada(temporada: Temporada): Long
    fun recuperarTemporada(num: Int): Temporada
    fun recuperarTemporadas(nome: String): MutableList<Temporada>
    fun atualizarTemporada(temporada: Temporada): Int
    fun removerTemporada(num: Int, serie: String): Int
}