--COMMON DICTIONARY
drop table if exists stage.resume_education
;

drop table if exists stage.resume_language
;

drop table if exists stage.resume_specialization
;

drop table if exists stage.resume_skills
;

drop table if exists stage.resume_work_type
;

drop table if exists stage.jobs_history
;

drop table if exists stage.resume
;

drop table if exists stage.position
;

drop table if exists stage.company
;

drop table if exists stage.person
;

drop table if exists stage.source
;

drop table if exists stage.sex
;

drop table if exists stage.work_category
;

drop table if exists stage.specialization
;

drop table if exists stage.employment
;

drop table if exists stage.work_type
;

drop table if exists stage.salary
;

drop table if exists stage.location
;

drop table if exists stage.skills
;

drop table if exists stage.language
;

drop table if exists stage.education
;

create table stage.skills
(
    id   bigserial primary key,
    name text not null UNIQUE
)
;

create table stage.language
(
    id   bigserial primary key,
    name text not null UNIQUE
)
;

create table stage.education
(
    id   bigserial primary key,
    name text not null UNIQUE
)
;

create table stage.sex
(
    id   bigint primary key,
    name text not null UNIQUE
)
;

create table stage.work_category
(
    id   bigserial primary key,
    name text not null UNIQUE
)
;

create table stage.specialization
(
    id   bigserial primary key,
    name text not null UNIQUE
)
;

create table stage.employment
(
    id   bigint primary key,
    name text not null UNIQUE
)
;

create table stage.work_type
(
    id   bigint primary key,
    name text not null UNIQUE
)
;

create table stage.salary
(
    id   bigint primary key,
    name text not null UNIQUE
)
;

create table stage.company
(
    id    bigserial primary key,
    name  text             not null UNIQUE,
    score double precision not null
)
;

create table stage.source
(
    id   bigint primary key,
    name text not null
)
;

create table stage.location
(
    id   bigserial primary key,
    name text not null unique
)
;

create table stage.position
(
    id   bigserial primary key,
    name text not null unique
)
;

create table stage.person
(
    id          bigserial primary key,
    score       double precision not null,
    birthday    date             not null,
    sex_id      bigint
        constraint fk_person_sex
            references stage.sex,
    location_id bigint
        constraint fk_person_location
            references stage.location

)
;

CREATE INDEX idx_fk_person_sex ON stage.person (sex_id);
CREATE INDEX idx_fk_person_location ON stage.person (location_id);

create table stage.resume
(
    id               bigserial not null
        constraint pk_resume
            primary key,
    position_id      bigint
        constraint fk_resume_position
            references stage.position,
    work_category_id bigint
        constraint fk_resume_work_category
            references stage.work_category,
    employment_id bigint
        constraint fk_resume_employment
            references stage.employment,
    person_id        bigint
        constraint fk_resume_person
            references stage.person,
    salary_id        bigint
        constraint fk_resume_salary
            references stage.salary,
    source_id        bigint
        constraint fk_resume_source
            references stage.source,
    external_id      text      not null,
    source_url       text      not null unique
);

CREATE INDEX idx_fk_resume_position ON stage.resume (position_id);
CREATE INDEX idx_fk_resume_person ON stage.resume (person_id);
CREATE INDEX idx_fk_resume_source ON stage.resume (source_id);
CREATE INDEX idx_fk_resume_salary ON stage.resume (salary_id);
CREATE INDEX idx_fk_resume_work_category ON stage.resume (work_category_id);
CREATE INDEX idx_fk_resume_employment ON stage.resume (employment_id);

create table stage.resume_language
(
    id           bigint primary key,
    resume_id    bigint
        constraint fk_resume_language_resume
            references stage.resume,
    language_id bigint
        constraint fk_resume_language_language
            references stage.language
)
;

CREATE INDEX idx_fk_resume_language_resume ON stage.resume_language (resume_id);
CREATE INDEX idx_fk_resume_language_language ON stage.resume_language (resume_id);

create table stage.resume_skills
(
    id           bigint primary key,
    resume_id    bigint
        constraint fk_resume_skills_resume
            references stage.resume,
    skill_id bigint
        constraint fk_resume_skills_skills
            references stage.skills
)
;

CREATE INDEX idx_fk_resume_skills_resume ON stage.resume_skills (resume_id);
CREATE INDEX idx_fk_resume_skills_skills ON stage.resume_skills (skill_id);

create table stage.resume_education
(
    id           bigint primary key,
    resume_id    bigint
        constraint fk_resume_education_resume
            references stage.resume,
    work_type_id bigint
        constraint fk_resume_education_education
            references stage.education
)
;

CREATE INDEX idx_fk_resume_education_resume ON stage.resume_education (resume_id);
CREATE INDEX idx_fk_resume_education_education ON stage.resume_education (work_type_id);

create table stage.resume_work_type
(
    id           bigint primary key,
    resume_id    bigint
        constraint fk_resume_work_type_resume
            references stage.resume,
    work_type_id bigint
        constraint fk_resume_work_type_work_type
            references stage.work_type
)
;

CREATE INDEX idx_fk_resume_work_type_resume ON stage.resume_work_type (resume_id);
CREATE INDEX idx_fk_resume_work_type_work_type ON stage.resume_work_type (work_type_id);

create table stage.resume_specialization
(
    id                bigint primary key,
    resume_id         bigint
        constraint fk_resume_specialization_person
            references stage.resume,
    specialization_id bigint
        constraint fk_resume_specialization_specialization
            references stage.specialization
)
;

CREATE INDEX idx_fk_resume_specialization_person ON stage.resume_specialization (resume_id);
CREATE INDEX idx_fk_resume_specialization_specialization ON stage.resume_specialization (specialization_id);

create table stage.jobs_history
(
    id          bigserial not null
        constraint pk_jobs_history
            primary key,
    position_id bigint
        constraint fk_jobs_history_position
            references stage.position,
    location_id bigint
        constraint fk_jobs_history_location
            references stage.location,
    resume_id   bigint
        constraint fk_jobs_history_resume
            references stage.resume,
    company_id  bigint
        constraint fk_jobs_history_company
            references stage.company,
    start_date  timestamp,
    end_date    timestamp,
    current     boolean   not null
);

CREATE INDEX idx_fk_jobs_history_position ON stage.jobs_history (position_id);
CREATE INDEX idx_fk_jobs_history_location ON stage.jobs_history (location_id);
CREATE INDEX idx_fk_jobs_history_resume ON stage.jobs_history (resume_id);
CREATE INDEX idx_fk_jobs_history_company ON stage.jobs_history (company_id);