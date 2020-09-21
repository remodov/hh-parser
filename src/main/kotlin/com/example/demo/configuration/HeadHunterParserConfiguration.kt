package com.example.demo.configuration

import com.example.demo.crawler.parser.*
import com.example.demo.model.Employee
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URL
import java.util.*

@Configuration
class HeadHunterParserConfiguration {
    @Value("\${application.resume-upload-dir}")
    private lateinit var pathForResumeSave: String

    @Bean
    fun headHunterNextPageLinkParser(): DocumentParser<Optional<URL>> {
        return HeadHunterNextPageLinkParser()
    }

    @Bean
    fun headHunterResumeLinksOnPageParser(): DocumentParser<List<URL>> {
        return HeadHunterResumeLinksOnPageParser()
    }

    @Bean
    fun headHunterResumeParser(): DocumentParser<Employee> {
        return HeadHunterResumeParser()
    }

    @Bean
    fun headHunterResumeUploadHtmlOnDisk(): DocumentParser<String> {
        return HeadHunterResumeUploadHtmlOnDisk(pathForResumeSave)
    }

}
