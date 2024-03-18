create database shopapp;
use shopapp;

-- bang user
create table users(
    id int primary key auto_increment,
    fullname varchar(100) default '',
    phone_number varchar(10) not null ,
    address varchar(200) default '',
    password varchar(100) not null default '',
    create_at datetime,
    update_at datetime,
    is_active tinyint(1) default 1,
    date_of_birth date,
    facebook_account_id int default 0,
    google_account_id int default 0
);

alter table users
add column role_id int;

create table roles(
    id int primary key ,
    name varchar(20) not null
);

alter table users add foreign key (role_id) references tokens(id);

create table tokens(
    id int primary key auto_increment,
    token varchar(255) unique not null ,
    token_type varchar(50) not null ,
    expiration_date datetime,
    revoked tinyint(1) not null ,
    expired tinyint(1) not null ,
    user_id int,
    foreign key (user_id) references users(id)
);

create table social_accounts(
    id int primary key auto_increment,
    provider varchar(20) not null,
    provider_id varchar(50) not null,
    email varchar(150) not null ,
    name varchar(100) not null ,
    user_id int,
    foreign key (user_id) references users(id)
);

create table categories(
    id int primary key auto_increment,
    name varchar(100) not null default '' comment 'Ten danh muc, Vi du: Do dien tu'
);

create table products(
    id int primary key auto_increment,
    name varchar(350),
    price float not null check ( price >=0 ),
    thumbnail varchar(300) default '' comment 'Duong dan anh thumbnail',
    description longtext,
    create_at datetime,
    update_at datetime,
    category_id int,
    foreign key (category_id) references categories(id)
);

create table product_images(
    id int primary key auto_increment,
    product_id int,
    image_url varchar(300) not null,
    constraint fk_product_images_product
    foreign key (product_id) references products(id) on delete cascade
);

create table orders(
    id int primary key auto_increment,
    user_id int,
    foreign key (user_id) references users(id),
    fullname varchar(100) default '',
    email varchar(200) default '',
    phone_number varchar(20) not null ,
    address varchar(200) not null ,
    note varchar(100) default '',
    total_money float not null check ( total_money >=0 ),
    order_date datetime default CURRENT_TIMESTAMP,
    status varchar(20)
);
alter table orders add column `shipping_method` varchar(100);
alter table orders add column `shipping_address` varchar(200);
alter table orders add column `shipping_date` DATE;
alter table orders add column `payment_method` varchar(100);
alter table orders add column `tracking_number` varchar(100);
alter table orders add column `active` tinyint;
alter table orders modify column status enum('pending','processing','shipped','delivered','cancelled');


create table order_details(
    id int primary key auto_increment,
    order_id int,
    foreign key (order_id) references orders(id),
    product_id int,
    foreign key (product_id) references products(id),
    price float check(price >=0), 
    number_of_products int check(number_of_products > 0),
    total_money float check(total_money >=0),
    color varchar(20) default ''
)

