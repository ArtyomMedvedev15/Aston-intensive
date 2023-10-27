create schema if not exists taskmaneger;

create table if not exists taskmaneger.users
(
    id serial primary key,
    username varchar(256) not null,
    email varchar(256) not null
    );
create table if not exists taskmaneger.project(
                                                  id serial primary key,
                                                  name varchar(256) not null,
    description varchar(512) not null
    );
create table if not exists taskmaneger.task
(
    id serial primary key,
    title varchar(256) not null,
    description varchar(256) not null,
    deadline date not null,
    status varchar(128) not null,
    projectId int references taskmaneger.project(id)
    );
create table if not exists taskmaneger.usertask
(
    id serial primary key,
    userid int references taskmaneger.users(id) on delete cascade,
    taskid int references taskmaneger.task(id) on delete cascade
    );