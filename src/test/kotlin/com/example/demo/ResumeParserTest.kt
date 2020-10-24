package com.example.demo

import com.example.demo.crawler.parser.HeadHunterResumeParser
import org.jsoup.Jsoup
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.util.ResourceUtils

class ResumeParserTest {

	@Test
	fun parseHeadHunterResume() {
		val file = ResourceUtils.getFile("classpath:hh-test-resume.html")
		val document = Jsoup.parse(file, "UTF-8")
		val parse = HeadHunterResumeParser().parse(document)

		println(parse)
		assertEquals("Мужчина", parse.male)
		assertEquals("Москва", parse.liveCity)
		assertEquals("Руководитель проекта. Аналитик", parse.position)
		assertEquals("Опыт работы 24 года 7 месяцев", parse.workExperience)
		assertNull(parse.salary)
		assertEquals(9, parse.companiesWork.size)
	}
}
