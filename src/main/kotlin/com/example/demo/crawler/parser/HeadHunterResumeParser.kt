package com.example.demo.crawler.parser

import com.example.demo.model.Employee
import org.jsoup.nodes.Document

class HeadHunterResumeParser : DocumentParser<Employee> {
    override fun parse(document: Document): Employee {
        val salaryAndPositionNameBlock = document.getElementsByAttributeValue("data-qa","resume-block-position")[0]

        val title = salaryAndPositionNameBlock.getElementsByAttributeValue("data-qa","resume-block-title-position").eachText().single()
        var salary = "none"
        var workExperience = "none"

        val salaryElements = salaryAndPositionNameBlock.getElementsByAttributeValue("data-qa","resume-block-salary")
        if (salaryElements.size == 1){
            salary = salaryElements[0].text()
        }

        val workExperienceBlock = document.getElementsByAttributeValue("data-qa", "resume-block-experience")
        if (workExperienceBlock.size > 0){
            workExperience = workExperienceBlock[0].getElementsByClass("resume-block__title-text resume-block__title-text_sub").text()

            workExperienceBlock[0].getElementsByClass("resume-block-item-gap").forEach {

            }
        }

        val employee = Employee(
                position = title,
                workExperience= workExperience,
                salary = salary,
                liveCity = "",
                male = "",
                headHunterResumeLink = document.location(),
                companiesWork = emptyList()
        )

        return employee
    }
}