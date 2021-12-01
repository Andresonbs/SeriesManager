package br.edu.ifsp.seriesmanager.model.episodio

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import br.edu.ifsp.seriesmanager.model.serie.Serie
import br.edu.ifsp.seriesmanager.model.serie.SerieSqlite
import java.sql.SQLException

class EpisodioSqlite(context: Context): EpisodioDao {

    companion object {
        private val BD_EPISODIOS = "episodios"
        private val TABELA_EPISODIO = "episodio"
        private val COLUNA_NOME = "nome"
        private val COLUNA_NUM_EPISODIO = "num_episodio"
        private val COLUNA_DURACAO = "duracao"
        private val COLUNA_TEMPORADA = "temporada"
        private val COLUNA_SERIE = "serie"
        private val COLUNA_ASSISTIDO = "assistido"

        private val CRIAR_TABELA_EPISODIO_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_EPISODIO} (" +
                "${COLUNA_NUM_EPISODIO} INTEGER NOT NULL," +
                "${COLUNA_NOME} TEXT NOT NULL," +
                "${COLUNA_DURACAO} INTEGER NOT NULL," +
                "${COLUNA_TEMPORADA} INTEGER NOT NULL," +
                "${COLUNA_ASSISTIDO} INTEGER NOT NULL," +
                "${COLUNA_SERIE} TEXT NOT NULL );"
    }

    private var episodioBd: SQLiteDatabase
    init {
        episodioBd = context.openOrCreateDatabase(BD_EPISODIOS, MODE_PRIVATE, null)
        try {
            episodioBd.execSQL(CRIAR_TABELA_EPISODIO_STMT)
        } catch (se: SQLException) {
            Log.e("SeriesManager", se.toString())
        }
    }

    override fun salvarEpisodio(episodio: Episodio): Long {
        val episodioCv = converterEpisodioParaContentValues(episodio)
        return episodioBd.insert(TABELA_EPISODIO, null, episodioCv)
    }

    override fun recuperarEpisodios(serie: String, temporada: Int): MutableList<Episodio> {
        val episodioCursor = episodioBd.query(
            true,
            TABELA_EPISODIO,
            null,
            "${COLUNA_SERIE} = ? AND ${COLUNA_TEMPORADA} = ?",
            arrayOf(serie, temporada.toString()),
            null,
            null,
            null,
            null
        )

        val listaEpisodios: MutableList<Episodio> = ArrayList()

        while (episodioCursor.moveToNext()){
            val assistidoNumero = episodioCursor.getInt(4)
            var assistido = assistidoNumero != 0
            listaEpisodios.add(
                with(episodioCursor){
                    Episodio(
                        getInt(getColumnIndexOrThrow(COLUNA_NUM_EPISODIO)),
                        getString(getColumnIndexOrThrow(COLUNA_NOME)),
                        getInt(getColumnIndexOrThrow(COLUNA_DURACAO)),
                        assistido,
                        getInt(getColumnIndexOrThrow(COLUNA_TEMPORADA)),
                        getString(getColumnIndexOrThrow(COLUNA_SERIE)),
                    )
                }
            )
        }
        return listaEpisodios
    }

    override fun atualizarEpisodio(episodio: Episodio): Int {
       val episodioCv = converterEpisodioParaContentValues(episodio)
        return episodioBd.update(TABELA_EPISODIO, episodioCv, "${COLUNA_SERIE} = ? AND ${COLUNA_TEMPORADA} = ? AND ${COLUNA_NUM_EPISODIO} = ?", arrayOf(episodio.serie, episodio.temporada.toString(), episodio.numEpisodio.toString()))
    }

    override fun removerEpisodio(serie: String, temporada: Int, numero: Int): Int {
        return episodioBd.delete(TABELA_EPISODIO, "${COLUNA_SERIE} = ? AND ${COLUNA_TEMPORADA} = ? AND ${COLUNA_NUM_EPISODIO} = ?", arrayOf(serie, temporada.toString(), numero.toString()))
    }

    private fun converterEpisodioParaContentValues(episodio: Episodio) = ContentValues().also {
        with(it) {
            if (episodio.assitido) {
                put(COLUNA_ASSISTIDO, 1)
            } else {
                put(COLUNA_ASSISTIDO, 0)
            }
            put(COLUNA_NUM_EPISODIO, episodio.numEpisodio)
            put(COLUNA_NOME, episodio.nomeEpisodio)
            put(COLUNA_DURACAO, episodio.tempoDuracao)
            put(COLUNA_SERIE, episodio.serie)
            put(COLUNA_TEMPORADA, episodio.temporada)
        }
    }

}