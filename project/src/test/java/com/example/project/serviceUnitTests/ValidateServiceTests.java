package com.example.project.serviceUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.project.classes.Ability;
import com.example.project.classes.Monster;
import com.example.project.enums.Type;
import com.example.project.exceptions.BuildPointException;
import com.example.project.exceptions.NoAbilityException;
import com.example.project.exceptions.NoTypeException;
import com.example.project.exceptions.existingAbilityException;
import com.example.project.service.ValidateService;

@RunWith(MockitoJUnitRunner.class)
public class ValidateServiceTests {

	@Mock
	private Monster monster;

	@Mock
	private ValidateService mockValid;

	@Mock
	private Ability ability;

	@InjectMocks
	private ValidateService valid;

	@Test
	public void testConvertStEpass() {
		Monster mon = new Monster(0, "test", 1, 1, "undead", "test");
		try {
			valid.convertStrToEnum(mon);
		} catch (NoTypeException t) {
			fail("Should recognise type");
		}
		assertEquals("Should return type.UNDEAD", mon.getType(), Type.UNDEAD);
	}

	@Test
	public void testConvertStEfail() {
		Monster mon = new Monster(0, "test", 1, 1, ".", "test");
		Throwable exception = assertThrows(NoTypeException.class, () -> valid.convertStrToEnum(mon));
		assertEquals("expected \"Cannot find a type called .\" but got" + exception.getMessage(),
				"Cannot find a type called .", exception.getMessage());
	}

	@Test
	public void testConvertEnumToStr() {
		Monster mon = new Monster();
		mon.setType(Type.FAE);
		valid.convertEnumToStr(mon);
		assertEquals("Expected 'Fae' but recieved " + mon.getTypeStr(), "Fae", mon.getTypeStr());
	}

	@Test
	public void testBPCheckT() {
		Monster mon = new Monster();
		ValidateService spyValid = Mockito.spy(valid);

		Mockito.doReturn(9).when(spyValid).bpUsed(Mockito.any());
		boolean out = false;
		try {
			out = spyValid.BPCheck(mon);
		} catch (BuildPointException b) {
			fail("Should pass");
		}
		assertTrue(out);
	}

	@Test
	public void testBPCheckF() {
		Monster mon = new Monster();
		ValidateService spyValid = Mockito.spy(valid);

		Mockito.doReturn(11).when(spyValid).bpUsed(Mockito.any());
		Throwable exception = assertThrows(BuildPointException.class, () -> spyValid.BPCheck(mon));
		assertEquals("error messages do not match",
				"This monster has used a total of 11 'build points' which exceeds the maximum of: 10",
				exception.getMessage());
	}

	@Test
	public void testBpUsedBuilt() {
		Monster mockMon = mock(Monster.class);
		List<Ability> abilities = new ArrayList<Ability>();
		abilities.add(new Ability());
		abilities.add(new Ability());
		when(mockMon.getType()).thenReturn(Type.BEAST);
		when(mockMon.isBuilt()).thenReturn(true);
		when((mockMon.getAbilities())).thenReturn(abilities);
		when(mockMon.getHealth()).thenReturn(5);
		when(mockMon.getAttack()).thenReturn(6);
		assertEquals("should be equal", 7, valid.bpUsed(mockMon));
		verify(mockMon, times(1)).isBuilt();
		verify(mockMon, times(1)).getAbilities();
		verify(mockMon, times(1)).getHealth();
		verify(mockMon, times(1)).getAttack();
		verify(mockMon, times(1)).getType();
	}

	@Test
	public void testBpUsedNotBuilt() {
		Monster mockMon = mock(Monster.class);
		List<Ability> abilities = new ArrayList<Ability>();
		abilities.add(new Ability());
		abilities.add(new Ability());
		when(mockMon.isBuilt()).thenReturn(false);
		when((mockMon.getAbilities())).thenReturn(abilities);
		when(mockMon.getHealth()).thenReturn(5);
		when(mockMon.getAttack()).thenReturn(6);
		assertEquals("should be equal", 17, valid.bpUsed(mockMon));
		verify(mockMon, times(1)).isBuilt();
		verify(mockMon, times(1)).getAbilities();
		verify(mockMon, times(1)).getHealth();
		verify(mockMon, times(1)).getAttack();
	}

	@Test
	public void testBPLeft() {
		Monster mon = new Monster();
		ValidateService spyValid = Mockito.spy(valid);

		Mockito.doReturn(9).when(spyValid).bpUsed(Mockito.any());
		assertEquals("Should be equal", 1, spyValid.bpLeft(mon));
	}

	@Test
	public void testValStatChangeAtkBPE() {
		Monster mockMon = mock(Monster.class);
		ValidateService spyValid = Mockito.spy(valid);
		when(mockMon.getType()).thenReturn(Type.BEAST);
		when(mockMon.getAttack()).thenReturn(6);
		Mockito.doReturn(2).when(spyValid).bpLeft(mockMon);
		Throwable exception = assertThrows(BuildPointException.class,
				() -> spyValid.valStatChange("attack", mockMon, 7));
		assertEquals("messages should match",
				"This monster has 2 build points remaining. The proposed action costs 7 build points",
				exception.getMessage());
	}

	@Test
	public void testValStatChangeDefBPE() {
		Monster mockMon = mock(Monster.class);
		ValidateService spyValid = Mockito.spy(valid);
		when(mockMon.getType()).thenReturn(Type.BEAST);
		when(mockMon.getHealth()).thenReturn(6);
		Mockito.doReturn(2).when(spyValid).bpLeft(mockMon);
		Throwable exception = assertThrows(BuildPointException.class,
				() -> spyValid.valStatChange("health", mockMon, 7));
		assertEquals("messages should match",
				"This monster has 2 build points remaining. The proposed action costs 7 build points",
				exception.getMessage());
	}
	
	@Test
	public void testValStatChangeAtkIOB() {
		Monster mockMon = mock(Monster.class);
		ValidateService spyValid = Mockito.spy(valid);
		when(mockMon.getType()).thenReturn(Type.BEAST);
		when(mockMon.getAttack()).thenReturn(4);
		Mockito.doReturn(5).when(spyValid).bpLeft(mockMon);
		Throwable exception = assertThrows(IndexOutOfBoundsException.class,
				() -> spyValid.valStatChange("attack", mockMon, -6));
		assertEquals("messages should match",
				"This monster has a base attack of: 1 and you cannot have an attack less than or equal to 0",
				exception.getMessage());
	}

@Test
public void testValStatChangeDefIOB() {
	Monster mockMon = mock(Monster.class);
	ValidateService spyValid = Mockito.spy(valid);
	when(mockMon.getType()).thenReturn(Type.BEAST);
	when(mockMon.getHealth()).thenReturn(5);
	Mockito.doReturn(5).when(spyValid).bpLeft(mockMon);
	Throwable exception = assertThrows(IndexOutOfBoundsException.class,
			() -> spyValid.valStatChange("health", mockMon, -6));
	assertEquals("messages should match",
			"This monster has a base health of: 1 and you cannot have an attack less than or equal to 0",
			exception.getMessage());
}

@Test
public void testValAbilityAddBPE() {
	ValidateService spyValid = Mockito.spy(valid);
	Mockito.doReturn(1).when(spyValid).bpLeft(Mockito.any());
	Monster mockMon = mock(Monster.class);
	Ability mockAbility = mock(Ability.class);
	Throwable exception = assertThrows(BuildPointException.class, () -> spyValid.validAbilityAdd(mockMon, mockAbility));
	assertEquals("Messages should be the same", "This monster has 1 'build points' remaining, and 3 'build points' are required to add a new Ability.", exception.getMessage());
}

@Test
public void tesValAbilityAddEAE() {
	ValidateService spyValid = Mockito.spy(valid);
	Mockito.doReturn(4).when(spyValid).bpLeft(Mockito.any());
	Monster mockMon = mock(Monster.class);
	Ability mockAbility = mock(Ability.class);
	List<Ability> abilities = new ArrayList<Ability>();
	abilities.add(mockAbility);
	when(mockMon.getAbilities()).thenReturn(abilities);
	when(mockMon.getName()).thenReturn("test");
	when(mockAbility.getName()).thenReturn("test");
	Throwable exception = assertThrows(existingAbilityException.class, () -> spyValid.validAbilityAdd(mockMon, mockAbility));
	assertEquals("Messages should be the same", "test has already got ability test", exception.getMessage());
}

@Test
public void testValAbilityRemoveNAE() {
	ValidateService spyValid = Mockito.spy(valid);
	Monster mockMon = mock(Monster.class);
	Ability mockAbility = mock(Ability.class);
	List<Ability> abilities = new ArrayList<Ability>();
	abilities.add(new Ability());
	when(mockMon.getAbilities()).thenReturn(abilities);
	when(mockAbility.getName()).thenReturn("test");
	Throwable exception = assertThrows(NoAbilityException.class, () -> spyValid.validAbilityRemove(mockMon, mockAbility));
	assertEquals("Messages should be the same", "This monster does not have test", exception.getMessage());
}

@Test
public void testValidAbilityRemoveInnate() {
	ValidateService spyValid = Mockito.spy(valid);
	Monster mockMon = mock(Monster.class);
	Ability mockAbility = mock(Ability.class);
	List<Ability> abilities = new ArrayList<Ability>();
	abilities.add(mockAbility);
	when(mockMon.getAbilities()).thenReturn(abilities);
	when(mockMon.getType()).thenReturn(Type.FAE);
	when(mockAbility.getId()).thenReturn(4L);
	Throwable exception = assertThrows(Exception.class, () -> spyValid.validAbilityRemove(mockMon, mockAbility));
	assertEquals("Messages should be the same", "Cannot remove innate abilities", exception.getMessage());
}
}