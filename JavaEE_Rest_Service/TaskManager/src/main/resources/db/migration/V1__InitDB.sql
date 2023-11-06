create schema if not exists taskmanager;

create table if not exists taskmanager.users
(
    id       bigint not null
        primary key,
    email    varchar(255),
    username varchar(255)
);

create table if not exists taskmanager.project
(
    id          bigint not null
        primary key,
    description varchar(255),
    name        varchar(255)
);

create table if not exists taskmanager.task
(
    id          bigint not null
        primary key,
    deadline    date,
    description varchar(255),
    status      varchar(255),
    title       varchar(255),
    project_id  bigint
        constraint fkk8qrwowg31kx7hp93sru1pdqa
            references taskmanager.project on delete cascade
);


create table taskmanager.usertask
(
    id      bigint not null
        primary key,
    task_id bigint
        constraint fk1kqdxo98ldhtsk8yepccmqjck
            references taskmanager.task on delete cascade,
    user_id bigint
        constraint fk4id53d8cqlg1330d2cxf9vnh4
            references taskmanager.users on delete cascade
);