package com.example.demo.core

enum class TaskType(val order: Int) {
    PARSE_HH(1), BUFFER_HH(2), STAGE_HH(3), SITE_HH(4)
}

interface LoadTask {
    fun getCountLoadedDocuments() : Int
    fun getCountErrors() : Int
    fun getTaskType() : TaskType
    fun start()
}