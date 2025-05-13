package com.example.historyapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ScoreActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        // Retrieve score data
        val score = intent.getIntExtra("FINAL_SCORE", 0)
        val total = intent.getIntExtra("TOTAL_QUESTIONS", 0)
        val percent = if (total > 0) (score * 100) / total else 0

        // UI references
        val scoreText = findViewById<TextView>(R.id.scoreText)
        val starsText = findViewById<TextView>(R.id.starsText)
        val feedbackText = findViewById<TextView>(R.id.feedbackText)
        val progressBar = findViewById<ProgressBar>(R.id.scoreProgressBar)
        val shareButton = findViewById<Button>(R.id.shareButton)
        val retryButton = findViewById<Button>(R.id.retryButton)

        // Display numeric score
        scoreText.text = "You scored $score out of $total"

        // Update progress bar
        progressBar.max = 100
        progressBar.progress = percent

        // Simple star rating using Unicode ★☆☆
        val starCount = when {
            percent >= 80 -> 3
            percent >= 50 -> 2
            percent >= 30 -> 1
            else -> 0
        }
        val stars = "★".repeat(starCount) + "☆".repeat(3 - starCount)
        starsText.text = stars

        // Personalized feedback
        val feedback = when {
            percent >= 80 -> "You're crushing it!\uD83C\uDFC6"
            percent >= 50 -> " Great progress! \uD83C\uDFAF"
            percent >= 30 -> " Almost there! \uD83D\uDEE0\uFE0F"
            else -> " Don't stop now — you’re improving! \uD83D\uDD25💪"
        }
        feedbackText.text = feedback

        // Share functionality
        shareButton.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "I scored $score/$total on the History Quiz!")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        // Retry quiz
        retryButton.setOnClickListener {
            val intent = Intent(this, QuestionsActivity::class.java) // ✅ Fixed class name
            startActivity(intent) // ✅ You forgot this
            finish()
        }
    }
}
