DROP TABLE IF EXISTS mon_ability;
DROP TABLE IF EXISTS monster;
create TABLE monster (
id bigInt PRIMARY KEY,
attack int,
built bit(1),
description varchar(255),
health int,
name varchar(255),
type varchar(255));
create TABLE mon_ability (
monster_id bigint,
ability_id bigint,
foreign key (monster_id) references monster(id),
foreign key (ability_id) references ability(id));
