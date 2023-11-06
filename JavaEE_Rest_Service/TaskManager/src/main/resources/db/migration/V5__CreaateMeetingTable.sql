create table taskmanager.meeting
(
    id              bigint not null
        primary key,
    datemeeting     date,
    locationmeeting varchar(255)
);