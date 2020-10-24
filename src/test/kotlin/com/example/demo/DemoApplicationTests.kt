package com.example.demo

import com.example.demo.crawler.HeadHunterCrawler
import com.example.demo.crawler.parser.HeadHunterResumeParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.ResourceUtils

@SpringBootTest
class DemoApplicationTests {
	@Autowired
	lateinit var crawler : HeadHunterCrawler

	@Test
	fun contextLoads() {
		crawler.parse()
	}

	@Test
	fun parseHeadHunterResume() {
		val file =
				ResourceUtils.getFile("classpath:hh-test-resume.html")

		val document = Jsoup.parse(file, "UTF-8")

		val parse = HeadHunterResumeParser().parse(document)

		println(parse)
		assertEquals("Мужчина", parse.male)

	}
}
