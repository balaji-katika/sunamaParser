package com.fourglabs.sunama.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SunamaUtilsTest {

	@Test
	public void testCorrectName() {

		assertEquals("Balaji Katika", SunamaUtils.correctName("balaji katika"));

	}

	@Test
	public void testGetPrefix() {
		assertEquals(Constants.STR_SON_OF, SunamaUtils.getPrefix("Male"));
		assertEquals(Constants.STR_DO_OF, SunamaUtils.getPrefix("Female"));
	}

	@Test
	public void testGetOccupation() {
		assertEquals(false,SunamaUtils.getOccupation("Engineer").isEmpty());
		assertEquals(true,SunamaUtils.getOccupation("").isEmpty());
		assertEquals(true,SunamaUtils.getOccupation(null).isEmpty());
	}
}
