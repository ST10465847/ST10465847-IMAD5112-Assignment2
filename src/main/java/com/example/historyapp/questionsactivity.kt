package com.example.historyapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.historyapp.R

class QuestionsActivity : AppCompatActivity() {

    private var score = 0
    private var questionIndex = 0
    private lateinit var countDownTimer: CountDownTimer

    private val timePerQuestionMillis = 30_000L

    private val questions = listOf(
        Triple("Who was the leader of the African National Congress (ANC) during the early years of resistance against apartheid?",
            listOf("Oliver Tambo","Walter Sisulu","Albertina Sisulu", "Govan Mbeki"),
            "Oliver Tambo"),
        Triple("Which event marked the beginning of apartheid in South Africa?",
            listOf("The 1948 general election", "The 1960 Sharpeville Massacre", "The 1976 Soweto Uprising", "The 1990 release of Nelson Mandela"),
            "The 1948 general election"),
        Triple("What year was the Soweto Uprising, a turning point in the anti-apartheid struggle?",
            listOf("1966", "1976", "1982", "1990"),
            "1976"),
        Triple("Which South African leader won the Nobel Peace Prize in 1993?",
            listOf("Desmond Tutu", "F.W. de Klerk", "Nelson Mandela", "Albertina Sisulu"),
            "Nelson Mandela"),
        Triple("Which iconic South African activist was known for the slogan 'Free Nelson Mandela'?",
            listOf("Steve Biko", "Desmond Tutu", "Hugh Masekela", "Brenda Fassie"),
            "Hugh Masekela")
    )

    private lateinit var questionText: TextView
    private lateinit var optionA: Button
    private lateinit var optionB: Button
    private lateinit var optionC: Button
    private lateinit var optionD: Button
    private lateinit var nextButton: Button
    private lateinit var timerText: TextView

    private var selectedAnswer: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionsactivity)

        questionText = findViewById(R.id.questionstext)
        optionA = findViewById(R.id.option1)
        optionB = findViewById(R.id.option2)
        optionC = findViewById(R.id.option3)
        optionD = findViewById(R.id.option4)
        nextButton = findViewById(R.id.nextButton)
        timerText = findViewById(R.id.timertext)

        // Set up answer buttons
        listOf(optionA, optionB, optionC, optionD).forEach { btn ->
            btn.setOnClickListener {
                countDownTimer.cancel()
                selectedAnswer = btn.text.toString()
                nextButton.isEnabled = true
                btn.setBackgroundColor(0xFFAAF0D1.toInt()) // highlight selected
            }
        }

        // Next button
        nextButton.setOnClickListener {
            countDownTimer.cancel()
            checkAnswerAndProceed()
        }

        // Load first question
        loadQuestion()
    }

    private fun loadQuestion() {
        // Reset UI
        listOf(optionA, optionB, optionC, optionD).forEach {
            it.setBackgroundColor(0xFFDDDDDD.toInt())
        }
        nextButton.isEnabled = false
        selectedAnswer = null

        // Display question and options
        val (qText, opts, _) = questions[questionIndex]
        questionText.text = qText
        optionA.text = opts[0]
        optionB.text = opts[1]
        optionC.text = opts[2]
        optionD.text = opts[3]

        // Start timer for this question
        startTimer()
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timePerQuestionMillis, 1_000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(ms: Long) {
                timerText.text = "Time left: ${ms / 1000}s"
            }
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                timerText.text = "Time's up!"
                checkAnswerAndProceed()
            }
        }.start()
    }

    private fun checkAnswerAndProceed() {
        // Score it
        if (selectedAnswer == questions[questionIndex].third) {
            score++
        }

        // Move to next
        questionIndex++
        if (questionIndex < questions.size) {
            loadQuestion()
        } else {
            // Quiz over
            Intent(this, ScoreActivity::class.java).also {
                it.putExtra("FINAL_SCORE", score)
                it.putExtra("TOTAL_QUESTIONS", questions.size)
                startActivity(it)
            }
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::countDownTimer.isInitialized) countDownTimer.cancel()
    }
}