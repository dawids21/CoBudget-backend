alter table plan
    rename month to yearAndMonth;

alter table plan
    alter column yearAndMonth type date;