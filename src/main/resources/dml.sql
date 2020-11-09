--CREATE EXTENSION IF NOT EXISTS pgcrypto;
--CREATE EXTENSION "uuid-ossp";

delete from public.jobs_history where 1 = 1
;

delete from public.company where 1 = 1
;

delete from public.person where 1 = 1
;
GRANT ALL PRIVILEGES ON TABLE public.jobs_history  TO rvujlolxhxtcae;
GRANT ALL PRIVILEGES ON TABLE public.person  TO rvujlolxhxtcae;
GRANT ALL PRIVILEGES ON TABLE public.company  TO rvujlolxhxtcae;

insert into public.person (id, name)
select id, position
from buffer_hh_v1.hh_employee
where upper(position) like '%JAVA%'
  and upper(position) not like '%JUNIOR%'
  and upper(position) not like upper('%Стажер%')
  and upper(position) not like upper('%Младший%')
;

insert into public.company (id, name, score)
select distinct encode(digest(upper(trim(company_name)), 'MD5'), 'hex'), upper(trim(company_name)), 0
from buffer_hh_v1.hh_company_employee
where employee_id in (
    select id
    from buffer_hh_v1.hh_employee
    where upper(position) like '%JAVA%'
      and upper(position) not like '%JUNIOR%'
      and upper(position) not like upper('%Стажер%')
      and upper(position) not like upper('%Младший%')
)
;

update buffer_hh_v1.hh_company_employee
set time_work_range = replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(trim(replace(time_work_range, 'по настоящее время', 'currently')), 'Декабрь', 'December'), 'Ноябрь', 'November'), 'Октябрь', 'October'), 'Сентябрь', 'September'), 'Август', 'August'), 'Июль', 'July'), 'Июнь', 'June'), 'Май', 'May'), 'Март', 'March'), 'Февраль', 'February'), 'Январь', 'January'), 'Апрель', 'April')
;

insert into public.jobs_history (id, person_id, company_id , start_date , end_date , "current")
select  uuid_generate_v4() id
     ,e.id person_id
     ,encode(digest(upper(trim(company_name)), 'MD5'), 'hex') company_id
     ,to_date(split_part(hce.time_work_range ,' — ', 1), 'Month YYYY') start_date
     ,case when split_part(hce.time_work_range ,' — ', 2) = 'currently'
               then CURRENT_DATE
           else to_date(split_part(hce.time_work_range ,' — ', 2), 'Month YYYY') end end_date
     ,split_part(hce.time_work_range ,' — ', 2) = 'currently' "current"
from buffer_hh_v1.hh_employee e inner join buffer_hh_v1.hh_company_employee hce
                                           on e.id = hce.employee_id
where employee_id not in (
    select distinct employee_id
    from buffer_hh_v1.hh_company_employee
    where time_work_range like '%С%' or  time_work_range like '%Г%'
       or  time_work_range like '%->%'  or  time_work_range like '%Л%'
)
  and upper(position) like '%JAVA%'
  and upper(position) not like '%JUNIOR%'
  and upper(position) not like upper('%Стажер%')
  and upper(position) not like upper('%Младший%')
;

update public.company C
set score = 0
where 1 = 1
;

update public.company C
set score =
        COALESCE((select (sum((end_date::date - start_date::date)/30) )/ count(distinct person_id)
                  from jobs_history JH
                  where  JH.company_id = C.id
                 ),0)
where C.id in (
    select company_id
    from jobs_history
    group by company_id
    having count(distinct person_id) > 2
)
;

commit;

update public.person P
set score = (
    select sum(C.score * case when (end_date::date - start_date::date)/30 > 36 then 36 else  (end_date::date - start_date::date)/30 end)
    from public.jobs_history JH inner join public.company C
                                           on C.id = JH.company_id
    where JH.person_id = P.id
)
where 1 = 1
;
--------------------CHECK
select *
from public.company
order by score desc
;

select *
from jobs_history
where company_id = '10e1ca17ededaa56f67dbcfaac462617'
;

select *
from buffer_hh_v1.hh_employee
where id in (
    select cast(person_id as BIGINT)
    from jobs_history
    where company_id = 'a87a5e3a2d0f97acf2e491d2ca8541ea'
)
;

select *
from buffer_hh_v1.hh_employee
where id = '9807'
;

select company_id
from jobs_history
group by company_id
having count(distinct person_id) > 10
;


select count(*)
from buffer_hh_v1.hh_employee
where upper(position) like '%JAVA%'
  and upper(position) not like '%JUNIOR%'
  and upper(position) not like upper('%Стажер%')
  and upper(position) not like upper('%Младший%')
;


