package br.edu.ifsp.seriesmanager.model.serie

interface SerieDao {

    fun salvarSerie(serie: Serie): Long
    fun recuperarSerie(nome: String): Serie
    fun recuperarSeries(): MutableList<Serie>
    fun atualizarSerie(serie: Serie): Int
    fun removerSerie(nome: String): Int
}