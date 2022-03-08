package com.example.project.enums;

import com.example.project.exceptions.NoAbilityException;

public enum Ability {
	STURDY("sturdy", "Reduces damage by 1"), CRITICAL("critical", "25% chance to do double damage"),
	PERFECTIONIST("perfectionist", "Deal 3 bonus damage when unharmed"),
	EVADE("evade", "25% chance to avoid all damage"), DRAINING("draining", "Heal self by half damage done"),
	MOMENTUM("momentum", "Gain 1 bonus damage after each attack"),
	RETALIATE("retaliate", "Deal 1 extra damage per missing bit of health"),
	REGENERATE("regenerate", "Heals 2 after each attack"),
	REVIVE("revive", "Once after going to <1 health, revert back to 1 health"),
	ATKBATON("atkbaton", "give the next monster in this monster's team half of its attack when defeated"),
	DEFBATON("defbaton", "give the next monster in this monster's team half of this monster's defense when defeated"),
	PACT("pact",
			"Gain attack and defense equal to half of the attack and defense of the next monster in this party but the next monster takes 2 damages and has 1 less attack"),
	REFLECT("reflect",
			"when this monster takes damage, deal damage to the opponent equal to half the damage taken rounded up"),
	RUTHLESS("ruthless", "This monster deals 3x damage to those it is strong against instead of 2x."),
	DEFENSIVE("defensive", "This monster does not take extra damage from enemies that are strong against it.");

	private final String name;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	private final String description;

	Ability(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public static Ability abilityStr(String in) throws NoAbilityException {
		for (Ability ability : Ability.values()) {
			if (in.toLowerCase().equals(ability.getName().toLowerCase()))
				return ability;
		}
		throw new NoAbilityException("No ability found called '" + in + "'.");
	}
}