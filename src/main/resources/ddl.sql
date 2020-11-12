drop table buffer_hh_v1.hh_employee
;

CREATE TABLE buffer_hh_v1.hh_employee
(
    id              BIGSERIAL,
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
    PRIMARY KEY (id)
)
;

drop table buffer_hh_v1.hh_company_employee
;

CREATE TABLE buffer_hh_v1.hh_company_employee
(
    id              BIGSERIAL,
    employee_id     bigint,
    company_name    text,
    time_work       text,
    time_work_range text,
    sequence        int,
    city            text,
    site            text,
    work_position   text,
    PRIMARY KEY (id),
    CONSTRAINT fk_employee
        FOREIGN KEY (employee_id)
            REFERENCES buffer_hh_v1.hh_employee (id)
)
;

drop table core_parser_url
;

create table core_parser_url
(
    id     BIGSERIAL,
    source text,
    url    text
)
;

delete
from public.core_parser_url
:
insert into core_parser_url (source, url)
SELECT 'hh.ru',
       'https://mytischi.hh.ru/search/resume?clusters=true&exp_period=all_time&logic=normal&no_magic=false&order_by=relevance&pos=full_text&text=java+developer&area=' ||
       generate_series
FROM generate_series(1, 6347);


https://mytischi.hh.ru/search/resume?area=1&clusters=true&exp_period=all_time&logic=normal&no_magic=false&order_by=relevance&pos=full_text&text=java+developer&skill=3093

create table CORE_SOURCE
(
    id     BIGSERIAL,
    source text,
    url    text,
    PRIMARY KEY (id)
)
;

insert into CORE_SOURCE(source, url)
values ('HH', 'https://hh.ru')
;

create table CORE_LOAD_TASK
(
    id                   NUMBER not null,
    load_session_type_id NUMBER not null,
    load_task_status     VARCHAR2(200),
    task_create_date     DATE   not null,
    note                 VARCHAR2(4000)
)
;
comment on column EXT_DATA.EXT_LOAD_TASK.load_task_status
    is 'START
RUNNING
STOP
DONE';
alter table EXT_DATA.EXT_LOAD_TASK
    add constraint EXT_LOAD_TASK_PK primary key (ID);
alter table EXT_DATA.EXT_LOAD_TASK
    add constraint EXT_LOAD_TASK_FK1 foreign key (LOAD_SESSION_TYPE_ID)
        references EXT_DATA.EXT_LOAD_SESSION_TYPE (ID);


create table EXT_DATA.EXT_LOAD_SESSION
(
    id                      NUMBER  not null,
    load_session_type_id    NUMBER  not null,
    load_task_id            NUMBER  not null,
    load_session_res        VARCHAR2(200),
    task_company_count      INTEGER not null,
    requested_company_count INTEGER,
    error_string            VARCHAR2(4000),
    start_time              DATE    not null,
    finish_time             DATE,
    note                    VARCHAR2(4000),
    loaded_documents        NUMBER,
    errors_documents        NUMBER,
)
;
comment on column EXT_DATA.EXT_LOAD_SESSION.load_session_res
    is 'RUNNING, SUCCESS eee ERROR';
alter table EXT_DATA.EXT_LOAD_SESSION
    add constraint EXT_LOAD_SESSION_PK primary key (ID);
alter table EXT_DATA.EXT_LOAD_SESSION
    add constraint EXT_LOAD_SESSION_FK1 foreign key (LOAD_SESSION_TYPE_ID)
        references EXT_DATA.EXT_LOAD_SESSION_TYPE (ID);
alter table EXT_DATA.EXT_LOAD_SESSION
    add constraint EXT_LOAD_SESSION_FK2 foreign key (LOAD_TASK_ID)
        references EXT_DATA.EXT_LOAD_TASK (ID);

