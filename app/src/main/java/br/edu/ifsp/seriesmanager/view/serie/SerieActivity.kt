package br.edu.ifsp.seriesmanager.view.serie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import br.edu.ifsp.seriesmanager.R
import br.edu.ifsp.seriesmanager.databinding.ActivitySerieBinding
import br.edu.ifsp.seriesmanager.model.serie.Serie

class SerieActivity : AppCompatActivity() {

    private lateinit var activitySerieBinding: ActivitySerieBinding

    private var posicao = -1
    private var serie: Serie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySerieBinding = ActivitySerieBinding.inflate(layoutInflater)
        setContentView(activitySerieBinding.root)
        title = "Séries"

        activitySerieBinding.salvarBt.setOnClickListener{
            with(activitySerieBinding) {
                serie = Serie(
                    nomeEt.text.toString(),
                    anoEt.text.toString().toInt(),
                    emissoraEt.text.toString(),
                    generoEt.text.toString()
                )
            }

            val resultadoIntent = Intent()
            resultadoIntent.putExtra(MainActivity.EXTRA_SERIE, serie)
            if (posicao != -1) {
                resultadoIntent.putExtra(MainActivity.EXTRA_POSICAO, posicao)
            }
            setResult(RESULT_OK, resultadoIntent)
            finish()
        }

        serie = intent.getParcelableExtra(MainActivity.EXTRA_SERIE)
        posicao = intent.getIntExtra(MainActivity.EXTRA_POSICAO, -1)
        if (serie != null) {
            with(activitySerieBinding) {
                nomeEt.isEnabled = false
                nomeEt.text = serie!!.nome.toEditable()
                anoEt.text = serie!!.anoLancamento.toString().toEditable()
                emissoraEt.text = serie!!.emissora.toEditable()
                generoEt.text = serie!!.genero.toEditable()
            }
            if (posicao == -1) {
                val num = activitySerieBinding.getRoot().getChildCount();
                for (i in 0..num) {
                    activitySerieBinding.root.getChildAt(i).isEnabled = false
                }
                activitySerieBinding.salvarBt.visibility = View.GONE
            }
        }

    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}