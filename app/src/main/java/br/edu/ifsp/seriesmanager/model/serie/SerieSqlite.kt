package br.edu.ifsp.seriesmanager.model.serie

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.sql.SQLException

class SerieSqlite(context: Context): SerieDao {

    companion object {
        private val BD_SERIES = "series"
        private val TABELA_SERIE = "serie"
        private val COLUNA_NOME = "nome"
        private val COLUNA_ANO_LANCAMENTO = "ano_lancamento"
        private val COLUNA_EMISSORA = "emissora"
        private val COLUNA_GENERO = "genero"

        private val CRIAR_TABELA_SERIE_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_SERIE} (" +
                "${COLUNA_NOME} TEXT NOT NULL PRIMARY KEY," +
                "${COLUNA_ANO_LANCAMENTO} INTEGER NOT NULL," +
                "${COLUNA_EMISSORA} TEXT NOT NULL," +
                "${COLUNA_GENERO} TEXT NOT NULL );"

    }

    private val seriesBD: SQLiteDatabase
    init {
        seriesBD = context.openOrCreateDatabase(BD_SERIES, MODE_PRIVATE, null)
        try {
            seriesBD.execSQL(CRIAR_TABELA_SERIE_STMT)
        } catch (se: SQLException) {
            Log.e("SeriesManager", se.toString())
        }
    }


    override fun salvarSerie(serie: Serie): Long {
        val serieCV = converterSerieParaContentValues(serie)
        return seriesBD.insert(TABELA_SERIE, null, serieCV)
    }

    override fun recuperarSerie(nome: String): Serie {
        val serieCursor = seriesBD.query(
            true,
            TABELA_SERIE,
            null,
            "${COLUNA_NOME} = ?",
            arrayOf(nome),
            null,
            null,
            null,
            null
        )

        if (serieCursor.moveToFirst()) {
            with(serieCursor) {
                return Serie(
                    getString(getColumnIndexOrThrow(COLUNA_NOME)),
                    getInt(getColumnIndexOrThrow(COLUNA_ANO_LANCAMENTO)),
                    getString(getColumnIndexOrThrow(COLUNA_EMISSORA)),
                    getString(getColumnIndexOrThrow(COLUNA_GENERO))
                )
            }
        } else {
            return Serie()
        }

    }

    override fun recuperarSeries(): MutableList<Serie> {
        val serieCursor = seriesBD.query(
            true,
            TABELA_SERIE,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val listaSeries: MutableList<Serie> = ArrayList()

        while (serieCursor.moveToNext()){
            listaSeries.add(
                with(serieCursor) {
                    Serie(
                        getString(getColumnIndexOrThrow(COLUNA_NOME)),
                        getInt(getColumnIndexOrThrow(COLUNA_ANO_LANCAMENTO)),
                        getString(getColumnIndexOrThrow(COLUNA_EMISSORA)),
                        getString(getColumnIndexOrThrow(COLUNA_GENERO))
                    )
                }
            )
        }
        return listaSeries
    }

    override fun atualizarSerie(serie: Serie): Int {
        val serieCv = converterSerieParaContentValues(serie)
        return seriesBD.update(TABELA_SERIE, serieCv, "${COLUNA_NOME} = ?", arrayOf(serie.nome))
    }

    override fun removerSerie(nome: String): Int {
        return seriesBD.delete(TABELA_SERIE, "${COLUNA_NOME} = ?", arrayOf(nome))
    }

    private fun converterSerieParaContentValues(serie: Serie) = ContentValues().also {
        with(it) {
            put(COLUNA_NOME, serie.nome)
            put(COLUNA_ANO_LANCAMENTO, serie.anoLancamento)
            put(COLUNA_EMISSORA, serie.emissora)
            put(COLUNA_GENERO, serie.genero)
        }
    }
}