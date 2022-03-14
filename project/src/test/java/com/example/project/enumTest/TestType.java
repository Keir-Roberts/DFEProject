package com.example.project.enumTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import org.junit.Test;

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
		assertEquals("Should return type 'abomination'", Type.ABOMINATION, result);
	}
	
	@Test
	public void teststrTypefalse() {
		Throwable exception = assertThrows(NoTypeException.class, () -> Type.strType("."));
		assertEquals("expected \"Could not find a type called '.'\".\" but got" + exception.getMessage(), "Could not find a type called '.'.", exception.getMessage());
	}
	
	@Test
	public void testGetCost() {
		int result = Type.BEAST.getCost();
		assertEquals("Expected 10 but recieved" + result, 10, result);
	}
	
	@Test
	public void testGetAtk() {
		int result = Type.DRAGON.getAttack(3);
		assertEquals("Expected outcome of 8 but got " + result, 8, result);
	}
	
	@Test
	public void testGetDef() {
		int  result = Type.UNDEAD.getDef(4);
		assertEquals("Expected 10 but received " + result, 10, result);
	}
}
