package com.example.demo.crawler

import com.example.demo.client.HtmlLoadClient
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.*
import javax.annotation.PostConstruct

@Component
class HeadHunterCrawler(val client: HtmlLoadClient) {
    @Value("\${application.resume-upload-dir}")
    private lateinit var pathForResumeSave: String

    @PostConstruct
    fun initLoad() {
        this.start();
    }

    fun start() {
        println("Parser start work")

        var url = URL("https://hh.ru/search/resume?text=java&area=1&isDefaultArea=true&exp_period=all_time&logic=normal&pos=full_text&fromSearchLine=true&st=resumeSearch&page=0")

        while (true) {
            val pageWithResumeLinks = client.load(url)

            resumeLinksOnPageParser(pageWithResumeLinks).forEach { linkWithResume ->
                val loadedResumeDocument = client.load(linkWithResume)
                val parsedResume = uploadResumeOnDisk(loadedResumeDocument)
                println("Parsed resume: $parsedResume")
            }

            val isNextUrl = nextPageLinkParser(pageWithResumeLinks)

            if (!isNextUrl.isPresent) {
                break
            }

            url = isNextUrl.get()
        }

        println("Parser end work")
    }

    private fun nextPageLinkParser(document: Document): Optional<URL> {
        val nextPageElement = document.getElementsByAttributeValue("data-qa", "pager-next")

        nextPageElement.let {
            val nextPageUrls = nextPageElement.eachAttr("href")
            if (nextPageUrls.size != 1) {
                return Optional.empty();
            }

            return Optional.of(URL("https://hh.ru" + nextPageUrls[0]))
        }
    }

    private fun resumeLinksOnPageParser(document: Document): List<URL> {
        return document.getElementsByClass("resume-search-item__name")
                .eachAttr("href")
                .map { URL("https://hh.ru$it") }
    }

    private fun uploadResumeOnDisk(document: Document): String {
        val resumeHtml = document.toString()
        val pathToResume = Paths.get(pathForResumeSave, document.baseUri().split("?")[0].split("resume/")[1] + ".html")
        Files.write(pathToResume, resumeHtml.toByteArray(), StandardOpenOption.CREATE)
        return pathToResume.toString()
    }
}