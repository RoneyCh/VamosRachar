package com.example.vamosrachar

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.util.Locale

class MainActivity : ComponentActivity() {
    private lateinit var textToSpeech: TextToSpeech

    private lateinit var totalValueEditText: EditText
    private lateinit var numPeopleEditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var listenButton: Button
    private lateinit var shareButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        totalValueEditText = findViewById(R.id.total_value_edit_text)
        numPeopleEditText = findViewById(R.id.num_people_edit_text)
        resultTextView = findViewById(R.id.result_text_view)
        listenButton = findViewById(R.id.listen_button)
        shareButton = findViewById(R.id.shareButton)

        setupNumPeopleEditText()
        setupTextToSpeech()
        setupListenButton()
        setupShareButton()
    }

    private fun setupNumPeopleEditText() {
        numPeopleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                updateResultTextView()
            }
        })
    }

    private fun updateResultTextView() {
        val totalValue = totalValueEditText.text.toString().toDoubleOrNull()
        val numPeople = numPeopleEditText.text.toString().toDoubleOrNull()
        if (totalValue != null && numPeople != null && numPeople != 0.0) {
            val result = totalValue / numPeople
            resultTextView.text = "R$ %.2f por pessoa".format(result)
        } else {
            resultTextView.text = ""
        }
    }

    private fun setupTextToSpeech() {
        textToSpeech = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech.language = Locale.getDefault()
            }
        }
    }

    private fun setupListenButton() {
        listenButton.setOnClickListener {
            val text = resultTextView.text.toString()
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    private fun setupShareButton() {
        shareButton.setOnClickListener {
            val text = resultTextView.text.toString()
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    override fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }
}
