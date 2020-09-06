package com.example.demo.crawler.parser

import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
import java.net.URL

class HeadHunterResumeLinksOnPageParser : DocumentParser<List<URL>> {
    override fun parse(document: Document): List<URL> {
        return document.getElementsByClass("resume-search-item__name")
                .eachAttr("href")
                .map { URL("https://hh.ru$it") }
    }
}
