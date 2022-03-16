package com.example.project.enumTest;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.example.project.enums.Type;
import com.example.project.exceptions.NoTypeException;

public class TestType {

	@Test
	public void teststrTypeTrue() {
		Type result = null;
		try {
			result = Type.strType("abomination");
		}
		catch (NoTypeException t) {
			fail("Should have found Type 'Abomination");
		}
		assertThat(Type.ABOMINATION).isEqualTo(result);
	}
	
	@Test
	public void teststrTypefalse() {
		Throwable exception = assertThrows(NoTypeException.class, () -> Type.strType("."));
		assertThat("Could not find a type called '.'.").isEqualTo(exception.getMessage());
	}
}
