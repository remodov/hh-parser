--CREATE EXTENSION IF NOT EXISTS pgcrypto;
--CREATE EXTENSION "uuid-ossp";

delete from jobs_history
;

delete from public.company
;

delete from person
;

insert into public.company (id, name, score)
select distinct encode(digest(company_name, 'MD5'), 'hex'),company_name, 0
from buffer_hh_v1.hh_company_employee
;

insert into person (id, name)
select id, position
from buffer_hh_v1.hh_employee
;

update buffer_hh_v1.hh_company_employee
set time_work_range = replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(trim(replace(time_work_range, 'по настоящее время', 'currently')), 'Декабрь', 'December'), 'Ноябрь', 'November'), 'Октябрь', 'October'), 'Сентябрь', 'September'), 'Август', 'August'), 'Июль', 'July'), 'Июнь', 'June'), 'Май', 'May'), 'Март', 'March'), 'Февраль', 'February'), 'Январь', 'January'), 'Апрель', 'April')
;

insert into jobs_history (id, person_id, company_id , start_date , end_date , "current")
select  uuid_generate_v4() id
       ,e.id person_id
       ,encode(digest(company_name, 'MD5'), 'hex') company_id
       ,to_date(split_part(hce.time_work_range ,' — ', 1), 'Month YYYY') start_date
       ,case when split_part(hce.time_work_range ,' — ', 2) = 'currently'
       then CURRENT_DATE
       else to_date(split_part(hce.time_work_range ,' — ', 2), 'Month YYYY') end end_date
       ,split_part(hce.time_work_range ,' — ', 2) = 'currently' "current"
from buffer_hh_v1.hh_employee e inner join buffer_hh_v1.hh_company_employee hce
		on e.id = hce.employee_id
;

update public.company C
set score =
(select  count(distinct person_id)/(sum((end_date::date - start_date::date)/30) + 0.1)
from jobs_history JH
where  JH.company_id = C.id
)

commit;

select *
from public.company
order by score  desc

