create table fault
(
    id              bigserial    not null,
    created_at      timestamp,
    updated_at      timestamp,
    uuid            varchar(255),
    version         int8         not null,
    fault_describe  varchar(255),
    status          varchar(255) not null,
    area_id         int8         not null,
    specialist_id   int8,
    who_assigned_id int8,
    who_notify_id   int8         not null,
    primary key (id)
);
create table tech_area
(
    id         bigserial not null,
    created_at timestamp,
    updated_at timestamp,
    uuid       varchar(255),
    version    int8      not null,
    area_name  varchar(255),
    primary key (id)
);
create table users
(
    id              bigserial not null,
    created_at      timestamp,
    updated_at      timestamp,
    uuid            varchar(255),
    version         int8      not null,
    email_user_name varchar(255),
    first_name      varchar(255),
    last_name       varchar(255),
    password        varchar(255),
    phone           varchar(255),
    role            varchar(255),
    primary key (id)
);
alter table tech_area
    add constraint UK_4jqv42pt6omr39kaf888o2ylq unique (area_name);
alter table users
    add constraint UK_4q115rdsc6i9q69fubixr245n unique (email_user_name);
alter table fault
    add constraint FKnc3j8y28dr3a2hg23b7m2jvtq foreign key (area_id) references tech_area;
alter table fault
    add constraint FKox31t774m8mx9vml4pl58dg4e foreign key (specialist_id) references users;
alter table fault
    add constraint FKt2b3v03b2kwrv4r1b1y6crxwm foreign key (who_assigned_id) references users;
alter table fault
    add constraint FKlofff968a63fm050blr65ywnn foreign key (who_notify_id) references users;
