package com.example.demo.crawler

import com.example.demo.client.HtmlLoadClient
import org.jsoup.nodes.Document
import java.net.URL
import java.util.*
import com.example.demo.crawler.BaseJobCrawler as BaseJobCrawler

class HeadHunterNextPageLinkParser : NextPageLinkParser {
    override fun parse(document: Document): Optional<URL> {
        val nextPageElement = document.getElementsByAttributeValue("data-qa", "pager-next")

        nextPageElement.let {
            val nextPageUrls = nextPageElement.eachAttr("href")
            if (nextPageUrls.size != 1) {
                return Optional.empty()
            }

            return Optional.of(URL("https://hh.ru" + nextPageUrls[0]))
        }
    }
}

class HeadHunterPageParser : PageParser {
    override fun parse(document: Document): List<URL> {
        return document.getElementsByClass("resume-search-item__name")
                .eachAttr("href")
                .map { URL("https://hh.ru$it") }
    }
}

class HeadHunterDocumentLoader : BaseSaveOnDisk() {
    override fun getUniqueId(document: Document): String {
        return document.baseUri().split("?")[0].split("resume/")[1]
    }
}

class HeadHunterJobCrawler(nextPageLinkParser: NextPageLinkParser,
                           pageParser: PageParser,
                           documentLoader: DocumentLoader,
                           client: HtmlLoadClient,
                           dirForSave: String
) : BaseJobCrawler(nextPageLinkParser, pageParser, documentLoader, client, dirForSave)