drop table if exists Product cascade;
drop sequence if exists Product_SEQ;
drop extension if exists vector;

create extension vector;

create table Product (price float(53) not null, id bigint not null, description varchar(65535), name varchar(65535), embedded vector(384), primary key (id));

create sequence Product_SEQ start with 1 increment by 50;

------



