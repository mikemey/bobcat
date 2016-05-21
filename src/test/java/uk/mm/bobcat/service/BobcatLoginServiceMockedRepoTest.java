package uk.mm.bobcat.service;

import java.util.Arrays;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.security.Credential.MD5;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.mm.bobcat.domain.BobcatUser;
import uk.mm.bobcat.service.repos.UserRepository;

public class BobcatLoginServiceMockedRepoTest {

	private BobcatLoginService loginService;
	private UserRepository userRepoMock;
	String username = "username";
	String clearPass = "unencoded";
	private String encodedPass;

	@Before
	public void setup() {
		loginService = new BobcatLoginService();
		userRepoMock = EasyMock.createMock(UserRepository.class);
		loginService.setUserRepository(userRepoMock);
		encodedPass = MD5.digest(clearPass);
	}

	@Test
	public void testPasswordStoreEncrypted() {
		BobcatUser user = new BobcatUser(username, clearPass, new String[] { "user" });
		user.setId("anyid");
		BobcatUserMatcher buMatcher = new BobcatUserMatcher(user.getName(), encodedPass, user.getRoles());

		EasyMock.expect(userRepoMock.findByName(EasyMock.eq(username))).andReturn(null);
		EasyMock.expect(userRepoMock.save(EasyMock.capture(buMatcher))).andReturn(user);
		EasyMock.expect(userRepoMock.findByNameAndPassword(username, encodedPass)).andReturn(user);
		EasyMock.replay(userRepoMock);
		loginService.registerUser(username, clearPass, new String[] { "user" });
		buMatcher.verify();
		EasyMock.verify(userRepoMock);
	}

	@Test
	public void testPasswordCheckEncrypted() {
		BobcatUser user = new BobcatUser();
		user.setId("userId");

		EasyMock.expect(userRepoMock.findByNameAndPassword(username, encodedPass)).andReturn(user);
		EasyMock.replay(userRepoMock);
		UserIdentity loggedInUser = loginService.login(username, clearPass);
		EasyMock.verify(userRepoMock);
		Assert.assertEquals(username, loggedInUser.getUserPrincipal().getName());
	}

	class BobcatUserMatcher extends Capture<BobcatUser> {
		private static final long serialVersionUID = 2146489825717236642L;
		private String expectedName;
		private String expectedPassword;
		private String[] expectedRoles;

		public BobcatUserMatcher(String expectedName, String expectedPassword, String[] expectedRoles) {
			this.expectedName = expectedName;
			this.expectedPassword = expectedPassword;
			this.expectedRoles = expectedRoles;
		}

		public void verify() {
			BobcatUser actualUser = getValue();
			Assert.assertEquals(expectedName, actualUser.getName());
			Assert.assertEquals(expectedPassword, actualUser.getPassword());
			Assert.assertTrue(Arrays.equals(expectedRoles, actualUser.getRoles()));
		}
	}
}