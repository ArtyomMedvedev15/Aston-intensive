create table if not exists taskmanager.bug
(
    id    bigint not null
        primary key,
    title varchar(255),
    type  varchar(255)
);
