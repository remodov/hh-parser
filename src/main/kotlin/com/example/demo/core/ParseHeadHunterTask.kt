package com.example.demo.core

import com.example.demo.crawler.JobCrawler
import com.example.demo.repository.ParserUrlsRepository
import org.springframework.stereotype.Component
import java.net.URL

@Component
class ParseHeadHunterTask(
        val headHunterJobCrawler: JobCrawler,
        val urlsRepository: ParserUrlsRepository) : LoadTask {

    override fun getCountLoadedDocuments(): Int {
        return headHunterJobCrawler.getCountLoadedDocuments()
    }

    override fun getCountErrors(): Int {
        return headHunterJobCrawler.getCountErrors()
    }

    override fun getTaskType(): TaskType {
        return TaskType.PARSE_HH
    }

    override fun start() {
        val urlsForLoad = urlsRepository.findAll().map { URL(it.url) }
        headHunterJobCrawler.parse(urlsForLoad)
    }
}