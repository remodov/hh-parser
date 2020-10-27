package com.example.demo.model

import javax.persistence.*

@Entity
@Table(name = "parser_url")
class ParserUrls(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int,
        val source: String,
        val url: String
)