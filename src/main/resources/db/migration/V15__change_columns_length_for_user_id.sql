alter table plan
    alter column user_id type varchar(100) using user_id::varchar(100);
alter table entry
    alter column user_id type varchar(100) using user_id::varchar(100);
alter table category
    alter column user_id type varchar(100) using user_id::varchar(100);
