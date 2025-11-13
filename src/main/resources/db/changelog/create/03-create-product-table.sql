
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists product (
    id uuid default gen_random_uuid() unique not null ,
    category_id uuid references category(id) on delete set null on update cascade ,
    name varchar(250) unique not null ,
    description text unique not null ,
    price decimal not null default 0.0 check ( price >= 0 ) ,
    created_at timestamp not null ,
    updated_at timestamp not null
)