package com.example.startbasic

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.startbasic.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var button: Button
    private lateinit var randomView: TextView
    private var job: Job? = null
    private lateinit var binding: ActivityMainBinding
    private var counter = 1
    private var isButtonPressed = false
//    Job은 완료될 때까지 생명주기가 있는 취소 가능한 항목이다.
//    Job은 상위 항목을 취소하면 모든 하위 항목이 재귀적으로 즉시 취소되는
//    상위-하위 계층 구조로 정렬될 수 있다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // button을 findViewById로 만듬
        // textView를 findViewById로 만듬
        // randomView를 findViewById로 만듬
        randomView = findViewById(R.id.randomText)
        textView = findViewById(R.id.firstText)
        button = findViewById(R.id.clickButton)

        setupButton()
        setRandomValueBetweenOneToHundred()
        setJobAndLaunch()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        counter = savedInstanceState.getInt("counter")
        randomView.text = savedInstanceState.getString("random")
        isButtonPressed = savedInstanceState.getBoolean("isButtonPressed", false) // 기본값을 false로 설정
        textView.text = savedInstanceState.getString("textView")
        // 버튼을 누르고 회전을 하면 카운트는 더 이상 안올라감
        if (isButtonPressed) {
            job?.cancel()
        } else {
            setJobAndLaunch()
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("counter", counter)
        outState.putString("random", randomView.text.toString())
        outState.putString("textView", textView.text.toString())
        outState.putBoolean("isButtonPressed", isButtonPressed)
        super.onSaveInstanceState(outState)
    }

    private fun setupButton() {
        button.setOnClickListener {
            // 작업 취소
            job?.cancel()
            checkAnswerAndShowToast()
            // 버튼을 누를 시 false에서 true로 변경
            isButtonPressed = true
        }
    }


    private fun setRandomValueBetweenOneToHundred() {
        // 100 이하의 숫자를 랜덤으로 생성
        val random: Int = (1..100).random()
        randomView.text = random.toString()
    }

    private fun setJobAndLaunch() {
        job?.cancel() // job is uninitialized exception
        job = lifecycleScope.launch {
            while (counter <= 100) {
                if (isActive) {
                    binding.firstText.text = counter++.toString()
                    delay(500) // 1초 = 1000
                }
            }
        }
    }

    private fun checkAnswerAndShowToast() {
        // Toast 메세지 생성
        if (randomView.toString() == textView.toString()) {
            Toast.makeText(this, "같은 수 입니다", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "다른 수 입니다", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onResume() {
        if (!isButtonPressed) {
            setJobAndLaunch() // 버튼이 눌러지지 않았다면, 카운트를 재개
        }
        super.onResume()
    }

    override fun onPause() {
        job?.cancel() // 현재 진행중인 Job을 취소하여 카운트를 멈춤
        super.onPause()
    }
}