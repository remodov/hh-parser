package com.example.demo.crawler

import com.example.demo.loader.HeadHunterResumeParser
import com.example.demo.repository.EmployeeCompanyRepository
import com.example.demo.repository.EmployeeRepository
import com.example.demo.repository.ParserUrlsRepository
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger
import javax.annotation.PostConstruct

@Component
class Starter(
        val employeeRepository: EmployeeRepository,
        val employeeCompanyRepository: EmployeeCompanyRepository,
        val parser: HeadHunterResumeParser,
        val urlsRepository: ParserUrlsRepository,
        val headHunterJobCrawler: JobCrawler
) {
    @Value("\${application.resume-upload-dir}")
    private lateinit var pathForResumeSave: String

    @Value("\${application.action}")
    private lateinit var initAction: String

    @PostConstruct
    fun initLoad() {
        if (initAction == "LOAD") {
            this.startLoad()
        } else {
            this.startParse()
        }
    }

    fun startParse() {
        println("Parser start work")

        var count = AtomicInteger()

        employeeCompanyRepository.deleteAllInBatch()
        employeeRepository.deleteAllInBatch()

        val totalDocuments = File(pathForResumeSave).listFiles().toList().parallelStream().map {
            val document = Jsoup.parse(it, "UTF-8")
            val employee = parser.convert(document)
            employeeRepository.save(employee)

            if (count.incrementAndGet() % 100 == 0) {
                println("Loaded $count ")
            }
        }.count()

        println("Total Loaded $totalDocuments ")
    }

    fun startLoad() {
        println("Parser start work")

        val urlsForLoad = urlsRepository.findAll().map { URL(it.url) }

        headHunterJobCrawler.parse(urlsForLoad)

        println("Parser end work")
    }
}