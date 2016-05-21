package uk.mm.bobcat.service;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.mm.bobcat.AStringInputValuesTest;

public class BobcatLoginServiceInputTest extends AStringInputValuesTest {

	private static BobcatLoginService bobcatLoginService;

	@BeforeClass
	public static void setupLoginService() {
		bobcatLoginService = new BobcatLoginService();
	}

	public BobcatLoginServiceInputTest(String inputValue) {
		super(inputValue);
	}

	@Test
	public void testNoUserProvided() {
		Assert.assertNull(bobcatLoginService.login(inputValue, "passw"));
	}

	@Test
	public void testNoCredentialsProvided() {
		Assert.assertNull(bobcatLoginService.login("user", inputValue));
	}
}
