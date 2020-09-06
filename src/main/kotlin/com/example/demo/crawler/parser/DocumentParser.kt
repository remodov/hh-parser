package com.example.demo.crawler.parser
import org.jsoup.nodes.Document

interface DocumentParser<T> {
    fun parse(document: Document): T
}