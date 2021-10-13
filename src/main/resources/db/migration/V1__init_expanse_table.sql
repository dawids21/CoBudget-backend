create table expense
(
    id          serial primary key,
    amount      integer,
    date        date,
    category    varchar,
    subcategory varchar
)