package com.example.demo.core

enum class TaskType {
    PARSE_HH, BUFFER_HH, STAGE_HH, SITE_HH
}

interface LoadTask {
    fun getCountLoadedDocuments() : Int
    fun getCountErrors() : Int
    fun getTaskType() : TaskType
}