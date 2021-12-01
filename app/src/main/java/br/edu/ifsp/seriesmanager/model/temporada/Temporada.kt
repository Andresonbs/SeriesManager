package br.edu.ifsp.seriesmanager.model.temporada

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Temporada(
    val numeroSequencial: Int = -1, //chave primaria
    val anoLancamento: Int = -1,
    val numeroEpisodios: Int = -1,
    val serie: String = ""
): Parcelable
