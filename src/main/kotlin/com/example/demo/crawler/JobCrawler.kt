package com.example.demo.crawler

import com.example.demo.client.HtmlLoadClient
import org.jsoup.nodes.Document
import java.lang.Exception
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicInteger


interface JobCrawler {
    fun parse(searchUrls: List<URL>)
    fun getCountLoadedDocuments(): Int
    fun getCountErrors(): Int
    fun isWork(): Boolean
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
        val pathToResume = Paths.get(pathForSave.toString(), getUniqueId(document) + ".html")
        if (Files.exists(pathToResume)) {
            println("Resume exists: $pathToResume")
            return
        }
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
    private val countLoaded = AtomicInteger()
    private val countErrors = AtomicInteger()

    @Volatile
    private var isWork = false

    override fun isWork(): Boolean {
        return isWork
    }

    override fun getCountLoadedDocuments(): Int {
        return countLoaded.get()
    }

    override fun getCountErrors(): Int {
        return countErrors.get()
    }

    override fun parse(searchUrls: List<URL>) {
        synchronized(isWork) {
            if (!isWork) {
                isWork = true
                var count: Long = 0L
                try {
                    val myPool = ForkJoinPool(8);
                    myPool.submit {
                        searchUrls.parallelStream().forEach {
                            loadSearchUrl(it)
                        }
                    }.get()

                } finally {
                    println("Perform work success: $count")
                    isWork = false
                    countLoaded.set(0)
                    countErrors.set(0)
                }
            }
            else {
                println("Task working")
            }
        }
    }

    private fun loadSearchUrl(searchUrl: URL) {
        var currentUrl = searchUrl

        while (true) {
            println("Start parse: $currentUrl")

            val pageWithResumeLinks = client.load(currentUrl)

            val result = pageParser.parse(pageWithResumeLinks)
                    .map { linkWithResume ->
                        try {
                            val loadedResumeDocument = client.load(linkWithResume)

                            documentLoader.load(loadedResumeDocument, Paths.get(dirForSave))

                            println("Loaded: ${countLoaded.incrementAndGet()}")
                        } catch (e: Exception) {
                            println("Error when load: $linkWithResume")
                            e.printStackTrace()
                            println("Errors: ${countErrors.incrementAndGet()}")
                        }
                    }.toList()

            println("Total Parsed resume from page: ${result.size}")

            val isNextUrl = nextPageLinkParser.parse(pageWithResumeLinks)

            if (!isNextUrl.isPresent) {
                break
            }
            currentUrl = isNextUrl.get()
        }
    }

//    private fun getCurrentDateDirectory(): String {
//        val formatter = SimpleDateFormat("yyyy-MM-dd")
//        return formatter.format(Date())
//    }
}