package uk.mm.bobcat.service;

import java.io.Serializable;
import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.security.DefaultIdentityService;
import org.eclipse.jetty.security.DefaultUserIdentity;
import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.security.Credential.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import uk.mm.bobcat.controller.LMT;
import uk.mm.bobcat.domain.BobcatUser;
import uk.mm.bobcat.service.BobcatServiceException.BobcatError;
import uk.mm.bobcat.service.repos.UserRepository;

public class BobcatLoginService implements LoginService {
	/** default timeout in milliseconds: **/
	public static final int DEFAULT_TIMEOUT = 5 * 60 * 1000;
	private final Logger logger = LoggerFactory.getLogger(BobcatLoginService.class);

	private IdentityService identityService = new DefaultIdentityService();
	private ConcurrentMap<String, BobcatUserIdentity> users = new ConcurrentHashMap<>();
	private int loggedInTimeout = DEFAULT_TIMEOUT;
	private UserRepository userRepository;

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Sets the timeout (in milliseconds) after which a user without any activity (validation/login) 
	 * is automatically logged out.
	 * @param timeout
	 */
	public void setLoggedInTimeout(int timeout) {
		loggedInTimeout = timeout;
	}

	@Override
	public String getName() {
		return "Bobcat server";
	}

	@Override
	public IdentityService getIdentityService() {
		return identityService;
	}

	@Override
	public void setIdentityService(IdentityService service) {
		identityService = service;
	}

	public BobcatUser registerUser(String username, String clearPassword, String... roles) {
		String encryptedPassword = MD5.digest(clearPassword);
		if (userRepository.findByName(username) != null) {
			throw new BobcatServiceException(BobcatError.NAME_ALREADY_IN_USE);
		}
		logger.info("{} user registration: {}", LMT.USER_AUTH, username);
		BobcatUser newUser = new BobcatUser(username, encryptedPassword, roles);
		BobcatUser savedUser = userRepository.save(newUser);
		loadUserIdentity(username, clearPassword);
		return savedUser;
	}

	/**
	 * @see LoginService#login(String, Object)
	 */
	@Override
	public UserIdentity login(String username, Object credentials) {
		logger.info("{} user: {}", LMT.USER_AUTH, username);
		if (!(credentials instanceof String)) {
			return null;
		}
		String clearPassword = (String) credentials;
		if (StringUtils.isBlank(username) || StringUtils.isBlank(clearPassword)) {
			return null;
		}
		return loadUserIdentity(username, clearPassword);
	}

	@Override
	public void logout(UserIdentity user) {
		System.out.println("BobcatLoginService.logout(): " + user);
	}

	@Override
	public boolean validate(UserIdentity user) {
		String userName = user.getUserPrincipal().getName();
		logger.debug("{} validate user: {}", LMT.USER_AUTH, userName);
		BobcatUserIdentity userIdentity = users.get(userName);
		if (userIdentity == null) {
			return false;
		}
		return userIdentity.isValid(loggedInTimeout);
	}

	private BobcatUserIdentity loadUserIdentity(String username, String clearPassword) {
		String encryptedPassword = MD5.digest(clearPassword);
		BobcatUser user = userRepository.findByNameAndPassword(username, encryptedPassword);
		if (user == null) {
			return null;
		}
		BobcatUserIdentity userIdentity = new BobcatUserIdentity(new KnownUser(username), user.getRoles());
		users.put(username, userIdentity);
		return userIdentity;
	}

	private class BobcatUserIdentity extends DefaultUserIdentity {

		private long lastActiveTime;

		public BobcatUserIdentity(KnownUser principal, String[] roles) {
			super(null, principal, roles);
			lastActiveTime = System.currentTimeMillis();
		}

		public boolean isValid(long timeout) {
			long currTime = System.currentTimeMillis();
			boolean valid = (lastActiveTime + timeout) > currTime;
			lastActiveTime = currTime;
			return valid;
		}
	}

	private class KnownUser implements Principal, Serializable {
		private static final long serialVersionUID = -8665196527455442034L;
		private final String name;

		public KnownUser(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			KnownUser other = (KnownUser) obj;
			if (name == null) {
				if (other.name != null) return false;
			} else if (!name.equals(other.name)) return false;
			return true;
		}
	}
}