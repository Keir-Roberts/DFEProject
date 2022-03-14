INSERT INTO `monster` (id, name, attack, health, type, description, built) VALUES
(1, 'test1', 7, 7, 'fae', 'test desc', 1);
INSERT INTO `monster` (id, name, attack, health, type, description, built) VALUES
(2, 'test2', 4, 7, 'undead', '2nd test desc has abilities', 1);
INSERT INTO `monster` (id, name, attack, health, type, description, built) VALUES
(3, 'test3', 3, 4, 'nature', '3rd test desc', 0);
INSERT INTO `monster` (id, name, attack, health, type, description, built) VALUES
(4, 'test4', 4, 7, 'beast', '4th test desc has abilities', 1);
INSERT INTO `mon_ability` (monster_id, ability_id) VALUES
(1, 4);
INSERT INTO `mon_ability` (monster_id, ability_id) VALUES
(2, 8);
INSERT INTO `mon_ability` (monster_id, ability_id) VALUES
(4, 6);
INSERT INTO `mon_ability` (monster_id, ability_id) VALUES
(2, 4);
INSERT INTO `mon_ability` (monster_id, ability_id) VALUES
(4, 8);
