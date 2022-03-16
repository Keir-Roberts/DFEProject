package com.example.project.serviceUnitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import com.example.project.classes.Ability;
import com.example.project.classes.Monster;
import com.example.project.enums.Type;
import com.example.project.exceptions.NoTypeException;
import com.example.project.repo.abilityRepo;
import com.example.project.repo.monsterRepo;
import com.example.project.service.MonsterService;
import com.example.project.service.ValidateService;

@SpringBootTest
@ActiveProfiles("test")
public class MonsterServiceTests {

	@MockBean
	private monsterRepo mRepo;

	@MockBean
	private ValidateService valid;

	@MockBean
	private abilityRepo abRepo;

	@Autowired
	private MonsterService service;

	@Test
	public void testBuildBuilt() {
		Monster monIn = new Monster(1, "test", 1, 1, "undead", "test", true);
		Monster monOut = null;
		try {
			monOut = service.build(monIn);
		} catch (Exception e) {
			fail("shouldnt fail with " + e.getMessage());
		}
		assertThat(monOut).isEqualTo(monIn);
	}

	@Test
	public void testBuildnotBuld() {
		MonsterService spyMonS = Mockito.spy(service);
		List<Ability> abilities = new ArrayList<Ability>();
		Ability ability = new Ability();
		abilities.add(ability);
		Monster monIn = new Monster(2, "test", 1, 1, "undead", "test");
		Monster expected = new Monster(2, "test", 2, 7, "undead", abilities, "test", Type.UNDEAD, true);
		when(abRepo.findById(Mockito.any())).thenReturn(Optional.of(ability));
		try {
			Mockito.doReturn(monIn).when(spyMonS).addMonAbility(Mockito.any(Monster.class), Mockito.any());
		} catch (Exception e) {
			fail("should not fail");
		}
		try {
			spyMonS.build(monIn);
		} catch (Exception e) {
			fail("Should not fail");
		}
		monIn.setAbilities(abilities);
		assertThat(monIn).usingRecursiveComparison().isEqualTo(expected);
		try {
			Mockito.verify(spyMonS, Mockito.times(1)).addMonAbility(monIn, ability);
		} catch (Exception e) {
			fail("Should not fail");
		}
		Mockito.verify(abRepo, Mockito.times(1)).findById(8L);
	}

	@Test
	public void testUnbuildNoBuilt() {
		Monster monIn = new Monster(1, "test", 1, 1, "Undead", "test", false);
		Monster expected = new Monster(1, "test", 1, 1, "Undead", "test", false);
		try {
			service.unbuild(monIn);
		} catch (Exception e) {
			fail("shouldnt fail with " + e.getMessage());
		}
		assertThat(monIn).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	public void testUnbuildBuilt() {
		MonsterService spyMonS = Mockito.spy(service);
		List<Ability> abilities = new ArrayList<Ability>();
		Ability ability = new Ability();
		abilities.add(ability);
		Monster expected = new Monster(2, "test", 1, 1, "Undead", abilities, "test", Type.UNDEAD, false);
		Monster monIn = new Monster(2, "test", 2, 7, "Undead", abilities, "test", Type.UNDEAD, true);
		when(abRepo.findById(Mockito.any())).thenReturn(Optional.of(ability));
		try {
			Mockito.doReturn(monIn).when(spyMonS).removeMonAbility(Mockito.any(Monster.class), Mockito.any());
			spyMonS.unbuild(monIn);
		} catch (Exception e2) {
			fail("shouldnt fail with " + e2.getMessage());
		}
		assertThat(monIn).usingRecursiveComparison().isEqualTo(expected);
		try {
			Mockito.verify(spyMonS, Mockito.times(1)).removeMonAbility(monIn, ability);
		} catch (Exception e) {
			fail("shouldnt fail with " + e.getMessage());
		}
			Mockito.verify(abRepo, Mockito.times(1)).findById(8L);
		
	}

	@Test
	public void testAddMonBuilt() {
		List<Ability> abilities = new ArrayList<Ability>();
		Monster monIn = new Monster(1, "test", 1, 1, "Undead", abilities, "test", Type.UNDEAD, true);
		Monster saveMon = new Monster(1, "test", 1, 1, "Undead", abilities, "test", Type.UNDEAD, true);
		when(mRepo.save(Mockito.any(Monster.class))).thenReturn(saveMon);
		try {
			assertThat(this.service.AddMonster(monIn)).usingRecursiveComparison().isEqualTo(saveMon);
		} catch (Exception e) {
			fail("shouldnt fail with " + e.getMessage());
		}
		Mockito.verify(this.mRepo, Mockito.times(2)).save(monIn);
	}
	
	@Test
	public void testAddMonNoBuilt() {
		List<Ability> abilities = new ArrayList<Ability>();
		Ability ability = new Ability();
		abilities.add(ability);
		MonsterService spyMonS = Mockito.spy(service);
		Monster monIn = new Monster(1, "test", 1, 1, "undead", abilities, "test", Type.UNDEAD, false);
		Monster builtMon = new Monster(1, "test", 2, 7, "undead", abilities, "test", Type.UNDEAD, true);
		when(mRepo.save(monIn)).thenReturn(monIn);
		when(mRepo.save(builtMon)).thenReturn(builtMon);
		try {
			Mockito.doReturn(builtMon).when(spyMonS).build(monIn);
			assertThat(spyMonS.AddMonster(monIn)).usingRecursiveComparison().isEqualTo(builtMon);
		} catch (Exception e) {
			fail("should not error");
		}
		Mockito.verify(this.mRepo, Mockito.times(1)).save(monIn);
		Mockito.verify(this.mRepo, Mockito.times(1)).save(builtMon);
		try {
			Mockito.verify(spyMonS, Mockito.times(1)).build(monIn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

@Test
public void testAddAbilityMon() {
	List<Ability> abStart = new ArrayList<Ability>();
	List<Ability> abEnd = new ArrayList<Ability>();
	Ability abilityIn = new Ability();
	abEnd.add(abilityIn);
	Monster expected = new Monster(2, "test", 1, 1, "Undead", abEnd, "test", Type.UNDEAD, true);
	Monster monIn = new Monster(2, "test", 1, 1, "Undead", abStart, "test", Type.UNDEAD, true);
	when(mRepo.save(monIn)).thenReturn(monIn);
	try {
		assertThat(service.addMonAbility(monIn, abilityIn)).usingRecursiveComparison().isEqualTo(expected);
	} catch (Exception e) {
		fail("shouldnt fail with " + e.getMessage());
	}
	Mockito.verify(this.mRepo, Mockito.times(1)).save(monIn);
}

@Test
public void testGetAll() {
	List<Monster> monList = new ArrayList<Monster>();
	when(mRepo.findAll()).thenReturn(monList);
	assertThat(service.getAllMon()).isEqualTo(monList);
	Mockito.verify(mRepo, Mockito.times(1)).findAll();
}

@Test
public void testGetTypeValues() {
	Monster mon1 = new Monster(1, "test", 1, 1, "Undead", null, "test", Type.UNDEAD, true);
	Monster mon2 = new Monster(2, "test", 1, 1, "Undead", null, "test", Type.UNDEAD, true);
	Monster mon3 = new Monster(3, "test", 1, 1, "Fae", null, "test", Type.FAE, true);
	List<Monster> allMon = new ArrayList<Monster>();
	allMon.add(mon3);
	allMon.add(mon2);
	allMon.add(mon1);
	List<Monster> expected = new ArrayList<Monster>();
	expected.add(mon2);
	expected.add(mon1);
	when(mRepo.findAll()).thenReturn(allMon);
	try {
		assertThat(service.getType("undead")).isEqualTo(expected);
	} catch (NoTypeException e) {
		fail("shouldnt fail with " + e.getMessage());
	}
	Mockito.verify(mRepo, Mockito.times(1)).findAll();
	
}

@Test
public void testGetTypeNull() {
	Monster mon3 = new Monster(3, "test", 1, 1, "Fae", null, "test", Type.FAE, true);
	List<Monster> allMon = new ArrayList<Monster>();
	allMon.add(mon3);
	when(mRepo.findAll()).thenReturn(allMon);
	String out = null;
	try {
		service.getType("undead");
	} catch (NoTypeException t) {
		out = t.getMessage();
	} finally { 
	assertThat(out).isEqualTo("No monsters found that match the input: 'undead'.");
	Mockito.verify(mRepo, Mockito.times(1)).findAll();
}
}

@Test
public void testReadId() {
	Monster testMon = new Monster();
	Optional<Monster> test = Optional.of(testMon);
	when(mRepo.findById(Mockito.anyLong())).thenReturn(test);
	try {
		assertThat(service.readID(1L)).isEqualTo(testMon);
	} catch (Exception e) {
		fail("shouldnt fail with " + e.getMessage());
	}
	Mockito.verify(mRepo, Mockito.times(1)).findById(1L);
}

@Test
public void testGetID() {
	MonsterService spyMonS = Mockito.spy(service);
	Monster testMon = new Monster(1, "test", 1, 1, "Undead", null, "test", null, false);
	Monster expected = new Monster(1, "test", 1, 1, "Undead", null, "test", Type.UNDEAD, false);
	try {
		Mockito.doReturn(testMon).when(spyMonS).readID(1L);
		assertThat(spyMonS.getID(1L)).usingRecursiveComparison().isEqualTo(expected);
	} catch (Exception e) {
		fail("shouldnt fail with " + e.getMessage());
	}
	try {
		Mockito.verify(spyMonS, Mockito.times(1)).readID(1L);
	} catch (Exception e) {
		fail("shouldnt fail with " + e.getMessage());
	}
}

@Test
public void testUpdateATK() {
	Monster monIn = new Monster(1, "test", 1, 1, "undead", null, "test", Type.UNDEAD, false);
	Monster expected = new Monster(1, "test", 3, 1, "undead", null, "test", Type.UNDEAD, false);
	MonsterService spyMonS = Mockito.spy(service);
	when(mRepo.save(Mockito.any())).thenReturn(monIn);
	try {
		Mockito.doReturn(monIn).when(spyMonS).readID(1L);
		assertThat(spyMonS.updateMonAttack(1L, 2)).usingRecursiveComparison().isEqualTo(expected);
	} catch (Exception e) {
		fail("shouldnt fail with " + e.getMessage());
	}
	Mockito.verify(mRepo, Mockito.times(1)).save(monIn);
}

@Test
public void testUpdateDEF() {
	Monster monIn = new Monster(1, "test", 1, 1, "undead", null, "test", Type.UNDEAD, false);
	Monster expected = new Monster(1, "test", 1, 3, "undead", null, "test", Type.UNDEAD, false);
	MonsterService spyMonS = Mockito.spy(service);
	when(mRepo.save(Mockito.any())).thenReturn(monIn);
	try {
		Mockito.doReturn(monIn).when(spyMonS).readID(1L);
		assertThat(spyMonS.updateMonHealth(1L, 2)).usingRecursiveComparison().isEqualTo(expected);
	} catch (Exception e) {
		fail("shouldnt fail with " + e.getMessage());
	}
	Mockito.verify(mRepo, Mockito.times(1)).save(monIn);
}

@Test
public void testAddAbilityId() {
	MonsterService spyMonS = Mockito.spy(service);
	List<Ability> abStart = new ArrayList<Ability>();
	List<Ability> abEnd = new ArrayList<Ability>();
	Ability abilityIn = new Ability();
	abEnd.add(abilityIn);
	Monster expected = new Monster(2, "test", 1, 1, "Undead", abEnd, "test", Type.UNDEAD, true);
	Monster monIn = new Monster(2, "test", 1, 1, "Undead", abStart, "test", Type.UNDEAD, true);
	when(mRepo.save(monIn)).thenReturn(monIn);
	
	try {
		when(abRepo.findByName("ability")).thenReturn(Optional.of(abilityIn));
		Mockito.doReturn(monIn).when(spyMonS).readID(1L);
		assertThat(spyMonS.addMonAbility(1L, "ability")).usingRecursiveComparison().isEqualTo(expected);
		Mockito.verify(spyMonS, Mockito.times(1)).readID(1L);
		Mockito.verify(abRepo, Mockito.times(1)).findByName("ability");
	} catch (Exception e) {
		fail("shouldnt fail with " + e.getMessage());
	}
	Mockito.verify(this.mRepo, Mockito.times(1)).save(monIn);
}
	
@Test
public void removeAbilityId() {
	MonsterService spyMonS = Mockito.spy(service);
	List<Ability> abStart = new ArrayList<Ability>();
	List<Ability> abEnd = new ArrayList<Ability>();
	Ability abilityIn = new Ability();
	abEnd.add(abilityIn);
	Monster monIn = new Monster(2, "test", 1, 1, "Undead", abEnd, "test", Type.UNDEAD, true);
	Monster expected = new Monster(2, "test", 1, 1, "Undead", abStart, "test", Type.UNDEAD, true);
	when(mRepo.save(monIn)).thenReturn(monIn);
	
	try {
		when(abRepo.findByName("ability")).thenReturn(Optional.of(abilityIn));
		Mockito.doReturn(monIn).when(spyMonS).readID(1L);
		assertThat(spyMonS.removeMonAbility(1L, "ability")).usingRecursiveComparison().isEqualTo(expected);
		Mockito.verify(spyMonS, Mockito.times(1)).readID(1L);
		Mockito.verify(abRepo, Mockito.times(1)).findByName("ability");
	} catch (Exception e) {
		fail("shouldnt fail with " + e.getMessage());
	}
	Mockito.verify(this.mRepo, Mockito.times(1)).save(monIn);
}

@Test
public void removeAbilityMon() {
	List<Ability> abStart = new ArrayList<Ability>();
	List<Ability> abEnd = new ArrayList<Ability>();
	Ability abilityIn = new Ability();
	abEnd.add(abilityIn);
	Monster monIn = new Monster(2, "test", 1, 1, "Undead", abEnd, "test", Type.UNDEAD, true);
	Monster expected = new Monster(2, "test", 1, 1, "Undead", abStart, "test", Type.UNDEAD, true);
	when(mRepo.save(monIn)).thenReturn(monIn);
	try {
		assertThat(service.removeMonAbility(monIn, abilityIn)).usingRecursiveComparison().isEqualTo(expected);
	} catch (Exception e) {
		fail("shouldnt fail with " + e.getMessage());
	}
	Mockito.verify(this.mRepo, Mockito.times(1)).save(monIn);
}

@Test
public void removeAbilityMonInnate() {
	List<Ability> abEnd = new ArrayList<Ability>();
	Ability abilityIn = new Ability(8, "revive", "revives");
	abEnd.add(abilityIn);
	Monster monIn = new Monster(2, "test", 1, 1, "Undead", abEnd, "test", Type.UNDEAD, true);
	try {
		service.removeMonAbility(monIn, abilityIn);
	} catch (Exception e) {
		assertThat(e.getMessage()).isEqualTo("Cannot remove innate abilities");
	}
}

@Test
public void testGetName() {
	Monster mon1 = new Monster(1, "testOne", 1, 1, "Undead", null, "test", Type.UNDEAD, true);
	Monster mon2 = new Monster(2, "testTwo", 1, 1, "Undead", null, "test", Type.UNDEAD, true);
	Monster mon3 = new Monster(3, "testThree", 1, 1, "Fae", null, "test", Type.FAE, true);
	List<Monster> allMon = new ArrayList<Monster>();
	allMon.add(mon3);
	allMon.add(mon2);
	allMon.add(mon1);
	List<Monster> expected = new ArrayList<Monster>();
	expected.add(mon2);
	when(mRepo.findAll()).thenReturn(allMon);
	assertThat(service.getName("testTwo")).isEqualTo(expected);
	Mockito.verify(mRepo, Mockito.times(1)).findAll();
}

@Test
public void testDeleteMon() {
	Monster monIn = new Monster(2, "test", 1, 1, "Undead", null, "test", Type.UNDEAD, true);
	MonsterService spyMonS = Mockito.spy(service);
	try {
		Mockito.doReturn(monIn).when(spyMonS).readID(1L);
		assertThat(spyMonS.deleteMon(1L)).isEqualTo("test has been deleted");
		Mockito.verify(spyMonS, Mockito.times(2)).readID(1L);
	} catch (Exception e) {
		fail("shouldnt fail with " + e.getMessage());
	}
}

@Test
public void testUpdate() {
	List<Ability> abilities = new ArrayList<Ability>();
	Ability ability = new Ability(1, "test", "test");
	abilities.add(ability);
	MonsterService spyMonS = Mockito.spy(service);
	Monster mon1 = new Monster(1, "testOne", 1, 1, "Undead", abilities, "test", Type.UNDEAD, false);
	Monster mon2 = new Monster(1, "testTwo", 7, 7, "Fae", abilities, "test1", Type.FAE, true);
	when(mRepo.save(mon1)).thenReturn(mon1);
	try {
		Mockito.doReturn(mon1).when(spyMonS).readID(1L);
		assertThat(spyMonS.Update(1L, mon2)).usingRecursiveComparison().isEqualTo(mon2);
		Mockito.verify(mRepo, Mockito.times(1)).save(mon1);
		Mockito.verify(spyMonS, Mockito.times(1)).readID(1L);
	} catch (Exception e) {
		fail("shouldn't fail with " + e.getMessage());
	}
	
}

@Test
public void testCompareLess() {
	MonsterService spyMonS = Mockito.spy(service);
	List<Ability> abilities = new ArrayList<Ability>();
	Monster m1 = new Monster(1L, "m1", 1, 1, "Undead", abilities, "test", Type.UNDEAD, true);
	Monster m2 = new Monster(2L, "m2", 2, 2, "Fae", abilities, "test", Type.FAE, true);
	String attack = "m2 has the higher attack of 2 which is 1 points larger than m1's attack of 1.";
	String health = "m2 has the higher health of 2 which is 1 points larger than m1's health of 1.";
	try {
		Mockito.doReturn(m1).when(spyMonS).readID(1L);
	
	Mockito.doReturn(m2).when(spyMonS).readID(2L);
	assertThat(spyMonS.compare(1L, 2L)).isEqualTo("Comparing m1 and m2.\n Type: \n      m1's type is Undead and m2's type is Fae.\n\n Attack: \n      " + attack + "\n\n Health: \n      " + health + "\n\n Abilities \n      m1's abilities are: " + m1.getAbilities() + "\n      m2's abilities are: " + m2.getAbilities());
	} catch (Exception e) {
		fail("shouldnt fail with " + e.getMessage());
	}
	}

@Test
public void testCompareMore() {
	MonsterService spyMonS = Mockito.spy(service);
	List<Ability> abilities = new ArrayList<Ability>();
	Monster m1 = new Monster(1L, "m1", 2, 2, "Undead", abilities, "test", Type.UNDEAD, true);
	Monster m2 = new Monster(2L, "m2", 1, 1, "Fae", abilities, "test", Type.FAE, true);
	String attack = "m1 has the higher attack of 2 which is 1 points larger than m2's attack of 1.";
	String health = "m1 has the higher health of 2 which is 1 points larger than m2's health of 1.";
	try {
		Mockito.doReturn(m1).when(spyMonS).readID(1L);
	
	Mockito.doReturn(m2).when(spyMonS).readID(2L);
	assertThat(spyMonS.compare(1L, 2L)).isEqualTo("Comparing m1 and m2.\n Type: \n      m1's type is Undead and m2's type is Fae.\n\n Attack: \n      " + attack + "\n\n Health: \n      " + health + "\n\n Abilities \n      m1's abilities are: " + m1.getAbilities() + "\n      m2's abilities are: " + m2.getAbilities());
	} catch (Exception e) {
		fail("shouldnt fail with " + e.getMessage());
	}
}

@Test
public void testCompareEqual() {
	MonsterService spyMonS = Mockito.spy(service);
	List<Ability> abilities = new ArrayList<Ability>();
	Monster m1 = new Monster(1L, "m1", 2, 2, "Undead", abilities, "test", Type.UNDEAD, true);
	Monster m2 = new Monster(2L, "m2", 2, 2, "Fae", abilities, "test", Type.FAE, true);
	String attack = "They both have the same attack score of 2";
	String health = "They both have the same health score of 2";
	try {
		Mockito.doReturn(m1).when(spyMonS).readID(1L);
	
	Mockito.doReturn(m2).when(spyMonS).readID(2L);
	assertThat(spyMonS.compare(1L, 2L)).isEqualTo("Comparing m1 and m2.\n Type: \n      m1's type is Undead and m2's type is Fae.\n\n Attack: \n      " + attack + "\n\n Health: \n      " + health + "\n\n Abilities \n      m1's abilities are: " + m1.getAbilities() + "\n      m2's abilities are: " + m2.getAbilities());
	} catch (Exception e) {
		fail("shouldnt fail with " + e.getMessage());
	}
}
}
