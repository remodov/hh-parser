package com.example.demo

import com.example.demo.core.LoadTask
import jdk.nashorn.internal.runtime.regexp.joni.Config.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@SpringBootApplication
@EnableScheduling
class DemoApplication {
	@Autowired lateinit var allTasks: List<LoadTask>

	@Value("\${application.tasks-run}")
	private lateinit var tasksRun: String

	@Scheduled(fixedRate = 50_000_000)
	fun reportCurrentTime() {
		val tasksForStart = tasksRun.split(",").toList()

		allTasks.sortedBy { it.getTaskType().order }
				.filter { tasksForStart.contains(it.getTaskType().name) }
				.forEach { it.start() }
	}
}

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

