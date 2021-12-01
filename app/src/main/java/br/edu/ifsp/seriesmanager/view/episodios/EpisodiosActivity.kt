package br.edu.ifsp.seriesmanager.view.episodios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.seriesmanager.R
import br.edu.ifsp.seriesmanager.adapter.EpisodioRvAdapter
import br.edu.ifsp.seriesmanager.controller.EpisodioController
import br.edu.ifsp.seriesmanager.databinding.ActivityEpisodiosBinding
import br.edu.ifsp.seriesmanager.model.episodio.Episodio
import br.edu.ifsp.seriesmanager.view.serie.MainActivity
import br.edu.ifsp.seriesmanager.view.temporada.TemporadaActivity
import com.google.android.material.snackbar.Snackbar

class EpisodiosActivity : AppCompatActivity(), OnEpisodeClickListener {

    companion object Extras {
        const val EXTRA_EPISODIO = "EXTRA_EPISODIO"
    }

    private var serie: String = ""
    private var temporada: Int = -1

    private lateinit var novoEpisodioActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarEpisodioActivityResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var episodioList: MutableList<Episodio>

    private val episodioController: EpisodioController by lazy {
        EpisodioController(this)
    }

    private lateinit var episodioAdapter: EpisodioRvAdapter

    private val activityEpisodiosBinding: ActivityEpisodiosBinding by lazy {
        ActivityEpisodiosBinding.inflate(layoutInflater)
    }

    private val episodioLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityEpisodiosBinding.root)
        title = "Episódios"

        serie = intent.getStringExtra(MainActivity.EXTRA_SERIE)!!
        temporada = intent.getIntExtra(TemporadaActivity.EXTRA_TEMPORADA, -1)

        episodioList = episodioController.buscarEpisodios(serie, temporada)
        episodioAdapter = EpisodioRvAdapter(this, episodioList)

        activityEpisodiosBinding.episodiosRv.adapter = episodioAdapter
        activityEpisodiosBinding.episodiosRv.layoutManager = episodioLayoutManager

        novoEpisodioActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getParcelableExtra<Episodio>(EXTRA_EPISODIO)?.apply {
                    episodioController.inserirEpisodio(this)
                    episodioList.add(this)
                    episodioAdapter.notifyDataSetChanged()
                }
            }
        }

        editarEpisodioActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(TemporadaActivity.EXTRA_POSICAO_TEMPORADA, -1)
                resultado.data?.getParcelableExtra<Episodio>(EXTRA_EPISODIO)?.apply {
                    if (posicao != null && posicao != -1) {
                        episodioController.modificarEpisodio(this)
                        episodioList[posicao] = this
                        episodioAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityEpisodiosBinding.adicionarEpisodioFab.setOnClickListener {
            val intent = Intent(this, EpisodioFormActivity::class.java)
            intent.putExtra(TemporadaActivity.EXTRA_TEMPORADA, temporada)
            intent.putExtra(MainActivity.EXTRA_SERIE, serie)
            novoEpisodioActivityResultLauncher.launch(intent)
        }
    }

    override fun onEpisodeClick(position: Int) {
        var episodio = episodioList[position]
        with(AlertDialog.Builder(this)) {
            setMessage("O que deseja fazer?")
            setPositiveButton("Editar") {_, _, ->
                val editarEpisodioIntent = Intent(this@EpisodiosActivity, EpisodioFormActivity::class.java)
                editarEpisodioIntent.putExtra(TemporadaActivity.EXTRA_TEMPORADA, temporada)
                editarEpisodioIntent.putExtra(EXTRA_EPISODIO, episodio)
                editarEpisodioIntent.putExtra(TemporadaActivity.EXTRA_POSICAO_TEMPORADA, position)
                editarEpisodioIntent.putExtra(MainActivity.EXTRA_SERIE, serie)
                editarEpisodioActivityResultLauncher.launch(editarEpisodioIntent)
            }
            setNegativeButton("Assistir") {_,_, ->
                if (!episodio.assitido){
                    episodio.assitido = true
                    episodioController.modificarEpisodio(episodio)
                    episodioList[position] = episodio
                    episodioAdapter.notifyDataSetChanged()
                    Snackbar.make(activityEpisodiosBinding.root, "Episodio marcado como assistido", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(activityEpisodiosBinding.root, "Episodio já foi assistido", Snackbar.LENGTH_SHORT).show()
                }
            }
            create()
        }.show()
    }
}