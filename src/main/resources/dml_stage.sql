--COMMON DICTIONARY
drop table stage.position
;

create table stage.sex (
 id integer,
 name text,
 primary key (id)
)
;

insert into stage.sex (id, name) values (1, 'Мужчина');
insert into stage.sex (id, name) values (2, 'Женщина');
insert into stage.sex (id, name) values (3, 'Пол не указан');
insert into stage.sex (id, name) values (99, 'Не удалось определить');

--dictionary
work_experience
sex
city
position
position_level
salary


create table stage.company
(
    id varchar(500) not null
        constraint pk_company
            primary key,
    name varchar(500) not null,
    score double precision not null
);

alter table stage.company owner to rvujlolxhxtcae;

create table stage.person
(
    id varchar(255) not null
        constraint pk_person
            primary key,
    name varchar(255) not null,
    score double precision
);

alter table stage.person owner to rvujlolxhxtcae;

create table stage.jobs_history
(
    id varchar(255) not null
        constraint pk_jobs_history
            primary key,
    person_id varchar(255)
        constraint fk_person
            references stage.person,
    company_id varchar(255)
        constraint fk_company
            references stage.company,
    start_date timestamp,
    end_date timestamp,
    current boolean not null
);

alter table stage.jobs_history owner to rvujlolxhxtcae;


select distinct male
from buffer_hh_v1.hh_employee
;

select *
from stage.sex