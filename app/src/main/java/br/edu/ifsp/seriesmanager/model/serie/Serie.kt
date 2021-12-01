package br.edu.ifsp.seriesmanager.model.serie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Serie(
    val nome: String = "", //chave primaria
    val anoLancamento: Int = 0,
    val emissora: String = "",
    val genero: String = ""
) : Parcelable
