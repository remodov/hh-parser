package com.example.demo.model

data class Employee(
        val workExperience: String?,
        val male: String,
        val liveCity: String,
        val position: String,
        val salary: String?,
        val headHunterResumeLink: String,
        val companiesWork: List<CompanyWork>
)

data class CompanyWork(
        val companyName: String,
        val startWork: String,
        val endWork: String,
        val timeWork: String,
        val position: String,
        val workDescription: String
)