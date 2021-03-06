package com.example.demo.model

import jdk.nashorn.internal.runtime.Specialization
import javax.persistence.*

@Entity
@Table(name = "hh_employee")
class Employee(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,

        var workExperience: String? = null,

        var male: String? = null,

        var liveCity: String? = null,

        var position: String? = null,

        var salary: String? = null,

        var resumeLink: String? = null,

        var birthday: String? = null,

        var githubLink: String? = null,

        var workType: String? = null,

        var employment: String? = null,

        var workCategory: String? = null,

        var specialization: String? = null,

        @OneToMany(mappedBy="employee", cascade = [CascadeType.ALL])
        var companiesEmployee: MutableList<EmployeeCompany> = ArrayList()
)

@Entity
@Table(name = "hh_company_employee")
class EmployeeCompany(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,

        var companyName: String? = null,

        var timeWork: String? = null,

        var sequence: Int? = null,

        var timeWorkRange: String? = null,

        var city: String ? = null,

        var site: String ? = null,

        var workPosition: String ? = null,

        @ManyToOne
        @JoinColumn(name = "employee_id", referencedColumnName = "id")
        var employee: Employee? = null
)