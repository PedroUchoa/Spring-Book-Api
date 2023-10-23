create table books(
    id varchar(255) not null UNIQUE,
    name varchar(100) not null unique,
    author varchar(100) not null,
    description text(16380) not null,
    image text(16380) not null,
    PRIMARY KEY (id)
);


