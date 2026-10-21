package com.example.aiagent

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.util.Log

class MyAccessibilityService : AccessibilityService() {

    companion object {
        var instance: MyAccessibilityService? = null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        Log.d("AIAgent", "Служба подключена!")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    // Функция для "чтения" экрана (возвращает текст всех элементов)
    fun getScreenText(): String {
        val rootNode = rootInActiveWindow ?: return "Экран пуст или недоступен"
        val sb = StringBuilder()
        traverseNode(rootNode, sb)
        return sb.toString()
    }

    private fun traverseNode(node: AccessibilityNodeInfo?, sb: StringBuilder) {
        if (node == null) return
        if (!node.text.isNullOrEmpty()) {
            sb.append("Текст: ").append(node.text).append("\n")
        }
        if (!node.contentDescription.isNullOrEmpty()) {
            sb.append("Описание: ").append(node.contentDescription).append("\n")
        }
        for (i in 0 until node.childCount) {
            traverseNode(node.getChild(i), sb)
        }
    }

    // Функция для нажатия кнопки "Назад"
    fun performBack() {
        performGlobalAction(GLOBAL_ACTION_BACK)
    }
    
    // Функция для нажатия кнопки "Домой"
    fun performHome() {
        performGlobalAction(GLOBAL_ACTION_HOME)
    }
}
