package com.example.demo.core

import com.example.demo.loader.HeadHunterResumeParser
import com.example.demo.repository.EmployeeCompanyRepository
import com.example.demo.repository.EmployeeRepository
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

@Component
class LoadHeadHunterInBufferTask(
        val employeeRepository: EmployeeRepository,
        val employeeCompanyRepository: EmployeeCompanyRepository,
        val parser: HeadHunterResumeParser,
        @Value("\${application.resume-upload-dir}")
        val pathForResumeSave: String
) : LoadTask {
    private val countLoaded = AtomicInteger()
    private val countErrors = AtomicInteger()

    override fun getCountLoadedDocuments(): Int {
        return countLoaded.get()
    }

    override fun getCountErrors(): Int {
        return countErrors.get()
    }

    override fun getTaskType(): TaskType {
        return TaskType.BUFFER_HH
    }

    @Synchronized
    override fun start() {
        println("Parser start work")
        employeeCompanyRepository.deleteAllInBatch()
        employeeRepository.deleteAllInBatch()

        val totalDocuments = File(pathForResumeSave).listFiles().toList().parallelStream().map {
            try {
                val document = Jsoup.parse(it, "UTF-8")
                val employee = parser.convert(document)
                employeeRepository.save(employee)

                if (countLoaded.incrementAndGet() % 100 == 0) {
                    println("Loaded $countLoaded")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                countErrors.incrementAndGet()
                println("Error on parse: $it")
                println("Errors: $countErrors")
            }
        }.count()

        println("Total Loaded $totalDocuments ")
    }
}