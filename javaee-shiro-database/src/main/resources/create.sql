drop table if exists `user`;
create table user (
  id bigint(20) unsigned not null auto_increment,
  username varchar(30) not null,
  password char(44) not null,
  salt char(24) not null,
  primary key (id),
  unique key uk_username (username)
);

drop table if exists `role`;
create table role (
  id bigint(20) unsigned not null auto_increment,
  role_name varchar(30) not null,
  primary key (id)
);

drop table if exists `permission`;
create table permission (
  id bigint(20) unsigned not null auto_increment,
  permission_str varchar(100) not null,
  primary key (id)
);

drop table if exists `user_role_rel`;
create table user_role_rel (
  id bigint(20) unsigned not null auto_increment,
  user_id bigint(20) unsigned not null,
  role_id bigint(20) unsigned not null,
  primary key (id)
);

drop table if exists `role_permission_rel`;
create table role_permission_rel (
  id bigint(20) unsigned not null auto_increment,
  role_id bigint(20) unsigned not null,
  permission_id bigint(20) unsigned not null,
  primary key (id)
);
