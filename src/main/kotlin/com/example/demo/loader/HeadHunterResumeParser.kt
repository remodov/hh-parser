package com.example.demo.loader

import com.example.demo.model.EmployeeCompany
import com.example.demo.model.Employee
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.file.Paths

@Component
class HeadHunterResumeParser(@Value("\${application.resume-upload-dir}")
                             val pathForResumeSave: String) {
    fun convert(document: Document): Employee {
        val employee = Employee()

        extractMale(document, employee)
        extractTitle(document, employee)
        extractSalary(document, employee)
        extractWorkExperience(document, employee)
        extractCity(document, employee)
        extractGitHub(document, employee)
        extractBirthday(document, employee)
        extractHeadHunterWorkType(document, employee)
        extractHeadHunterSourceUrl(document, employee)
        extractWorkPlaces(document, employee)

        return employee
    }

    private fun extractHeadHunterWorkType(document: Document, employee: Employee) {
        val resumeBlockPosition = getDataQaAttributeValue(document, "resume-block-position")
        if (resumeBlockPosition.isNotEmpty()) {
            val resumeBlockSpecialization = resumeBlockPosition[0].getElementsByClass("resume-block__specialization")
            if (resumeBlockSpecialization.isNotEmpty()) {
                employee.specialization = resumeBlockSpecialization.eachText().stream().reduce { t: String?, u: String? -> "$u||$t" }.get()
            }

            val aboutWork = resumeBlockPosition[0].getElementsByClass("bloko-gap bloko-gap_bottom")

            if (aboutWork.isNotEmpty()) {
                val aboutWorkParents = aboutWork.parents();

                if (aboutWorkParents.isNotEmpty()) {
                    val aboutWorkParentsElements = aboutWorkParents[0].getElementsByTag("p")

                    if (aboutWorkParentsElements.isNotEmpty()) {
                        val aboutWorkParentsElementsText = aboutWorkParentsElements.eachText()

                        aboutWorkParentsElementsText.forEach {
                            if (it.contains("Занятость:")) {
                                employee.employment = it.removePrefix("Занятость:")
                            }

                            if (it.contains("График работы:")) {
                                employee.workType = it.removePrefix("График работы:")
                            }
                        }
                    }

                }
            }

            val resumeBlockSpecializationCategory = getDataQaAttributeValue(resumeBlockPosition, "resume-block-specialization-category");
            if (resumeBlockSpecializationCategory.isNotEmpty()) {
                employee.workCategory = getDataQaAttributeValue(resumeBlockSpecializationCategory, "resume-block-specialization-category").text()
            }
        }
    }

    private fun extractHeadHunterSourceUrl(document: Document, employee: Employee) {
        employee.resumeLink = "https://hh.ru/resume" +
                document.baseUri()
                        .removePrefix(Paths.get(pathForResumeSave).toFile().absolutePath)
                        .removeSuffix(".html")
    }

    private fun extractBirthday(document: Document, employee: Employee) {
        val resumeAboutMe = getDataQaAttributeValue(document, "resume-personal-birthday")
        if (resumeAboutMe.isNotEmpty()) {
            employee.birthday = resumeAboutMe[0].text()
        }
    }

    private fun extractGitHub(document: Document, employee: Employee) {
        val resumeAboutMe = getDataQaAttributeValue(document, "resume-block-skills-content")
        if (resumeAboutMe.isNotEmpty()) {
            if (resumeAboutMe[0].text().contains("https://github.com")) {
                val githubLinkIndex = resumeAboutMe[0].text().indexOf("github.com")
                var githubLink =
                        "https://github.com/" +
                                resumeAboutMe[0].text().substring(githubLinkIndex, resumeAboutMe[0].text().length).split("/")[1]

                val cleanUrl = githubLink.split(" ")
                if (cleanUrl.isNotEmpty()) {
                    githubLink = cleanUrl[0]
                }

                employee.githubLink = githubLink
            }
        }
    }

    private fun extractWorkPlaces(document: Document, employee: Employee) {
        val workExperienceBlock = getDataQaAttributeValue(document, "resume-block-experience")
        if (workExperienceBlock.isNotEmpty()) {
            val workPlaceBlocks = workExperienceBlock[0].getElementsByClass("resume-block-container")
            if (workPlaceBlocks.isNotEmpty()) {
                var sequence = 1
                workPlaceBlocks.forEach {
                    val companyTitleBlock = it.getElementsByClass("resume-block__sub-title");
                    if (companyTitleBlock.isNotEmpty()) {
                        val companyName = companyTitleBlock[0].text()
                        val companyTimeToWork =
                                companyTitleBlock[0]
                                        .parent()
                                        .parent()
                                        .parent()
                                        .getElementsByClass("resume-block__experience-timeinterval").text()

                        val companyWork = EmployeeCompany()
                        companyWork.companyName = companyName;
                        companyWork.timeWork = companyTimeToWork
                        companyWork.employee = employee
                        companyWork.sequence = sequence++
                        companyWork.workPosition = getDataQaAttributeValue(it, "resume-block-experience-position").text()

                        val experienceBlock = it.getElementsByClass("resume__experience-url")
                        if (experienceBlock.isNotEmpty() && experienceBlock.parents().size > 0) {
                            val cityAndSite = experienceBlock.parents()[0].getElementsByTag("p")

                            if (cityAndSite.size > 0) {
                                val cityAndSiteSeparate = cityAndSite.text().split(",")

                                if (cityAndSiteSeparate.isNotEmpty()) {
                                    companyWork.city = cityAndSite.text().split(",")[0]
                                }

                                if (cityAndSiteSeparate.size > 1) {
                                    companyWork.site = cityAndSite.text().split(",")[1]
                                }
                            }
                        }

                        companyWork.timeWorkRange = it.parent().parent().getElementsByClass("bloko-column bloko-column_xs-4 bloko-column_s-2 bloko-column_m-2 bloko-column_l-2").text().removeSuffix(companyTimeToWork)
                        employee.companiesEmployee.add(companyWork)
                    }
                }
            }
        }
    }

    private fun extractSalary(document: Document, employee: Employee) {
        val salaryAndPositionNameBlock = getDataQaAttributeValue(document, "resume-block-position")
        if (salaryAndPositionNameBlock.isNotEmpty()) {
            val salaryPositionBlock = getDataQaAttributeValue(salaryAndPositionNameBlock, "resume-block-salary")
            if (salaryPositionBlock.isNotEmpty()) {
                employee.salary = salaryPositionBlock.eachText().single()
            }
        }
    }

    private fun extractMale(document: Document, employee: Employee) {
        val male = getDataQaAttributeValue(document, "resume-personal-gender")
        if (male.isNotEmpty()) {
            employee.male = male[0].text()
        }
    }

    private fun extractTitle(document: Document, employee: Employee) {
        val salaryAndPositionNameBlock = getDataQaAttributeValue(document, "resume-block-position")
        if (salaryAndPositionNameBlock.isNotEmpty()) {
            val resumePositionNameBlock = getDataQaAttributeValue(salaryAndPositionNameBlock, "resume-block-title-position")
            if (resumePositionNameBlock.isNotEmpty()) {
                employee.position = resumePositionNameBlock.eachText().single()
            }
        }
    }

    private fun extractCity(document: Document, employee: Employee) {
        val resumeCity = getDataQaAttributeValue(document, "resume-personal-address")
        if (resumeCity.isNotEmpty()) {
            employee.liveCity = resumeCity[0].text()
        }
    }

    private fun extractWorkExperience(document: Document, employee: Employee) {
        val resumeBlockExperience = document.getElementsByClass("resume-block__title-text resume-block__title-text_sub")
        if (resumeBlockExperience.isNotEmpty()) {
            employee.workExperience = resumeBlockExperience[0].text()
        }
    }

    private fun getDataQaAttributeValue(document: Document, attributeValue: String): Elements {
        return document.getElementsByAttributeValue("data-qa", attributeValue)
    }

    private fun getDataQaAttributeValue(elements: Elements, attributeValue: String): Elements {
        return elements[0].getElementsByAttributeValue("data-qa", attributeValue)
    }

    private fun getDataQaAttributeValue(element: Element, attributeValue: String): Elements {
        return element.getElementsByAttributeValue("data-qa", attributeValue)
    }
}