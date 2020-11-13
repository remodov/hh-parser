delete
from stage.resume_education
where 1 = 1
;

delete
from stage.resume_language
where 1 = 1
;

delete
from stage.resume_education
where 1 = 1
;

delete
from stage.resume_specialization
where 1 = 1
;

delete
from stage.resume_skills
where 1 = 1
;

delete
from stage.resume_work_type
where 1 = 1
;

delete
from stage.jobs_history
where 1 = 1
;

delete
from stage.resume
where 1 = 1
;

delete
from stage.position
where 1 = 1
;

delete
from stage.company
where 1 = 1
;

delete
from stage.person
where 1 = 1
;

delete
from stage.source
where 1 = 1
;

delete
from stage.sex
where 1 = 1
;

delete
from stage.work_category
where 1 = 1
;

delete
from stage.specialization
where 1 = 1
;

delete
from stage.employment
where 1 = 1
;

delete
from stage.work_type
where 1 = 1
;

delete
from stage.salary
where 1 = 1
;

delete
from stage.location
where 1 = 1
;

delete
from stage.skills
where 1 = 1
;

delete
from stage.language
where 1 = 1
;

delete
from stage.education
where 1 = 1
;

insert into stage.education (id, name)
values (-1, 'Не указано')
;

insert into stage.language (id, name)
values (-1, 'Не указано')
;

insert into stage.skills (id, name)
values (-1, 'Не указано')
;

insert into stage.location (id, name)
values (-1, 'Не указано')
;

insert into stage.salary (id, name)
values (-1, 'Не указано')
;

insert into stage.work_type (id, name)
values (-1, 'Не указано')
;

insert into stage.employment (id, name)
values (-1, 'Не указано')
;

insert into stage.specialization (id, name)
values (-1, 'Не указано')
;

insert into stage.work_category (id, name)
values (-1, 'Не указано')
;

insert into stage.sex (id, name)
values (-1, 'Не указано')
;

insert into stage.position (id, name)
values (-1, 'Не указано')
;

insert into stage.company (id, name, score)
values (-1, 'Не указано', -1)
;

insert into stage.source (id, name)
values (-1, 'Не указано')

;
select *
from buffer_hh_v1.hh_employee
;

DO
$$
    DECLARE
        l_person_id bigint;
        employee buffer_hh_v1.hh_employee%rowtype;
    BEGIN
        FOR employee IN
            SELECT * FROM buffer_hh_v1.hh_employee
            LOOP
                SELECT nextval('stage.resume_id_seq') INTO l_person_id;

                insert into stage.person(id, score, birthday, sex_id, location_id)
                values (l_person_id, -1, current_timestamp , -1 , -1);

                insert into stage.resume(position_id, work_category_id, employment_id, person_id, salary_id, source_id, external_id , source_url)
                values (-1, -1 , -1 ,l_person_id, -1, -1, '-1', 'none'||l_person_id);

            END LOOP;
        COMMIT;
    END
$$;

select *
from stage.resume
;

--CREATE EXTENSION IF NOT EXISTS pgcrypto;
--CREATE EXTENSION "uuid-ossp";

-- delete from public.jobs_history where 1 = 1
-- ;
--
-- delete from public.company where 1 = 1
-- ;
--
-- delete from public.person where 1 = 1
-- ;
-- GRANT ALL PRIVILEGES ON TABLE public.jobs_history  TO rvujlolxhxtcae;
-- GRANT ALL PRIVILEGES ON TABLE public.person  TO rvujlolxhxtcae;
-- GRANT ALL PRIVILEGES ON TABLE public.company  TO rvujlolxhxtcae;
--
-- insert into public.person (id, name)
-- select id, position
-- from buffer_hh_v1.hh_employee
-- where upper(position) like '%JAVA%'
--   and upper(position) not like '%JUNIOR%'
--   and upper(position) not like upper('%Стажер%')
--   and upper(position) not like upper('%Младший%')
-- ;
--
-- insert into public.company (id, name, score)
-- select distinct encode(digest(upper(trim(company_name)), 'MD5'), 'hex'), upper(trim(company_name)), 0
-- from buffer_hh_v1.hh_company_employee
-- where employee_id in (
--     select id
--     from buffer_hh_v1.hh_employee
--     where upper(position) like '%JAVA%'
--       and upper(position) not like '%JUNIOR%'
--       and upper(position) not like upper('%Стажер%')
--       and upper(position) not like upper('%Младший%')
-- )
-- ;
--
-- update buffer_hh_v1.hh_company_employee
-- set time_work_range = replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(trim(replace(time_work_range, 'по настоящее время', 'currently')), 'Декабрь', 'December'), 'Ноябрь', 'November'), 'Октябрь', 'October'), 'Сентябрь', 'September'), 'Август', 'August'), 'Июль', 'July'), 'Июнь', 'June'), 'Май', 'May'), 'Март', 'March'), 'Февраль', 'February'), 'Январь', 'January'), 'Апрель', 'April')
-- ;
--
-- insert into public.jobs_history (id, person_id, company_id , start_date , end_date , "current")
-- select  uuid_generate_v4() id
--      ,e.id person_id
--      ,encode(digest(upper(trim(company_name)), 'MD5'), 'hex') company_id
--      ,to_date(split_part(hce.time_work_range ,' — ', 1), 'Month YYYY') start_date
--      ,case when split_part(hce.time_work_range ,' — ', 2) = 'currently'
--                then CURRENT_DATE
--            else to_date(split_part(hce.time_work_range ,' — ', 2), 'Month YYYY') end end_date
--      ,split_part(hce.time_work_range ,' — ', 2) = 'currently' "current"
-- from buffer_hh_v1.hh_employee e inner join buffer_hh_v1.hh_company_employee hce
--                                            on e.id = hce.employee_id
-- where employee_id not in (
--     select distinct employee_id
--     from buffer_hh_v1.hh_company_employee
--     where time_work_range like '%С%' or  time_work_range like '%Г%'
--        or  time_work_range like '%->%'  or  time_work_range like '%Л%'
-- )
--   and upper(position) like '%JAVA%'
--   and upper(position) not like '%JUNIOR%'
--   and upper(position) not like upper('%Стажер%')
--   and upper(position) not like upper('%Младший%')
-- ;
--
-- update public.company C
-- set score = 0
-- where 1 = 1
-- ;
--
-- update public.company C
-- set score =
--         COALESCE((select (sum((end_date::date - start_date::date)/30) )/ count(distinct person_id)
--                   from jobs_history JH
--                   where  JH.company_id = C.id
--                  ),0)
-- where C.id in (
--     select company_id
--     from jobs_history
--     group by company_id
--     having count(distinct person_id) > 2
-- )
-- ;
--
-- commit;
--
-- update public.person P
-- set score = (
--     select sum(C.score * case when (end_date::date - start_date::date)/30 > 36 then 36 else  (end_date::date - start_date::date)/30 end)
--     from public.jobs_history JH inner join public.company C
--                                            on C.id = JH.company_id
--     where JH.person_id = P.id
-- )
-- where 1 = 1
-- ;
-- --------------------CHECK
-- select *
-- from public.company
-- order by score desc
-- ;
--
-- select *
-- from jobs_history
-- where company_id = '10e1ca17ededaa56f67dbcfaac462617'
-- ;
--
-- select *
-- from buffer_hh_v1.hh_employee
-- where id in (
--     select cast(person_id as BIGINT)
--     from jobs_history
--     where company_id = 'a87a5e3a2d0f97acf2e491d2ca8541ea'
-- )
-- ;
--
-- select *
-- from buffer_hh_v1.hh_employee
-- where id = '9807'
-- ;
--
-- select company_id
-- from jobs_history
-- group by company_id
-- having count(distinct person_id) > 10
-- ;
--
--
-- select count(*)
-- from buffer_hh_v1.hh_employee
-- where upper(position) like '%JAVA%'
--   and upper(position) not like '%JUNIOR%'
--   and upper(position) not like upper('%Стажер%')
--   and upper(position) not like upper('%Младший%')
-- ;
--
--
