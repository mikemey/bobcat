package uk.mm.bobcat.controller;

import org.eclipse.jetty.security.authentication.FormAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController extends CommonDataController {
	private final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String serveLoginPage(Model model) {
		logger.info("{} [Login Overlay]", LMT.PAGE_REQ);

		model.addAttribute("loginUrl", FormAuthenticator.__J_SECURITY_CHECK);
		model.addAttribute("userParam", FormAuthenticator.__J_USERNAME);
		model.addAttribute("passwordParam", FormAuthenticator.__J_PASSWORD);
		return "loginOverlay";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String serveRegisterPage() {
		return "register";
	}
}