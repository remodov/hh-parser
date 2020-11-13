drop table if exists buffer.hh_company_employee
;

drop table if exists  buffer.hh_employee
;

drop table if exists buffer.core_parser_url
;

create table buffer.hh_employee
(
    id              bigserial,
    work_experience text,
    male            text,
    live_city       text,
    position        text,
    salary          text,
    resume_link     text,
    birthday        text,
    github_link     text,
    work_type       text,
    employment      text,
    work_category   text,
    specialization  text,
    primary key (id)
)
;

create table buffer.hh_company_employee
(
    id              bigserial,
    employee_id     bigint,
    company_name    text,
    time_work       text,
    time_work_range text,
    sequence        int,
    city            text,
    site            text,
    work_position   text,
    primary key (id),
    constraint fk_employee
        foreign key (employee_id)
            references buffer_hh_v1.hh_employee (id)
)
;

create table buffer.core_parser_url
(
    id     BIGSERIAL,
    source text,
    url    text
)
;