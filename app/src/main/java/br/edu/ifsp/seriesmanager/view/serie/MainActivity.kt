package br.edu.ifsp.seriesmanager.view.serie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.seriesmanager.R
import br.edu.ifsp.seriesmanager.adapter.SeriesRvAdapter
import br.edu.ifsp.seriesmanager.controller.SerieController
import br.edu.ifsp.seriesmanager.databinding.ActivityMainBinding
import br.edu.ifsp.seriesmanager.model.serie.Serie
import br.edu.ifsp.seriesmanager.view.temporada.TemporadaActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), OnSerieClickListener {

    companion object Extras {
        const val EXTRA_SERIE = "EXTRA_SERIE"
        const val EXTRA_POSICAO = "EXTRA_POSICAO"
    }

    //binding
    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //result launchers
    private lateinit var serieActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarSerieActivityResultLauncher: ActivityResultLauncher<Intent>

    //data source
    private val seriesList: MutableList<Serie> by lazy {
        serieController.buscarSeries()
    }

    private val serieController: SerieController by lazy {
        SerieController(this)
    }

    //adapter
    private val seriesAdapter: SeriesRvAdapter by lazy {
        SeriesRvAdapter(this, seriesList)
    }

    //layout manager
    private val seriesLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        //associando adapter e layout manager ao ListView
        activityMainBinding.seriesRv.adapter = seriesAdapter
        activityMainBinding.seriesRv.layoutManager = seriesLayoutManager

        //configurando result launcher
        serieActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                resultado ->
            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getParcelableExtra<Serie>(EXTRA_SERIE)?.apply {
                    serieController.inserirSerie(this)
                    seriesList.add(this)
                    seriesAdapter.notifyDataSetChanged()
                }
            }
        }

        editarSerieActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO, -1)
                resultado.data?.getParcelableExtra<Serie>(EXTRA_SERIE)?.apply {
                    if (posicao != null && posicao != -1) {
                        serieController.modificarSerie(this)
                        seriesList[posicao] = this
                        seriesAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
        //botao adicionar
        activityMainBinding.adicionarSerieFab.setOnClickListener {
            serieActivityResultLauncher.launch(Intent(this, SerieActivity::class.java))
        }
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
            val posicao = seriesAdapter.posicao

            return when(item.itemId) {
                R.id.editarMi -> {
                    val serie = seriesList[posicao]
                    val editarSerieIntent = Intent(this, SerieActivity::class.java)
                    editarSerieIntent.putExtra(EXTRA_SERIE, serie)
                    editarSerieIntent.putExtra(EXTRA_POSICAO, posicao)
                    editarSerieActivityResultLauncher.launch(editarSerieIntent)
                    true
                }
                R.id.removerMi -> {
                    val serie = seriesList[posicao]
                    with(AlertDialog.Builder(this)) {
                        setMessage("Confirma remoção?")
                        setPositiveButton("Sim") {_, _, ->
                            serieController.removerSerie(serie.nome)
                            seriesList.removeAt(posicao)
                            seriesAdapter.notifyDataSetChanged()
                        }
                        setNegativeButton("Não") {_,_, ->
                            Snackbar.make(activityMainBinding.root, "Remoção cancelada", Snackbar.LENGTH_SHORT).show()
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

    override fun onSerieClick(posicao: Int) {
        val serie = seriesList[posicao]
        val consultaTemporadasIntent = Intent(this, TemporadaActivity::class.java)
        consultaTemporadasIntent.putExtra(EXTRA_SERIE, serie)
        startActivity(consultaTemporadasIntent)
    }
}