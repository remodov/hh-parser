package com.example.demo.model

data class Employee(
        var workExperience: String? = null,
        var male: String? = null,
        var liveCity: String? = null,
        var position: String? = null,
        var salary: String? = null,
        var headHunterResumeLink: String? = null,
        var companiesWork: MutableList<CompanyWork> = ArrayList()
)

data class CompanyWork(
        var companyName: String? = null,
        var startWork: String? = null,
        var endWork: String? = null,
        var timeWork: String? = null,
        var position: String? = null,
        var workDescription: String? = null
)