package br.edu.ifsp.seriesmanager.model.episodio

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Episodio(
    val numEpisodio: Int = -1,
    val nomeEpisodio: String = "",
    val tempoDuracao: Int = -1,
    var assitido: Boolean = false,
    val temporada: Int = -1,
    val serie: String = ""
): Parcelable
