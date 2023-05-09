
create table point
(
    id      bigint primary key auto_increment,
    user_id bigint  not null,
    x       real    not null check ( x >= -5.0 and x <= 3.0 ),
    y       real    not null check ( y >= -5.0 and y <= 3.0 ),
    r       real    not null check ( r >= -5.0 and r <= 3.0 ),
    hit     boolean not null,
    time    text    not null,
    foreign key (user_id) references user (id) on delete cascade
);
