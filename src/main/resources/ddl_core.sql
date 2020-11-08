drop table core_parser_url
;

create table core_parser_url (
    id BIGSERIAL,
    source text,
    url text
)
;

insert into core_parser_url (source, url)
SELECT 'hh.ru', 'https://mytischi.hh.ru/search/resume?clusters=true&exp_period=all_time&logic=normal&no_magic=false&order_by=relevance&pos=full_text&text=java+developer&area='|| generate_series
FROM generate_series(1, 6347);


create table core_source
(
    id BIGSERIAL,
    source text,
    url text,
    PRIMARY KEY(id)
)
;

insert into CORE_SOURCE(source, url)
values ('HH','https://hh.ru')
;

create table EXT_LOAD_TASK_TYPE
(
    id  BIGSERIAL,
    name text,
    code text
)
;



create table CORE_LOAD_TASK
(
  id                   bigserial,
  load_session_type_id NUMBER not null,
  load_task_status     VARCHAR2(200),
  task_create_date     DATE not null,
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
  id                          NUMBER not null,
  load_session_type_id        NUMBER not null,
  load_task_id                NUMBER not null,
  load_session_res            VARCHAR2(200),
  task_company_count          INTEGER not null,
  requested_company_count     INTEGER,
  error_string                VARCHAR2(4000),
  start_time                  DATE not null,
  finish_time                 DATE,
  note                        VARCHAR2(4000),
  loaded_documents            NUMBER,
  errors_documents            NUMBER,
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

