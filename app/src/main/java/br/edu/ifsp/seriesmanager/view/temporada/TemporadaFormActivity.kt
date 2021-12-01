package br.edu.ifsp.seriesmanager.view.temporada

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import br.edu.ifsp.seriesmanager.R
import br.edu.ifsp.seriesmanager.databinding.ActivityTemporadaFormBinding
import br.edu.ifsp.seriesmanager.model.serie.Serie
import br.edu.ifsp.seriesmanager.model.temporada.Temporada
import br.edu.ifsp.seriesmanager.view.serie.MainActivity

class TemporadaFormActivity : AppCompatActivity() {

    private lateinit var activityTemporadaFormBinding: ActivityTemporadaFormBinding

    private var posicao = -1
    private var temporada: Temporada? = null
    private var serie: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTemporadaFormBinding = ActivityTemporadaFormBinding.inflate(layoutInflater)
        setContentView(activityTemporadaFormBinding.root)
        title = "Temporadas"

        //recuperar nome da serie
        serie = intent.getStringExtra(MainActivity.EXTRA_SERIE)!!

        activityTemporadaFormBinding.salvarBt.setOnClickListener {
            with(activityTemporadaFormBinding) {
                temporada = Temporada(
                    numeroEt.text.toString().toInt(),
                    anoEt.text.toString().toInt(),
                    numEpisodiosEt.text.toString().toInt(),
                    serie
                )
            }

            val resultadoIntent = Intent()
            resultadoIntent.putExtra(TemporadaActivity.EXTRA_TEMPORADA, temporada)
            if (posicao != -1) {
                resultadoIntent.putExtra(TemporadaActivity.EXTRA_POSICAO_TEMPORADA, posicao)
            }
            setResult(RESULT_OK, resultadoIntent)
            finish()
        }


        temporada = intent.getParcelableExtra(TemporadaActivity.EXTRA_TEMPORADA)
        posicao = intent.getIntExtra(TemporadaActivity.EXTRA_POSICAO_TEMPORADA, -1)
        if (temporada != null){
            with(activityTemporadaFormBinding){
                numeroEt.isEnabled = false
                numeroEt.text = temporada!!.numeroSequencial.toString().toEditable()
                anoEt.text = temporada!!.anoLancamento.toString().toEditable()
                numEpisodiosEt.text = temporada!!.numeroEpisodios.toString().toEditable()
            }
            if (posicao == -1) {
                val num = activityTemporadaFormBinding.getRoot().getChildCount();
                for (i in 0..num) {
                    activityTemporadaFormBinding.root.getChildAt(i).isEnabled = false
                }
                activityTemporadaFormBinding.salvarBt.visibility = View.GONE
            }
        }

    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
}