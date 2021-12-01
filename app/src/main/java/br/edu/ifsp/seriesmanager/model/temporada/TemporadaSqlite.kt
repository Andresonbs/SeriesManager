package br.edu.ifsp.seriesmanager.model.temporada

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.sql.SQLException

class TemporadaSqlite(context: Context): TemporadaDao {

    companion object {
        private val BD_TEMPORADA = "temporadas"
        private val TABELA_TEMPORADA = "temporada"
        private val COLUNA_NUMERO_SEQUENCIAL = "numero_sequencial"
        private val COLUNA_ANO_LANCAMENTO = "ano_lancamento"
        private val COLUNA_QUANTIDADE_EPISODIOS = "quantidade_episodios"
        private val COLUNA_SERIE = "serie"

        private val CRIAR_TABELA_TEMPORADA_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_TEMPORADA} (" +
                "${COLUNA_ANO_LANCAMENTO} INTEGER NOT NULL PRIMARY KEY," +
                "${COLUNA_NUMERO_SEQUENCIAL} INTEGER NOT NULL," +
                "${COLUNA_QUANTIDADE_EPISODIOS} INTEGER NOT NULL," +
                "${COLUNA_SERIE} TEXT NOT NULL );"
    }

    private val temporadasBd: SQLiteDatabase
    init {
        temporadasBd = context.openOrCreateDatabase(BD_TEMPORADA, MODE_PRIVATE, null)
        try {
            temporadasBd.execSQL(CRIAR_TABELA_TEMPORADA_STMT)
        } catch (se: SQLException) {
            Log.e("SeriesManager", se.toString())
        }
    }

    override fun salvarTemporada(temporada: Temporada): Long {
        val temporadaCv = converterTemporadaParaContentValues(temporada)
        return temporadasBd.insert(TABELA_TEMPORADA, null, temporadaCv)
    }

    override fun recuperarTemporada(num: Int): Temporada {
        val temporadaCursor = temporadasBd.query(
            true,
            TABELA_TEMPORADA,
            null,
            "${COLUNA_NUMERO_SEQUENCIAL} = ?",
            arrayOf(num.toString()),
            null,
            null,
            null,
            null
        )
        if (temporadaCursor.moveToFirst()){
            with(temporadaCursor) {
                return Temporada(
                    getInt(getColumnIndexOrThrow(COLUNA_NUMERO_SEQUENCIAL)),
                    getInt(getColumnIndexOrThrow(COLUNA_ANO_LANCAMENTO)),
                    getInt(getColumnIndexOrThrow(COLUNA_QUANTIDADE_EPISODIOS)),
                    getString(getColumnIndexOrThrow(COLUNA_SERIE))
                )
            }
        } else {
            return Temporada()
        }
    }

    override fun recuperarTemporadas(nome: String): MutableList<Temporada> {
        val temporadaCursor = temporadasBd.query(
            true,
            TABELA_TEMPORADA,
            null,
            "${COLUNA_SERIE} = ?",
           arrayOf(nome),
            null,
            null,
            null,
            null
        )

        val listaTemporadas: MutableList<Temporada> = ArrayList()

        while (temporadaCursor.moveToNext()){
            listaTemporadas.add(
                with(temporadaCursor){
                    Temporada(
                        getInt(getColumnIndexOrThrow(COLUNA_NUMERO_SEQUENCIAL)),
                        getInt(getColumnIndexOrThrow(COLUNA_ANO_LANCAMENTO)),
                        getInt(getColumnIndexOrThrow(COLUNA_QUANTIDADE_EPISODIOS)),
                        getString(getColumnIndexOrThrow(COLUNA_SERIE))
                    )
                }
            )
        }
        return listaTemporadas
    }

    override fun atualizarTemporada(temporada: Temporada): Int {
        val temporadaCv = converterTemporadaParaContentValues(temporada)
        return temporadasBd.update(TABELA_TEMPORADA, temporadaCv, "${COLUNA_NUMERO_SEQUENCIAL} = ?", arrayOf(temporada.numeroSequencial.toString()))
    }

    override fun removerTemporada(num: Int, serie: String): Int {
        return temporadasBd.delete(TABELA_TEMPORADA, "${COLUNA_NUMERO_SEQUENCIAL} = ?, ${COLUNA_SERIE} = ?", arrayOf(num.toString(), serie))
    }

    private fun converterTemporadaParaContentValues(temporada: Temporada) = ContentValues().also {
        with(it) {
            put(COLUNA_ANO_LANCAMENTO, temporada.anoLancamento)
            put(COLUNA_NUMERO_SEQUENCIAL, temporada.numeroSequencial)
            put(COLUNA_QUANTIDADE_EPISODIOS, temporada.numeroEpisodios)
            put(COLUNA_SERIE, temporada.serie)
        }
    }
}