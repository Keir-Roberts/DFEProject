drop table if exists `mon_ability` cascade;
create table mon_ability (
id BIGINT NOT NULL AUTO_INCREMENT,
monster_id BIGINT NOT NULL,
ability_id BIGINT NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (monster_id) REFERENCES monster(id),
FOREIGN KEY (ability_id) REFERENCES ability(id));

DROP TABLE IF EXISTS `ability` CASCADE;
CREATE TABLE ability (
	id BIGINT NOT NUll AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL UNIQUE,
	description varchar(255),
	PRIMARY KEY (id));
	
DROP TABLE IF EXISTS `monster` CASCADE;
CREATE TABLE IF NOT EXISTS monster (
id  BIGINT NOT NULL AUTO_INCREMENT,
name VARCHAR(255) NOT NULL,
attack INT,
health INT,
type_str VARCHAR(255) NOT NULL,
description varchar(255),
built boolean NOT NULL
PRIMARY KEY (id));


