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
	PRIMARY KEY(id),
	CONSTRAINT fk_employee
      FOREIGN KEY(employee_id)
	  REFERENCES hh_employee(id)
)
;