INSERT INTO `ability` (id, name, description) VALUES 
	(1, 'sturdy', 'Reduces damage by 1');
	INSERT INTO `ability` (id, name, description) VALUES  
	(2, 'critical', '25% chance to deal double damage');
	INSERT INTO `ability` (id, name, description) VALUES 
	(3, 'perfectionist', 'Deal 3 bonus damage when unharmed');
	INSERT INTO `ability` (id, name, description) VALUES 
	(4, 'evade', '25% chance to avoid all damage');
	INSERT INTO `ability` (id, name, description) VALUES 
	(5, 'draining', 'heal self by half damage done');
	INSERT INTO `ability` (id, name, description) VALUES 
	(6, 'momentum', 'gain 1 bonus damage after each attack');
	INSERT INTO `ability` (id, name, description) VALUES 
	(7, 'regenerate', 'heals 2 after each attack');
	INSERT INTO `ability` (id, name, description) VALUES 
	(8, 'revive', 'once after going to <1 health, revert back to 1 health');
	INSERT INTO `ability` (id, name, description) VALUES 
	(9, 'atkbaton', 'give the next monster in this monster''s team half of this monster''s attack when defeated');
	INSERT INTO `ability` (id, name, description) VALUES 
	(10, 'defbaton', 'give the next monster in this monster''s team half of this monster''s health when defeated');
	INSERT INTO `ability` (id, name, description) VALUES 
	(11, 'pact', 'gain attack and defense equal to half of the attack and health of the next monster in this party but the next monster takes 2 damage and has 1 less attack');
	INSERT INTO `ability` (id, name, description) VALUES 
	(12, 'reflect', 'when this monster takes damage, deal damage to the opponent equal to half the damage taken rounded up');
	INSERT INTO `ability` (id, name, description) VALUES 
	(13, 'ruthless', 'this monster deals 3x damage to those it is strong against instead of 2x. If the opponent has defensive, they cancel out');
	INSERT INTO `ability` (id, name, description) VALUES 
	(14, 'defensive', 'this monster does not take extra damage from enemies that are strong against it. if the opponent has ruthless, they cancel out');
	INSERT INTO `ability` (id, name, description) VALUES 
	(15, 'retaliate', 'this monster deals 1 extra damage per piece of health missing');