package com.example.demo.crawler

import com.example.demo.client.HtmlLoadClient
import com.example.demo.crawler.parser.DocumentParser
import com.example.demo.model.Employee
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.net.URL
import java.util.*

@Component
class HeadHunterCrawler(
        val headHunterNextPageLinkParser: DocumentParser<Optional<URL>>,
        val headHunterResumeLinksOnPageParser: DocumentParser<List<URL>>,
        val headHunterResumeParser: DocumentParser<Employee>,
        val client: HtmlLoadClient
) {

    fun parse(){
        println("Parser start work")

        var url = URL("https://hh.ru/search/resume?text=java&area=1&isDefaultArea=true&exp_period=all_time&logic=normal&pos=full_text&fromSearchLine=true&st=resumeSearch&page=248")

        while (true) {
            val pageWithResumeLinks = client.load(url)

            headHunterResumeLinksOnPageParser.parse(pageWithResumeLinks).forEach { linkWithResume ->
                val loadedResumeDocument = client.load(linkWithResume)
                val parsedResume = headHunterResumeParser.parse(loadedResumeDocument)
                println("Parsed resume: $parsedResume")
            }

            val isNextUrl = headHunterNextPageLinkParser.parse(pageWithResumeLinks)

            if (!isNextUrl.isPresent){
                break
            }

            url = isNextUrl.get()
        }

        println("Parser end work")
    }


}