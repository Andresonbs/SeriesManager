package br.edu.ifsp.seriesmanager.view.episodios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import br.edu.ifsp.seriesmanager.databinding.ActivityEpisodioFormBinding
import br.edu.ifsp.seriesmanager.model.episodio.Episodio
import br.edu.ifsp.seriesmanager.view.serie.MainActivity
import br.edu.ifsp.seriesmanager.view.temporada.TemporadaActivity

class EpisodioFormActivity : AppCompatActivity() {

    private lateinit var activityEpisodioFormBinding: ActivityEpisodioFormBinding;

    private var posicao = -1
    private var temporada = -1
    private var serie = ""
    private var episodio: Episodio? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEpisodioFormBinding = ActivityEpisodioFormBinding.inflate(layoutInflater)
        setContentView(activityEpisodioFormBinding.root)

        title = "Episódios"
        serie = intent.getStringExtra(MainActivity.EXTRA_SERIE)!!
        temporada = intent.getIntExtra(TemporadaActivity.EXTRA_TEMPORADA, -1)

        activityEpisodioFormBinding.salvarBt.setOnClickListener {
            var assistido = false
            if (episodio != null) {
                assistido = episodio!!.assitido
            }
            with(activityEpisodioFormBinding) {
                episodio = Episodio(
                    numeroEt.text.toString().toInt(),
                    nomeEt.text.toString(),
                    duracaoEt.text.toString().toInt(),
                    assistido,
                    temporada,
                    serie
                )
            }

            posicao = intent.getIntExtra(TemporadaActivity.EXTRA_POSICAO_TEMPORADA, -1)
            val resultadoIntent = Intent()
            resultadoIntent.putExtra(EpisodiosActivity.EXTRA_EPISODIO, episodio)
            if (posicao != -1) {
                resultadoIntent.putExtra(TemporadaActivity.EXTRA_POSICAO_TEMPORADA, posicao)
            }
            setResult(RESULT_OK, resultadoIntent)
            finish()
        }

        episodio = intent.getParcelableExtra(EpisodiosActivity.EXTRA_EPISODIO)
        if (episodio != null) {
            with(activityEpisodioFormBinding) {
                numeroEt.isEnabled = false
                numeroEt.text = episodio!!.numEpisodio.toString().toEditable()
                nomeEt.text = episodio!!.nomeEpisodio.toEditable()
                duracaoEt.text = episodio!!.tempoDuracao.toString().toEditable()
            }
        }
    }
    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}