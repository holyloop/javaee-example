insert into user (id, username, password, salt) values (1, 'root', 'BiwGS1zD2dpXKVIlsodlUcQKgryR0wUP46RNpbQzNXk=', 'IDX+pnHqyU6lql0hERphhg==');
insert into user (id, username, password, salt) values (2, 'guest', '63J/7bQzg9eWSCYP/SxkRMyj0Vsjb+lf7kMQZxO0uBA=', 'xoExnX1OzPK7tMaRDCHY7A==');

insert into role (id, role_name) values (1, 'admin');
insert into role (id, role_name) values (2, 'guest');

insert into user_role_rel (id, user_id, role_id) values (1, 1, 1);
insert into user_role_rel (id, user_id, role_id) values (2, 2, 2);

insert into permission (id, permission_str) values (1, 'user:insert');
insert into permission (id, permission_str) values (2, 'user:delete');
insert into permission (id, permission_str) values (3, 'user:update');
insert into permission (id, permission_str) values (4, 'user:select');

insert into role_permission_rel (id, role_id, permission_id) values (1, 1, 1);
insert into role_permission_rel (id, role_id, permission_id) values (2, 1, 2);
insert into role_permission_rel (id, role_id, permission_id) values (3, 1, 3);
insert into role_permission_rel (id, role_id, permission_id) values (4, 1, 4);
