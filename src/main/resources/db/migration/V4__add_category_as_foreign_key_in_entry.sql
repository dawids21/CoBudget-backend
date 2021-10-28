alter table entry
    alter column category type int
        using null;


alter table entry
    add constraint entry_category_id_fk
        foreign key (category) references category;
