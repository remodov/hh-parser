package com.example.demo.configuration

import com.example.demo.crawler.parser.DocumentParser
import com.example.demo.crawler.parser.HeadHunterNextPageLinkParser
import com.example.demo.crawler.parser.HeadHunterResumeLinksOnPageParser
import com.example.demo.crawler.parser.HeadHunterResumeParser
import com.example.demo.model.Employee
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URL
import java.util.*

@Configuration
class HeadHunterParserConfiguration{

    @Bean
    fun headHunterNextPageLinkParser() : DocumentParser<Optional<URL>>{
        return HeadHunterNextPageLinkParser()
    }

    @Bean
    fun headHunterResumeLinksOnPageParser() : DocumentParser<List<URL>>{
        return HeadHunterResumeLinksOnPageParser()
    }

    @Bean
    fun headHunterResumeParser() : DocumentParser<Employee>{
        return HeadHunterResumeParser()
    }
}
