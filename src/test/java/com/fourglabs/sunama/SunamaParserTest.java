package com.fourglabs.sunama;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import junit.framework.TestCase;

public class SunamaParserTest extends TestCase {

	SunamaParser spParser = new SunamaParser();

	@Test
	public void testConstructName() {
		Method method;
		try {
			method = SunamaParser.class.getDeclaredMethod("correctName", String.class);
			method.setAccessible(true);
			String output = (String) method.invoke(spParser, "balaji katika");
			// Assert.assertEquals("Balaji Katika", "balaji katika");
			System.out.println(output);
		}
		catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
