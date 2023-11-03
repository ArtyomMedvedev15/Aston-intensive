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
            references taskmanager.project
);

create table if not exists taskmanager.user_task
(
    user_id bigint not null
        constraint fkj6lai3y87ttxldkysg1549etg
            references taskmanager.users,
    task_id bigint not null
        constraint fkvs34bjkmpbk2e54qlrol3ilt
            references taskmanager.task,
    primary key (user_id, task_id)
);