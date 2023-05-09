create table user
(
    id            bigint primary key auto_increment,
    username      text not null check ( username != '' ),
    password      text not null check ( password != '' ),
    refresh_token text
);

