
--liquibase formatted sql

--changeset burgasvv:1
begin ;
insert into category(name, description, created_at, updated_at)
values ('Молочные продукты','Описание категории Молочные продукты',
        '2025-11-13 15:30:00','2025-11-13 15:30:00');
insert into category(name, description, created_at, updated_at)
values ('Выпечка','Описание категории Выпечка',
        '2025-11-13 15:30:00','2025-11-13 15:30:00');
insert into category(name, description, created_at, updated_at)
values ('Фрукты','Описание категории Фрукты',
        '2025-11-13 15:30:00','2025-11-13 15:30:00');
insert into category(name, description, created_at, updated_at)
values ('Овощи','Описание категории Овощи',
        '2025-11-13 15:30:00','2025-11-13 15:30:00');
commit ;