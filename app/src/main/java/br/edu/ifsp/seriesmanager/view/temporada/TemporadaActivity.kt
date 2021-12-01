package br.edu.ifsp.seriesmanager.view.temporada

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.seriesmanager.R
import br.edu.ifsp.seriesmanager.adapter.TemporadaRvAdapter
import br.edu.ifsp.seriesmanager.controller.TemporadaController
import br.edu.ifsp.seriesmanager.databinding.ActivityTemporadaBinding
import br.edu.ifsp.seriesmanager.model.serie.Serie
import br.edu.ifsp.seriesmanager.model.temporada.Temporada
import br.edu.ifsp.seriesmanager.view.episodios.EpisodiosActivity
import br.edu.ifsp.seriesmanager.view.serie.MainActivity
import com.google.android.material.snackbar.Snackbar

class TemporadaActivity : AppCompatActivity(), OnTemporadaClickListener {

    companion object Extras {
        const val EXTRA_TEMPORADA = "EXTRA_TEMPORADA"
        const val EXTRA_POSICAO_TEMPORADA = "EXTRA_POSICAO_TEMPORADA"
    }

    private lateinit var novaTemporadaActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarTemporadaActivityResultLauncher: ActivityResultLauncher<Intent>

    private var serie: Serie? = null

    //data source
    private lateinit var temporadaList: MutableList<Temporada>

    private val temporadaController: TemporadaController by lazy {
        TemporadaController(this)
    }

    private lateinit var temporadaAdapter: TemporadaRvAdapter

    private val activityTemporadaBinding: ActivityTemporadaBinding by lazy {
        ActivityTemporadaBinding.inflate(layoutInflater)
    }

    private val temporadaLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityTemporadaBinding.root)

        title = "Temporadas"

        //recuperar serie
        serie = intent.getParcelableExtra(MainActivity.EXTRA_SERIE)

        temporadaList = temporadaController.buscarTemporadas(serie!!.nome)

        temporadaAdapter = TemporadaRvAdapter(this, temporadaList)

        activityTemporadaBinding.temporadasRv.adapter = temporadaAdapter
        activityTemporadaBinding.temporadasRv.layoutManager = temporadaLayoutManager

        novaTemporadaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getParcelableExtra<Temporada>(EXTRA_TEMPORADA)?.apply {
                    temporadaController.inserirTemporada(this)
                    temporadaList.add(this)
                    temporadaAdapter.notifyDataSetChanged()
                }
            }
        }

        editarTemporadaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO_TEMPORADA, -1)
                resultado.data?.getParcelableExtra<Temporada>(EXTRA_TEMPORADA)?.apply {
                    if (posicao != null && posicao != -1) {
                        temporadaController.modificarTemporada(this)
                        temporadaList[posicao] = this
                        temporadaAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityTemporadaBinding.adicionarTemporadaFab.setOnClickListener {
            val intent = Intent(this, TemporadaFormActivity::class.java)
            intent.putExtra(MainActivity.EXTRA_SERIE, serie?.nome)
            novaTemporadaActivityResultLauncher.launch(intent)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = temporadaAdapter.posicao

        return when(item.itemId) {
            R.id.editarMi -> {
                val temporada = temporadaList[posicao]
                val editarTemporadaIntent = Intent(this, TemporadaFormActivity::class.java)
                editarTemporadaIntent.putExtra(EXTRA_TEMPORADA, temporada)
                editarTemporadaIntent.putExtra(EXTRA_POSICAO_TEMPORADA, posicao)
                editarTemporadaIntent.putExtra(MainActivity.EXTRA_SERIE, serie!!.nome)
                editarTemporadaActivityResultLauncher.launch(editarTemporadaIntent)
                true
            }
            R.id.removerMi -> {
                val temporada = temporadaList[posicao]
                with(AlertDialog.Builder(this)) {
                    setMessage("Confirma remoção?")
                    setPositiveButton("Sim") {_, _, ->
                        temporadaController.removerTemporada(temporada.numeroSequencial, serie!!.nome)
                        temporadaList.removeAt(posicao)
                        temporadaAdapter.notifyDataSetChanged()
                    }
                    setNegativeButton("Não") {_,_, ->
                        Snackbar.make(activityTemporadaBinding.root, "Remoção cancelada", Snackbar.LENGTH_SHORT).show()
                    }
                    create()
                }.show()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onTemporadaClick(posicao: Int) {
        val temporada = temporadaList[posicao]
        val consultaEpisodiosIntent = Intent(this, EpisodiosActivity::class.java)
        consultaEpisodiosIntent.putExtra(MainActivity.EXTRA_SERIE, serie!!.nome)
        consultaEpisodiosIntent.putExtra(EXTRA_TEMPORADA, temporada.numeroSequencial)
        startActivity(consultaEpisodiosIntent)
    }
}