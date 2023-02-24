create table feature_toggle
(
    id      serial primary key,
    name    varchar(50) not null,
    enabled bool        not null
);