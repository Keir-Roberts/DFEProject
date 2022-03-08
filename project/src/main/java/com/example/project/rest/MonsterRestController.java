package com.example.project.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.classes.Monster;
import com.example.project.enums.SkillPoints;
import com.example.project.exceptions.BuildPointException;
import com.example.project.exceptions.NoAbilityException;
import com.example.project.exceptions.NoTypeException;
import com.example.project.service.MonsterService;

@RestController
public class MonsterRestController {

	public MonsterService service;
	
	public MonsterRestController(MonsterService service) {
		this.service = service;
	}
	
	@PostMapping("/createMon") 
	public Monster devCreate(@RequestBody Monster monster) throws NoTypeException, NoAbilityException {
		try {
			service.convert(monster);
		} catch(NoTypeException t) {
			throw t;
		} catch(NoAbilityException a) {
			throw a;
		}
		monster.setBuilt(true);
		return service.AddMonster(monster);
	}
	
	@PostMapping("/makeMon") 
	public Monster build(@RequestBody Monster monster) throws BuildPointException, NoTypeException, NoAbilityException {
		try {
			service.convert(monster);
		} catch(NoTypeException t) {
			throw t;
		} catch(NoAbilityException a) {
			throw a;
		}
		if (monster.getBuildPoints() > SkillPoints.POINTSMAX.getPoints()) {
		throw new BuildPointException("This build uses " + monster.getBuildPoints() + " build points which is greater than the maximum of " + SkillPoints.POINTSMAX.getPoints() + ".");
		} else {
			return service.AddMonster(monster);
		}
	}


}
