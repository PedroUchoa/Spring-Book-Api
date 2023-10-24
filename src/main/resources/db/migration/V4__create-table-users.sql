create table users(
    id varchar(255) not null UNIQUE,
    login varchar(100) not null unique,
    password varchar(255) not null,
    name varchar(255) not null,
    role text not null,

    primary key(id)

);
    