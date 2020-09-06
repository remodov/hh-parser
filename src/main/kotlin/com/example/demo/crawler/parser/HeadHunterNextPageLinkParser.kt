package com.example.demo.crawler.parser

import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
import java.net.URL
import java.util.*

class HeadHunterNextPageLinkParser : DocumentParser<Optional<URL>> {
    override fun parse(document: Document): Optional<URL> {
        val nextPageElement = document.getElementsByAttributeValue("data-qa", "pager-next")

        nextPageElement.let {
            val nextPageUrls = nextPageElement.eachAttr("href")
            if (nextPageUrls.size != 1) {
                return Optional.empty();
            }

            return Optional.of(URL("https://hh.ru" + nextPageUrls[0]))
        }
    }
}