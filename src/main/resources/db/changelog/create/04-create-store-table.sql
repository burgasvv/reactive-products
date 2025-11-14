
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists store (
    id uuid default gen_random_uuid() unique not null ,
    name varchar(250) not null ,
    address text not null ,
    created_at timestamp not null ,
    updated_at timestamp not null
)