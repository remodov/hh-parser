package com.example.demo

import com.example.demo.crawler.HeadHunterCrawler
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DemoApplicationTests {
	@Autowired
	lateinit var crawler : HeadHunterCrawler

	@Test
	fun contextLoads() {
		crawler.parse()
	}
}
