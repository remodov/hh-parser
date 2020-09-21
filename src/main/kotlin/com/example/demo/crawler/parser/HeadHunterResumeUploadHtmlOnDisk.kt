package com.example.demo.crawler.parser

import org.jsoup.nodes.Document
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class HeadHunterResumeUploadHtmlOnDisk(
        private val pathForResumeSave: String) : DocumentParser<String> {

    override fun parse(document: Document): String {
        val resumeHtml = document.toString()
        val pathToResume = Paths.get(pathForResumeSave, document.baseUri().split("?")[0].split("resume/")[1] + ".html")
        Files.write(pathToResume, resumeHtml.toByteArray(), StandardOpenOption.CREATE)
        return pathToResume.toString()
    }
}