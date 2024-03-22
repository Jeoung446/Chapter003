package com.example.startbasic

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlinx.coroutines.delay as delay1

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var button: Button
    private lateinit var randomView: TextView
    private var job: Job? = null
//    Job은 완료될 때까지 생명주기가 있는 취소 가능한 항목이다.
//    Job은 상위 항목을 취소하면 모든 하위 항목이 재귀적으로 즉시 취소되는
//    상위-하위 계층 구조로 정렬될 수 있다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
       
        // button을 findViewById로 만듬
        // textView를 findViewById로 만듬
        // randomView를 findViewById로 만듬
        randomView = findViewById(R.id.randomText)
        textView = findViewById(R.id.firstText)
        button = findViewById(R.id.clickButton)

        ClickOk()
        setupButton()
        setRandomValueBetweenOneToHundred()
    }

    fun setupButton() {
        button.setOnClickListener {
            // 작업 취소
            job?.cancel()
            checkAnswerAndShowToast()
        }
    }

    fun ClickOk() {
//        var i = 1
//        launch의 리턴타입은 Job이다
        job = lifecycleScope.launch {
//            while (isActive && i <= 100) {
//                textView.text = i.toString()
//                delay1(100)
//                i++ // ++i, i++
//            }
            // while문 생성 대신 if문으로 변경
            for(i in 1..100) {
                if(isActive) {
                    // 활성화 상태일 떄
                    textView.text = i.toString()
//                    5초동안 대기
                    delay1(500)
                }
            }
        }
    }

    fun setRandomValueBetweenOneToHundred() {
        // 100 이하의 숫자를 랜덤으로 생성
        val random: Int = (1..100).random()
        randomView.text = random.toString()
    }

    private fun checkAnswerAndShowToast() {
        // Toast 메세지 생성
        if (randomView.toString() == textView.toString()) {
            Toast.makeText(this, "같은 수 입니다", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "다른 수 입니다", Toast.LENGTH_SHORT).show()
        }
    }
}