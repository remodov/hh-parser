package com.example.demo

import com.example.demo.loader.HeadHunterResumeParser
import org.jsoup.Jsoup
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.util.ResourceUtils
import java.util.stream.Stream

class ResumeParserTest {

    @Test
    fun parseHeadHunterResume() {
        val file = ResourceUtils.getFile("classpath:hh-test-resume.html")
        val document = Jsoup.parse(file, "UTF-8")
        val parse = HeadHunterResumeParser("").convert(document)
        assertNotNull(parse)
        assertEquals("Мужчина", parse.male)
        assertEquals("Москва", parse.liveCity)
        assertEquals("Руководитель проекта. Аналитик", parse.position)
        assertEquals("Опыт работы 24 года 7 месяцев", parse.workExperience)
        assertNull(parse.salary)
        assertEquals(9, parse.companiesEmployee.size)
    }

    @Test
    fun parseHeadHunterResumeWithSalary() {
        val file = ResourceUtils.getFile("classpath:hh-test-resume-salary.html")
        val document = Jsoup.parse(file, "UTF-8")
        val parse = HeadHunterResumeParser("").convert(document)

        println(parse)
        assertEquals("Женщина", parse.male)
        assertEquals("Москва", parse.liveCity)
        assertEquals("Программист Oracle", parse.position)
        assertEquals("Опыт работы 17 лет 7 месяцев", parse.workExperience)
        assertEquals("190 000 руб.", parse.salary)
        assertEquals(10, parse.companiesEmployee.size)
    }

    @Test
    fun parseHeadHunterResumeWithGithubLink() {
        val file = ResourceUtils.getFile("classpath:hh-test-github.html")
        val document = Jsoup.parse(file, "UTF-8")
        val parse = HeadHunterResumeParser("").convert(document)
        assertNotNull(parse)
        assertEquals("Мужчина", parse.male)
        assertEquals("Москва", parse.liveCity)
        assertEquals("Руководитель проекта. Аналитик", parse.position)
        assertEquals("Опыт работы 24 года 7 месяцев", parse.workExperience)
        assertNull(parse.salary)
        assertEquals(9, parse.companiesEmployee.size)
    }

    @Test
    fun generateLinks() {
        val experiences = listOf("noExperience", "moreThan6", "between3And6", "between1And3")
        for (area in 0 until 400) {
            for (experience in experiences) {
                println(
                        """insert into buffer_hh_v1.parser_url (source, url)
						   values( 'hh.ru', 'https://mytischi.hh.ru/search/resume?clusters=true&exp_period=all_time&logic=normal&no_magic=false&order_by=relevance&pos=full_text&text=java+developer&area=${area}&experience=${experience}&skill=3093')
						    ;
						"""
                )
            }
        }
    }
}
