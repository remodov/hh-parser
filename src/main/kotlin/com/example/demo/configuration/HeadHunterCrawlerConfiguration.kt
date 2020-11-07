package com.example.demo.configuration

import com.example.demo.client.HtmlLoadClient
import com.example.demo.crawler.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HeadHunterCrawlerConfiguration {
    @Value("\${application.resume-upload-dir}")
    private lateinit var pathForResumeSave: String

    @Bean
    fun headHunterJobCrawler(client: HtmlLoadClient): JobCrawler {
        return HeadHunterJobCrawler(
                nextPageLinkParser = HeadHunterNextPageLinkParser(),
                pageParser = HeadHunterPageParser(),
                documentLoader = HeadHunterDocumentLoader(),
                client = client,
                dirForSave = pathForResumeSave
        )
    }
}