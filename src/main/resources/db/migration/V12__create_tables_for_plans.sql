create table plan
(
    id      serial primary key,
    user_id varchar(20) not null,
    month   varchar     not null
);

create table planned_category
(
    plan         int not null
        references plan (id)
            on delete cascade,
    category_key int not null,
    amount       int not null
);




