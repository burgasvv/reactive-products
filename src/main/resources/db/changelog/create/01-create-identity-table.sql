
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists identity (
    id uuid default gen_random_uuid() unique not null ,
    authority varchar(250) not null ,
    username varchar(250) unique not null ,
    password varchar(250) not null ,
    email varchar(250) unique not null ,
    enabled boolean not null ,
    created_at timestamp not null ,
    updated_at timestamp not null
)