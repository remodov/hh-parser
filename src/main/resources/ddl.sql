drop table parser_url
;

create table parser_url (
id BIGSERIAL,
source text,
url text
)
;

insert into buffer_hh_v1.parser_url (source, url)
SELECT 'hh.ru', 'https://mytischi.hh.ru/search/resume?clusters=true&exp_period=all_time&logic=normal&no_magic=false&order_by=relevance&pos=full_text&text=java+developer&area='|| generate_series
FROM generate_series(1, 6347);

drop table hh_employee
;

CREATE TABLE hh_employee (
	id BIGSERIAL,
	work_experience text,
	male text,
	live_city text,
	position text,
	salary text,
	resume_link text,
	PRIMARY KEY(id)
)
;

drop table hh_company_employee
;

CREATE TABLE hh_company_employee (
	id BIGSERIAL,
	employee_id bigint,
	company_name text,
	time_work text,
	time_work_range text,
	sequence int,
	PRIMARY KEY(id),
	CONSTRAINT fk_employee
      FOREIGN KEY(employee_id)
	  REFERENCES hh_employee(id)
)
;