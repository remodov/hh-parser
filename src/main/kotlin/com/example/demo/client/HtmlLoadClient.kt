package com.example.demo.client

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
import java.net.URL

@Component
class HtmlLoadClientTLSNone : HtmlLoadClient {
    override fun load(url: URL): Document {
        return Jsoup.connect(url.toString())
                .validateTLSCertificates(false)
                .get()
    }
}

interface HtmlLoadClient {
    fun load(url: URL) : Document
}