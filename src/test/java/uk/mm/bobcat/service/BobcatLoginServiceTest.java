package uk.mm.bobcat.service;

import java.security.Principal;
import java.util.Arrays;

import org.easymock.EasyMock;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.security.Credential.MD5;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.mm.bobcat.ARunningMongoDBTest;
import uk.mm.bobcat.domain.BobcatUser;
import uk.mm.bobcat.service.BobcatServiceException.BobcatError;
import uk.mm.bobcat.service.repos.UserRepository;

public class BobcatLoginServiceTest extends ARunningMongoDBTest {

	@Autowired
	private BobcatLoginService bobcatLoginService;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void beforeTest() {
		userRepository.save(new BobcatUser("first_user", md5("first_pw"), new String[] { "user", "admin" }));
		userRepository.save(new BobcatUser("second", md5("sec_pw"), "user"));
		userRepository.save(new BobcatUser("3rd", md5("3rd"), "user"));
	}

	private String md5(String in) {
		return MD5.digest(in);
	}

	@Test
	public void testAdminLoginUnsuccessful() {
		String userName = "first_user";
		String password = "wrong_pw";
		UserIdentity login = bobcatLoginService.login(userName, password);
		Assert.assertNull(login);
	}

	@Test
	public void testAdminLoginSuccessful() {
		String userName = "first_user";
		String password = "first_pw";
		UserIdentity login = bobcatLoginService.login(userName, password);
		checkUserIdentity(login, userName, "admin", "user");
	}

	@Test
	public void testUserLoginSuccessful() {
		String userName = "3rd";
		String password = "3rd";
		UserIdentity login = bobcatLoginService.login(userName, password);
		checkUserIdentity(login, userName, "user");
	}

	@Test
	public void testRegisterUserSuccess() {
		String name = "fourth user";
		String[] roles = new String[] { "user" };
		BobcatUser newUser = bobcatLoginService.registerUser(name, "anypw", roles);
		Assert.assertNotNull(newUser);
		Assert.assertEquals(name, newUser.getName());
		Assert.assertTrue(Arrays.equals(roles, newUser.getRoles()));

		Assert.assertEquals(4, userRepository.count());
	}

	@Test
	public void testRegisterUserAlreadyExists() {
		try {
			bobcatLoginService.registerUser("second", "sec_pw", "user");
			Assert.fail("expected exception not thrown!");
		} catch (BobcatServiceException bse) {
			Assert.assertEquals(BobcatError.NAME_ALREADY_IN_USE, bse.getReason());
		}
	}

	@Test
	public void testValidateWithoutLogin() {
		String userName = "first_user";

		UserIdentity userIdentityMock = mockUserIdentity(userName);
		Assert.assertFalse(bobcatLoginService.validate(userIdentityMock));
	}

	@Test
	public void testValidateAfterLoginSuccessful() {
		String userName = "first_user";
		String password = "first_pw";
		bobcatLoginService.login(userName, password);

		UserIdentity userIdentityMock = mockUserIdentity(userName);
		Assert.assertTrue(bobcatLoginService.validate(userIdentityMock));
		EasyMock.verify(userIdentityMock);
	}

	@Test
	public void testValidateAfterRegisterSuccessful() {
		String userName = "anotheruser";
		String password = "simple";
		bobcatLoginService.registerUser(userName, password, new String[] { "user" });

		UserIdentity userIdentityMock = mockUserIdentity(userName);
		Assert.assertTrue(bobcatLoginService.validate(userIdentityMock));
		EasyMock.verify(userIdentityMock);
	}

	@Test
	public void testValidateUnsuccessful() {
		UserIdentity userIdentityMock = mockUserIdentity("somename");
		Assert.assertFalse(bobcatLoginService.validate(userIdentityMock));
	}

	@Test
	public void testValidateTimedOutAfterLogin() throws InterruptedException {
		try {
			String username = "second";
			String password = "sec_pw";
			bobcatLoginService.login(username, password);
			UserIdentity userIdentityMock = mockUserIdentity(username);

			bobcatLoginService.setLoggedInTimeout(10);
			Thread.sleep(50);
			Assert.assertFalse(bobcatLoginService.validate(userIdentityMock));
		} finally {
			bobcatLoginService.setLoggedInTimeout(BobcatLoginService.DEFAULT_TIMEOUT);
		}
	}

	@Test
	public void testValidateTimedOutAfterOtherValidate() throws InterruptedException {
		try {
			String username = "second";
			String password = "sec_pw";
			bobcatLoginService.login(username, password);

			UserIdentity userIdentityMock = mockUserIdentity(username, 2);

			bobcatLoginService.setLoggedInTimeout(64);
			Assert.assertTrue(bobcatLoginService.validate(userIdentityMock));
			Thread.sleep(100);
			Assert.assertFalse(bobcatLoginService.validate(userIdentityMock));
		} finally {
			bobcatLoginService.setLoggedInTimeout(BobcatLoginService.DEFAULT_TIMEOUT);
		}
	}

	@Test
	public void testValidateResetsTimeout() throws InterruptedException {
		try {
			String username = "second";
			String password = "sec_pw";
			bobcatLoginService.login(username, password);

			UserIdentity userIdentityMock = mockUserIdentity(username, 2);
			bobcatLoginService.setLoggedInTimeout(100);
			Thread.sleep(50);
			Assert.assertTrue(bobcatLoginService.validate(userIdentityMock));
			Thread.sleep(50);
			Assert.assertTrue(bobcatLoginService.validate(userIdentityMock));
		} finally {
			bobcatLoginService.setLoggedInTimeout(BobcatLoginService.DEFAULT_TIMEOUT);
		}
	}

	public void testValidateTimedOutAfterRegistration() throws InterruptedException {
		try {
			String username = "second";
			String password = "sec_pw";
			bobcatLoginService.registerUser(username, password, new String[] {});
			UserIdentity userIdentityMock = mockUserIdentity(username);

			bobcatLoginService.setLoggedInTimeout(64);
			Assert.assertTrue(bobcatLoginService.validate(userIdentityMock));
			Thread.sleep(100);
			Assert.assertFalse(bobcatLoginService.validate(userIdentityMock));
		} finally {
			bobcatLoginService.setLoggedInTimeout(BobcatLoginService.DEFAULT_TIMEOUT);
		}
	}

	private void checkUserIdentity(UserIdentity login, String expectedName, String... expectedRoles) {
		Assert.assertNotNull(login);
		Assert.assertEquals(expectedName, login.getUserPrincipal().getName());
		for (String expectedRole : expectedRoles) {
			Assert.assertTrue(login.isUserInRole(expectedRole, null));
		}
	}

	private UserIdentity mockUserIdentity(String username) {
		return mockUserIdentity(username, 1);
	}

	private UserIdentity mockUserIdentity(String username, int times) {
		UserIdentity userIdentityMock = EasyMock.createMock(UserIdentity.class);
		Principal principal = EasyMock.createMock(Principal.class);
		EasyMock.expect(principal.getName()).andReturn(username).times(times);
		EasyMock.expect(userIdentityMock.getUserPrincipal()).andReturn(principal).times(times);
		EasyMock.replay(principal);
		EasyMock.replay(userIdentityMock);
		return userIdentityMock;
	}
}
