package com.example.project.serviceUnitTests;



import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.project.classes.Ability;
import com.example.project.classes.Monster;
import com.example.project.dto.AbilityDTO;
import com.example.project.enums.Type;
import com.example.project.exceptions.BuildPointException;
import com.example.project.exceptions.NoAbilityException;
import com.example.project.exceptions.existingAbilityException;
import com.example.project.service.ValidateService;




@SpringBootTest
public class ValidateServiceTests {

	@MockBean
	private Monster monster;

	@MockBean
	private ValidateService mockValid;

	@MockBean
	private Ability ability;

	@InjectMocks
	private ValidateService valid;



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
		assertThat(out).isTrue();
	}

	@Test
	public void testBPCheckF() {
		Monster mon = new Monster();
		ValidateService spyValid = Mockito.spy(valid);

		Mockito.doReturn(11).when(spyValid).bpUsed(Mockito.any());
		Throwable exception = assertThrows(BuildPointException.class, () -> spyValid.BPCheck(mon));
		assertThat("This monster has used a total of 11 'build points' which exceeds the maximum of: 10").isEqualTo(exception.getMessage());
	}

	@Test
	public void testBpUsedBuilt() {
		Monster mockMon = mock(Monster.class);
		List<AbilityDTO> abilities = new ArrayList<AbilityDTO>();
		abilities.add(new AbilityDTO(1L, "test", "test"));
		abilities.add(new AbilityDTO(2L, "test", "test"));
		Mockito.when(mockMon.getTypeEnum()).thenReturn(Type.BEAST);
		Mockito.when(mockMon.isBuilt()).thenReturn(true);
		Mockito.when((mockMon.getAbilities())).thenReturn(abilities);
		Mockito.when(mockMon.getHealth()).thenReturn(5);
		Mockito.when(mockMon.getAttack()).thenReturn(6);
		assertThat(7).isEqualTo(valid.bpUsed(mockMon));
		Mockito.verify(mockMon, times(1)).isBuilt();
		Mockito.verify(mockMon, times(1)).getAbilities();
		Mockito.verify(mockMon, times(1)).getHealth();
		Mockito.verify(mockMon, times(1)).getAttack();
		Mockito.verify(mockMon, times(1)).getTypeEnum();
	}

	@Test
	public void testBpUsedNotBuilt() {
		Monster mockMon = mock(Monster.class);
		List<AbilityDTO> abilities = new ArrayList<AbilityDTO>();
		abilities.add(new AbilityDTO(1L, "test", "test"));
		abilities.add(new AbilityDTO(2L, "test", "test"));
		Mockito.when(mockMon.isBuilt()).thenReturn(false);
		Mockito.when((mockMon.getAbilities())).thenReturn(abilities);
		Mockito.when(mockMon.getHealth()).thenReturn(5);
		Mockito.when(mockMon.getAttack()).thenReturn(6);
		assertThat(17).isEqualTo(valid.bpUsed(mockMon));
		Mockito.verify(mockMon, times(1)).isBuilt();
		Mockito.verify(mockMon, times(1)).getAbilities();
		Mockito.verify(mockMon, times(1)).getHealth();
		Mockito.verify(mockMon, times(1)).getAttack();
	}

	@Test
	public void testBPLeft() {
		Monster mon = new Monster();
		ValidateService spyValid = Mockito.spy(valid);

		Mockito.doReturn(9).when(spyValid).bpUsed(Mockito.any());
		assertThat(1).isEqualTo(spyValid.bpLeft(mon));
	}

	@Test
	public void testValStatChangeAtkBPE() {
		Monster mockMon = mock(Monster.class);
		ValidateService spyValid = Mockito.spy(valid);
		Mockito.when(mockMon.getTypeEnum()).thenReturn(Type.BEAST);
		Mockito.when(mockMon.getAttack()).thenReturn(6);
		Mockito.doReturn(2).when(spyValid).bpLeft(mockMon);
		Throwable exception = assertThrows(BuildPointException.class,
				() -> spyValid.valStatChange("attack", mockMon, 7));
		assertThat("This monster has 2 build points remaining. The proposed action costs 7 build points").isEqualTo(exception.getMessage());
	}

	@Test
	public void testValStatChangeDefBPE() {
		Monster mockMon = mock(Monster.class);
		ValidateService spyValid = Mockito.spy(valid);
		Mockito.when(mockMon.getTypeEnum()).thenReturn(Type.BEAST);
		Mockito.when(mockMon.getHealth()).thenReturn(6);
		Mockito.doReturn(2).when(spyValid).bpLeft(mockMon);
		Throwable exception = assertThrows(BuildPointException.class,
				() -> spyValid.valStatChange("health", mockMon, 7));
		assertThat("This monster has 2 build points remaining. The proposed action costs 7 build points").isEqualTo(exception.getMessage());
	}
	
	@Test
	public void testValStatChangeAtkIOB() {
		Monster mockMon = mock(Monster.class);
		ValidateService spyValid = Mockito.spy(valid);
		Mockito.when(mockMon.getTypeEnum()).thenReturn(Type.BEAST);
		Mockito.when(mockMon.getAttack()).thenReturn(4);
		Mockito.doReturn(5).when(spyValid).bpLeft(mockMon);
		Throwable exception = assertThrows(IndexOutOfBoundsException.class,
				() -> spyValid.valStatChange("attack", mockMon, -6));
		assertThat("This monster has a base attack of: 1 and you cannot have an attack less than or equal to 0").isEqualTo(exception.getMessage());
	}

@Test
public void testValStatChangeDefIOB() {
	Monster mockMon = mock(Monster.class);
	ValidateService spyValid = Mockito.spy(valid);
	Mockito.when(mockMon.getTypeEnum()).thenReturn(Type.BEAST);
	Mockito.when(mockMon.getHealth()).thenReturn(5);
	Mockito.doReturn(5).when(spyValid).bpLeft(mockMon);
	Throwable exception = assertThrows(IndexOutOfBoundsException.class,
			() -> spyValid.valStatChange("health", mockMon, -6));
	assertThat("This monster has a base health of: 1 and you cannot have an attack less than or equal to 0").isEqualTo(exception.getMessage());
}

@Test
public void testValAbilityAddBPE() {
	ValidateService spyValid = Mockito.spy(valid);
	Mockito.doReturn(1).when(spyValid).bpLeft(Mockito.any());
	Monster mockMon = mock(Monster.class);
	Ability mockAbility = mock(Ability.class);
	Throwable exception = assertThrows(BuildPointException.class, () -> spyValid.validAbilityAdd(mockMon, mockAbility));
	assertThat("This monster has 1 'build points' remaining, and 3 'build points' are required to add a new Ability.").isEqualTo(exception.getMessage());
}

@Test
public void tesValAbilityAddEAE() {
	ValidateService spyValid = Mockito.spy(valid);
	Mockito.doReturn(4).when(spyValid).bpLeft(Mockito.any());
	Monster mockMon = mock(Monster.class);
	Ability mockAbility = mock(Ability.class);
	List<Ability> abilities = new ArrayList<Ability>();
	abilities.add(mockAbility);
	Mockito.when(mockMon.trueGetAbilities()).thenReturn(abilities);
	Mockito.when(mockMon.getName()).thenReturn("test");
	Mockito.when(mockAbility.getName()).thenReturn("test");
	Throwable exception = assertThrows(existingAbilityException.class, () -> spyValid.validAbilityAdd(mockMon, mockAbility));
	assertThat("test has already got ability test").isEqualTo(exception.getMessage());
}

@Test
public void testValAbilityRemoveNAE() {
	ValidateService spyValid = Mockito.spy(valid);
	Monster mockMon = mock(Monster.class);
	Ability mockAbility = mock(Ability.class);
	List<Ability> abilities = new ArrayList<Ability>();
	abilities.add(new Ability());
	Mockito.when(mockMon.trueGetAbilities()).thenReturn(abilities);
	Mockito.when(mockAbility.getName()).thenReturn("test");
	Throwable exception = assertThrows(NoAbilityException.class, () -> spyValid.validAbilityRemove(mockMon, mockAbility));
	assertThat("This monster does not have test").isEqualTo(exception.getMessage());
}

@Test
public void testValidAbilityRemoveInnate() {
	ValidateService spyValid = Mockito.spy(valid);
	Monster mockMon = mock(Monster.class);
	Ability mockAbility = mock(Ability.class);
	List<Ability> abilities = new ArrayList<Ability>();
	abilities.add(mockAbility);
	Mockito.when(mockMon.trueGetAbilities()).thenReturn(abilities);
	Mockito.when(mockMon.getTypeEnum()).thenReturn(Type.FAE);
	Mockito.when(mockAbility.getId()).thenReturn(4L);
	Throwable exception = assertThrows(Exception.class, () -> spyValid.validAbilityRemove(mockMon, mockAbility));
	assertThat("Cannot remove innate abilities").isEqualTo(exception.getMessage());
}
}