--COMMON DICTIONARY
drop table if exists stage.resume_filter
;

drop table if exists stage.jobs_history
;

drop table if exists stage.dictionary_value
;

drop table if exists stage.resume
;

drop table if exists stage.position
;

drop table if exists stage.position_level
;

drop table if exists stage.company
;

drop table if exists stage.person
;

drop table if exists stage.source
;

drop table if exists stage.location
;

create table stage.position
(
    id    bigint primary key,
    title text not null UNIQUE
)
;

create table stage.position_level
(
    id   bigint primary key,
    name text not null UNIQUE
)
;

create table stage.company
(
    id    bigint primary key,
    name  text             not null UNIQUE,
    score double precision not null
)
;

create table stage.person
(
    id    bigint primary key,
    name  text             not null,
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
    id   bigint primary key,
    name text not null unique
)
;

create table stage.resume
(
    id                bigserial not null
        constraint pk_resume
            primary key,
    position_id       bigint
        constraint fk_resume_position
            references stage.position,
    position_level_id bigint
        constraint fk_resume_position_level
            references stage.position_level,
    person_id         bigint
        constraint fk_resume_person
            references stage.person,
    source_id         bigint
        constraint fk_resume_source
            references stage.source,
    location_id       bigint
        constraint fk_resume_location
            references stage.location,
    external_id       text      not null
);

CREATE INDEX idx_fk_resume_position ON stage.resume (position_id);
CREATE INDEX idx_fk_resume_position_level ON stage.resume (position_level_id);
CREATE INDEX idx_fk_resume_person ON stage.resume (person_id);
CREATE INDEX idx_fk_resume_source ON stage.resume (source_id);
CREATE INDEX idx_fk_resume_location ON stage.resume (location_id);

create table stage.jobs_history
(
    id                bigserial not null
        constraint pk_jobs_history
            primary key,
    position_id       bigint
        constraint fk_jobs_history_position
            references stage.position,
    position_level_id bigint
        constraint fk_jobs_history_position_level
            references stage.position_level,
    person_id         bigint
        constraint fk_jobs_history_person
            references stage.person,
    company_id        bigint
        constraint fk_jobs_history_company
            references stage.company,
    start_date        timestamp,
    end_date          timestamp,
    current           boolean   not null
);

CREATE INDEX idx_fk_jobs_history_position ON stage.jobs_history (position_id);
CREATE INDEX idx_fk_jobs_history_position_level ON stage.jobs_history (position_level_id);
CREATE INDEX idx_fk_jobs_history_person ON stage.jobs_history (person_id);
CREATE INDEX idx_fk_jobs_history_company ON stage.jobs_history (company_id);

drop table if exists stage.dictionary_type
;

create table stage.dictionary_type
(
    id   bigint primary key,
    name text not null UNIQUE
)
;

create table stage.dictionary_value
(
    id                 bigserial not null
        constraint pk_dictionary_value
            primary key,
    dictionary_type_id bigint
        constraint fk_dictionary_value_dictionary_type
            references stage.dictionary_type,
    value              text      not null
)
;

CREATE INDEX idx_fk_dictionary_value_dictionary_type ON stage.dictionary_value (dictionary_type_id);

create table stage.resume_filter
(
    id                bigserial not null
        constraint pk_resume_filter
            primary key,
    resume_id         bigint
        constraint fk_resume_filter_resume
            references stage.resume,
    dictionary_type_id bigint
        constraint fk_resume_filter_dictionary_type
            references stage.dictionary_type,
    dictionary_value_id         bigint
        constraint fk_resume_filter_dictionary_value
            references stage.dictionary_value
);

CREATE INDEX idx_fk_resume_filter_resume ON stage.resume_filter (resume_id);
CREATE INDEX idx_fk_resume_filter_dictionary_type ON stage.resume_filter (dictionary_type_id);
CREATE INDEX idx_fk_resume_filter_dictionary_value ON stage.resume_filter (dictionary_value_id);
