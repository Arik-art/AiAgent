package com.example.aiagent

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var chatLog: TextView
    private lateinit var inputField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        // Кнопка для выдачи разрешения Службе
        val settingsButton = Button(this).apply {
            text = "1. Включить доступ к экрану"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }

        chatLog = TextView(this).apply {
            text = "Шаг 1: Нажми кнопку выше и включи 'AiAgent' в настройках.\nШаг 2: Пиши команды в чат."
        }
        val scrollView = ScrollView(this).apply {
            addView(chatLog)
        }
        
        inputField = EditText(this).apply {
            hint = "Введите команду..."
        }
        
        val sendButton = Button(this).apply {
            text = "Отправить"
            setOnClickListener {
                val text = inputField.text.toString()
                if (text.isNotEmpty()) {
                    chatLog.append("\n\nВы: $text")
                    inputField.setText("")
                    sendRequestToAI(text)
                }
            }
        }

        val weight = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            1.0f
        )
        layout.addView(settingsButton)
        layout.addView(scrollView, weight)
        layout.addView(inputField)
        layout.addView(sendButton)

        setContentView(layout)
    }

    private fun sendRequestToAI(prompt: String) {
        thread {
            try {
                Thread.sleep(800) // Имитация задержки
                runOnUiThread {
                    // Проверяем, работает ли служба
                    val service = MyAccessibilityService.instance
                    val status = if (service != null) "Служба активна!" else "Служба ВЫКЛЮЧЕНА (нажми кнопку 1)."
                    
                    chatLog.append("\nИИ: Принято '$prompt'. $status")
                    
                    // Если служба работает, попробуем прочитать экран
                    if (service != null) {
                         val screenText = service.getScreenText()
                         chatLog.append("\n(Я вижу на экране: ${screenText.take(50)}...)")
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    chatLog.append("\nОшибка: ${e.message}")
                }
            }
        }
    }
}
