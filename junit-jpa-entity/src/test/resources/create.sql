-- book
drop table if exists `book`;
create table book (
  id bigint(20) unsigned not null auto_increment,
  title varchar(50) not null,
  author varchar(20) not null,
  primary key (id)
);