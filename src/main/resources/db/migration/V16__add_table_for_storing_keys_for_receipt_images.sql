create table receipt_image
(
    id          serial primary key,
    user_id     varchar(100),
    key         varchar(100) not null,
    submit_date timestamp with time zone
)