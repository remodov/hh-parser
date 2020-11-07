package com.example.demo.loader

interface Step {
    fun perform()

    fun getName() : String
}

abstract class AbstractLoader {
    private val steps: MutableList<Step> = mutableListOf()

    fun addStep(step: Step) {
        steps.add(step)
    }

    fun execute() {
        steps.forEach {
            println("Start execute ${it.getName()}")
            it.perform()
            println("End execute ${it.getName()}")
        }
    }
}