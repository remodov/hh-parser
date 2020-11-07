package com.example.demo.crawler

import com.example.demo.client.HtmlLoadClient
import com.example.demo.model.ParserUrls
import org.jsoup.nodes.Document
import org.springframework.remoting.support.UrlBasedRemoteAccessor
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.*

interface JobCrawler {
    fun parse(searchUrls: List<URL>)
}

interface NextPageLinkParser {
    fun parse(document: Document): Optional<URL>
}

interface PageParser {
    fun parse(document: Document): List<URL>
}

interface DocumentLoader {
    fun load(document: Document, pathForSave: Path)
    fun getUniqueId(document: Document): String
}

abstract class BaseSaveOnDisk : DocumentLoader {
    override fun load(document: Document, pathForSave: Path) {
        val resumeHtml = document.toString()
        val pathToResume = Paths.get(pathForSave.toString(), getUniqueId(document)+ ".html")
        Files.write(pathToResume, resumeHtml.toByteArray(), StandardOpenOption.CREATE)
    }
}

abstract class BaseJobCrawler(
        val nextPageLinkParser: NextPageLinkParser,
        val pageParser: PageParser,
        val documentLoader: DocumentLoader,
        val client: HtmlLoadClient,
        val dirForSave: String
) : JobCrawler {

    override fun parse(searchUrls: List<URL>) {
        val count = searchUrls.parallelStream().map { searchUrl ->
            loadSearchUrl(searchUrl)
        }.count()

        println("Perform work success: $count")
    }

    private fun loadSearchUrl(searchUrl: URL) {
        var currentUrl = searchUrl

        while (true) {
            println("Start parse: $currentUrl")

            val pageWithResumeLinks = client.load(currentUrl)

            val result = pageParser.parse(pageWithResumeLinks)
                    .map { linkWithResume ->
                        val loadedResumeDocument = client.load(linkWithResume)
                        val parsedResume = documentLoader.load(loadedResumeDocument, Paths.get(dirForSave))
                        println("Parsed resume: $parsedResume")
                    }.toList()

            println("Total Parsed resume from page: ${result.size}")

            val isNextUrl = nextPageLinkParser.parse(pageWithResumeLinks)

            if (!isNextUrl.isPresent) {
                break
            }
            currentUrl = isNextUrl.get()
        }
    }
}